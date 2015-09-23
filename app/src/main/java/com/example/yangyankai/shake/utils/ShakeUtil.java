package com.example.yangyankai.shake.utils;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by yangyankai on 2015/9/7.
 */
public class ShakeUtil {

    private static long lastClickTime = 0;// 记录最后一次触发事件，用于防止短时间内多次触发摇动事件
    private static SensorManager sensorManager = null;
    private static Vibrator vibrator = null; // 振动传感器

    public static void open(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    private static SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //加速度可能是负值，所以要取他们的绝对值
            float xValue = Math.abs(sensorEvent.values[0]);
            float yValue = Math.abs(sensorEvent.values[1]);
            float zValue = Math.abs(sensorEvent.values[2]);
            if (xValue > 15 || yValue > 15 || zValue > 15) {

                if (allowShake()) {//判断是否为重复晃动
                    Log.e("aaa", "摇一摇，摇一摇 in the Util");
                    vibrator.vibrate(500);
//                    new AllowShake().start();
                } else {
//                    Log.e("aaa", "2s 后再次允许摇动");
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private static boolean allowShake() {
        if ( System.currentTimeMillis()-lastClickTime>800){// 时间间隔大于 0.8 秒，可以摇动
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    public static void close(){
        if(sensorManager!=null)
            sensorManager.unregisterListener(listener);
    }
}
