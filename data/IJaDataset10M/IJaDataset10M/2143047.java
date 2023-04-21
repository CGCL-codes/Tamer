package com.jme3.noise.module;

public class Min extends Module {

    public Min() {
        super(2);
    }

    @Override
    public double getValue(double x, double y, double z) {
        return Math.min(sourceModule[0].getValue(x, y, z), sourceModule[1].getValue(x, y, z));
    }
}
