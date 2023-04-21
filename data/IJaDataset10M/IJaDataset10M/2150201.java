package com.akjava.lib.android.activity;

import android.app.Activity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

/**
 * 動きが激しいアクションゲームとか向き?
 * 
 * @author aki
 *
 */
public abstract class PausePatternActivity extends Activity implements SensorListener {

    protected SensorManager sensorManager;

    protected boolean enableSensor = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInitialize(savedInstanceState);
        Log.i("sensor", "start");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Log.i("sensor", "end");
        if (savedInstanceState == null) {
            doStart();
        } else {
            doRestore(savedInstanceState);
        }
    }

    @Override
    protected void onStop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doResume();
        if (enableSensor) {
            sensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER | SensorManager.SENSOR_ORIENTATION, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        doPause();
    }

    /**
	     * Activityを初期化します。
	     * @param savedInstanceState
	     */
    public abstract void doInitialize(Bundle savedInstanceState);

    /**
	     * Activityを開始状態にします。
	     */
    public abstract void doStart();

    /**
	     * ActivityをSaveした状態から戻します。
	     * @param savedInstanceState
	     * ただ、doSave()が保存したデーターが読めないケースもあるみたいです。(あるいは保存前に呼ばれる?)
	     * その時の処理は、doStartかdoPauseをした方がいいのかな?
	     * 
	     * 開発ツールから、エミュレーターとアプリを同時に呼び出しや
	     * エミュレターでは電源切ってるのにアプリが起動できるので、そこから呼ばれることもあります。
	     * 
	     */
    public abstract void doRestore(Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        doSave();
    }

    /**
	     * Activityの状態を保存します。ここで保存した情報を元にRestoreします。
	     */
    public abstract void doSave();

    /**
	     * Activityを一時停止状態にします。
	     */
    public abstract void doPause();

    public abstract void doResume();

    public abstract void onSensorOrientation(float values[]);

    public void onAccuracyChanged(int sensor, int accuracy) {
    }

    protected float[] currentOrientationValues = { 0.0f, 0.0f, 0.0f };

    public void onSensorChanged(int sensor, float[] values) {
        switch(sensor) {
            case SensorManager.SENSOR_ACCELEROMETER:
                currentOrientationValues[0] = values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
                currentOrientationValues[1] = values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
                currentOrientationValues[2] = values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
                onSensorOrientation(currentOrientationValues);
                break;
            default:
        }
    }
}
