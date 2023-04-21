package com.jme3.noise.module;

public class Const extends Module {

    private double constValue = 0.0;

    public Const() {
        super(0);
    }

    @Override
    public double getValue(double x, double y, double z) {
        return constValue;
    }

    public double getConstValue() {
        return constValue;
    }

    public void setConstValue(double constValue) {
        this.constValue = constValue;
    }
}
