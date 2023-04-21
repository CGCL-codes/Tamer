package com.etracks.dades;

import android.hardware.SensorManager;

public class CtrlCompass {

    public CtrlCompass() {
    }

    public float getOrientation() {
        float[] R = new float[16];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, null, null);
        SensorManager.getOrientation(R, values);
        return values[0];
    }
}
