package com.vividsolutions.jts.index.sweepline;

/**
 * @version 1.7
 */
public class SweepLineInterval {

    private double min, max;

    private Object item;

    public SweepLineInterval(double min, double max) {
        this(min, max, null);
    }

    public SweepLineInterval(double min, double max, Object item) {
        this.min = min < max ? min : max;
        this.max = max > min ? max : min;
        this.item = item;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public Object getItem() {
        return item;
    }
}
