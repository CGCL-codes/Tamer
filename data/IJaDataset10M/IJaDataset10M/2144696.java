package org.processmining.mining.organizationmining.util;

import java.util.ArrayList;

/**
 * @author alexander
 * 
 */
public class Normalisation {

    public static double getMaximum(ArrayList<Double> original) {
        double maximum = original.get(original.size());
        for (int x = original.size() - 1; x >= 0; x--) {
            if (maximum < original.get(x)) maximum = original.get(x);
        }
        return maximum;
    }

    public static double getMinimum(ArrayList<Double> original) {
        double minimum = original.get(original.size());
        for (int x = original.size() - 1; x >= 0; x--) {
            if (minimum > original.get(x)) minimum = original.get(x);
        }
        return minimum;
    }

    public static double getMean(ArrayList<Double> original) {
        double sum = 0.0;
        for (int x = original.size(); x >= 0; x--) {
            sum += original.get(x);
        }
        return sum / original.size();
    }

    public static double getStandardDeviation(ArrayList<Double> original) {
        double mean = getMean(original);
        return getStdDev(original, mean);
    }

    private static double getStdDev(ArrayList<Double> original, double mean) {
        double sum = 0.0;
        for (int x = original.size(); x >= 0; x--) {
            double diff = original.get(x) - mean;
            sum += (diff * diff);
        }
        return Math.sqrt(sum / original.size());
    }

    /**
	 * 
	 * @param original
	 * @return normalised list normalisation = subtract mean and divide by the
	 *         stddev
	 */
    public static ArrayList<Double> normalise(ArrayList<Double> original) {
        double mean = getMean(original);
        double stddev = getStdDev(original, mean);
        ArrayList<Double> normalised = new ArrayList<Double>();
        for (int x = original.size(); x >= 0; x--) {
            normalised.add((original.get(x) - mean) / stddev);
        }
        return normalised;
    }
}
