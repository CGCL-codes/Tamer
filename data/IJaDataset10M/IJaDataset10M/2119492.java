package com.jme3.noise.module;

public class Clamp extends Module {

    protected double lowerBound = -1.0;

    protected double upperBound = 1.0;

    public Clamp() {
        super(1);
    }

    public void setBounds(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double value = sourceModule[0].getValue(x, y, z);
        if (value < lowerBound) {
            return lowerBound;
        } else if (value > upperBound) {
            return upperBound;
        } else {
            return value;
        }
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }
}
