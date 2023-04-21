package org.powermock;

public class ClassWithSeveralMethodsWithSameNameOneWithoutParameters {

    public double getDouble() {
        return Double.MAX_VALUE;
    }

    public double getDouble(double value) {
        return value;
    }

    public double getDouble(double value1, double value2) {
        return value1 + value2;
    }
}
