package com.dukesoftware.utils.math.function;

public class LinearFunction implements IFunction {

    private final double a, b;

    public LinearFunction(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double f(double x) {
        return a * x + b;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }
}
