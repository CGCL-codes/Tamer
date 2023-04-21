package org.processmining.analysis.traceclustering.util;

import java.util.ArrayList;
import java.util.Collections;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * @author christian
 * 
 */
public class MatrixUtils {

    public static double getMaximum(DoubleMatrix2D original) {
        double maximum = 0.0;
        for (int x = original.rows() - 1; x >= 0; x--) {
            for (int y = original.columns() - 1; y >= 0; y--) {
                maximum = Math.max(maximum, original.get(x, y));
            }
        }
        return maximum;
    }

    public static double getMinimum(DoubleMatrix2D original) {
        double minimum = 0.0;
        for (int x = original.rows() - 1; x >= 0; x--) {
            for (int y = original.columns() - 1; y >= 0; y--) {
                minimum = Math.min(minimum, original.get(x, y));
            }
        }
        return minimum;
    }

    public static double getMean(DoubleMatrix2D original) {
        double sum = 0.0;
        for (int x = original.rows() - 1; x >= 0; x--) {
            for (int y = original.columns() - 1; y >= 0; y--) {
                sum += original.get(x, y);
            }
        }
        return sum / (original.rows() * original.columns());
    }

    public static double getMedian(DoubleMatrix2D original) {
        ArrayList<Double> valueSet = new ArrayList<Double>();
        double current;
        for (int x = original.rows() - 1; x >= 0; x--) {
            for (int y = original.columns() - 1; y >= 0; y--) {
                current = original.get(x, y);
                if (valueSet.contains(current) == false) {
                    valueSet.add(current);
                }
            }
        }
        Collections.sort(valueSet);
        return valueSet.get(valueSet.size() / 2);
    }

    public static DoubleMatrix2D normalize(DoubleMatrix2D original, double normalizationMaximum) {
        double originalMaximum = getMaximum(original);
        if (originalMaximum != 0.0) {
            double factor = normalizationMaximum / originalMaximum;
            if (factor != 1.0) {
                original.assign(cern.jet.math.Mult.mult(factor));
            }
        }
        return original;
    }

    public static DoubleMatrix2D invert(DoubleMatrix2D original) {
        final double max = getMaximum(original);
        original.assign(new DoubleFunction() {

            public double apply(double value) {
                return Math.max(0.0, (max - value));
            }
        });
        return original;
    }
}
