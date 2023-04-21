package com.jtstand.statistics;

/**
 *
 * @author albert_kurucz
 */
public class Stat {

    private int N = 0;

    private double sum = 0.0;

    private double sum2 = 0.0;

    private double min = 0.0;

    private double max = 0.0;

    /** Creates a new instance of Stat */
    public Stat() {
    }

    public boolean isDistribution() {
        return N > 2 && getStandardDeviation() != 0.0;
    }

    public void addValue(double val) {
        if (N++ == 0) {
            min = val;
            max = val;
        } else {
            min = Math.min(min, val);
            max = Math.max(max, val);
        }
        sum += val;
        sum2 += val * val;
    }

    public void substractValue(double val) {
        N--;
        sum -= val;
        sum2 -= val * val;
    }

    public int getN() {
        return N;
    }

    public double getAverage() {
        return sum / N;
    }

    public double getStandardDeviation() {
        if (N < 2) {
            return 0;
        }
        if ((N * sum2 - sum * sum) < 0.0) {
            return 0.0;
        }
        return Math.sqrt(N * sum2 - sum * sum) / N;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getCP(Double LSL, Double USL) {
        if (LSL == null) {
            throw new IllegalArgumentException("LSL is null");
        }
        if (USL == null) {
            throw new IllegalArgumentException("USL is null");
        }
        if (isDistribution()) {
            return (USL - LSL) / (6 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPL(Double LSL) {
        if (isDistribution()) {
            return (getAverage() - LSL.doubleValue()) / (3 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPU(Double USL) {
        if (isDistribution()) {
            return (USL.doubleValue() - getAverage()) / (3 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPK(Double LSL, Double USL) {
        if (LSL == null && USL == null) {
            return Double.MAX_VALUE;
        }
        if (USL == null) {
            return getCPL(LSL);
        }
        if (LSL == null) {
            return getCPU(USL);
        }
        return Math.min(getCPL(LSL), getCPU(USL));
    }
}
