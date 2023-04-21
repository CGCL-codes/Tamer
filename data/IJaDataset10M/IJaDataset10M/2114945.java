package org.thechiselgroup.choosel.core.client.util.math;

public class MinCalculation implements Calculation {

    @Override
    public double calculate(NumberArray values) {
        return values.min();
    }

    @Override
    public String getDescription() {
        return "Minimum";
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
