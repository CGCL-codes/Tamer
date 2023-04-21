package edu.harvard.syrah.nc;

public class EWMAStatistic {

    public static final double GAIN = 0.01;

    protected final double gain;

    protected double value;

    public EWMAStatistic(double g) {
        gain = g;
        value = 0;
    }

    public EWMAStatistic() {
        gain = GAIN;
        value = 0;
    }

    public synchronized void add(double item) {
        value = (GAIN * item) + ((1. - GAIN) * value);
    }

    public synchronized double get() {
        return value;
    }
}
