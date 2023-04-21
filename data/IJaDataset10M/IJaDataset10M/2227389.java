package com.nhncorp.usf.core.service.mock;

/**
 * CalculatorBO
 *
 * @author Web Platform Development Team
 */
public class CalculatorBO {

    public int add(int a, int b) {
        return a + b;
    }

    public double divide(double a, double b) {
        return ((double) a) / ((double) b);
    }

    public double multiple(double a, double b) {
        return a * b;
    }
}
