package com.enerjy.index.model.distribution;

import com.enerjy.index.model.IDistribution;

/**
 * Implementation of IDistribution to represent a normal distribution with a given mean and variance.
 */
public class NormalDistribution implements IDistribution {

    private static final long serialVersionUID = 1L;

    private static final double LOG_ROOT_2_PI = (Math.log(2.0) + Math.log(Math.PI)) / 2;

    private static final double EPSILON = 1e-8;

    private double mean;

    private double variance;

    /**
     * Create a NormalDistribution with the given mean and variance.
     * @param mean
     * @param variance
     */
    public NormalDistribution(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double getLogDensity(double value, double weight) {
        if (getVariance() < EPSILON) {
            return 0;
        }
        value *= weight;
        double sigma = Math.sqrt(getVariance());
        double mu = getMean();
        double normalizedValue = (value - mu) / sigma;
        return -LOG_ROOT_2_PI - Math.log(sigma) - normalizedValue * normalizedValue / 2;
    }

    @Override
    public String toString() {
        return String.format("Normal(%4.2f, %4.2f)", getMean(), Math.sqrt(getVariance()));
    }
}
