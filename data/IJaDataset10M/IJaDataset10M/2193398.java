package org.jdmp.core.algorithm.estimator;

public class DiscreteDensityEstimator implements DensityEstimator {

    private double[] counts;

    private double sumOfCounts;

    public DiscreteDensityEstimator(int nmbVals, boolean laplace) {
        counts = new double[nmbVals];
        sumOfCounts = 0;
        if (laplace) {
            for (int i = 0; i < nmbVals; i++) {
                counts[i] = 1;
            }
            sumOfCounts = (double) nmbVals;
        }
    }

    public DiscreteDensityEstimator(int nmbVals, double correction) {
        counts = new double[nmbVals];
        sumOfCounts = 0;
        for (int i = 0; i < nmbVals; i++) {
            counts[i] = correction;
        }
        sumOfCounts = (double) nmbVals;
    }

    public void addValue(int val, double weight) {
        counts[val] += weight;
        sumOfCounts += weight;
    }

    public void addValue(int val) {
        counts[val]++;
        sumOfCounts++;
    }

    public double getProbability(int val) {
        if (sumOfCounts == 0) {
            return 0;
        }
        return counts[val] / sumOfCounts;
    }
}
