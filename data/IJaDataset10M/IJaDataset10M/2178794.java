package com.stromberglabs.cluster;

import java.util.List;
import com.stromberglabs.cluster.Clusterable;

public class ClusterUtils {

    public static double getEuclideanDistance(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("Attempting to compare two clusterables of different dimensions");
        }
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public static double getEuclideanDistance(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("Attempting to compare two clusterables of different dimensions");
        }
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public static double getEuclideanDistance(Clusterable a, Clusterable b) {
        return getEuclideanDistance(a.getLocation(), b.getLocation());
    }

    public static double sumDifferences(List<Double> a, List<Double> b) {
        assert (a.size() == b.size());
        double sumDiff = 0;
        double aSum = 0;
        double bSum = 0;
        for (int i = 0; i < a.size(); i++) {
            sumDiff += Math.abs(a.get(i) - b.get(i));
            aSum += a.get(i);
            bSum += b.get(i);
        }
        return sumDiff;
    }
}
