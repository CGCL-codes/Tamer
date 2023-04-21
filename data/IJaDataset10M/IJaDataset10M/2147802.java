package com.st.rrd.model;

public class ValueAxisSetting {

    private final double gridStep;

    private final int labelFactor;

    ValueAxisSetting(double gridStep, int labelFactor) {
        this.gridStep = gridStep;
        this.labelFactor = labelFactor;
    }

    public double getGridStep() {
        return gridStep;
    }

    public int getLabelFactor() {
        return labelFactor;
    }
}
