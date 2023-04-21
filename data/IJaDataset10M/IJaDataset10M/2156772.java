package org.mcisb.beacon.analysis;

import java.util.ArrayList;

public final class DoubleArrayUtils {

    /**
	 * Convert an ArrayList of double arrays to a 2d primitive array.
	 * 
	 * @param al ArrayList of double[]
	 * @return double[][]
	 */
    public static double[][] listToDouble(ArrayList<double[]> al) {
        double[][] d = new double[al.size()][al.get(0).length];
        for (int i = 0; i < al.size(); i++) {
            for (int j = 0; j < al.get(i).length; j++) {
                d[i][j] = al.get(i)[j];
            }
        }
        return d;
    }

    /**
	 * Convert an ArrayList of Double to a primitive array.
	 * @param al ArrayList of Double
	 * @return double[]
	 */
    public static double[] listToDouble(ArrayList<Double> al) {
        double[] d = new double[al.size()];
        for (int i = 0; i < al.size(); i++) {
            d[i] = al.get(i).doubleValue();
        }
        return d;
    }

    /**
	 * Get maximum value in array of doubles.
	 * @param d
	 * @return double
	 */
    public static double max(double[] d) {
        double max = d[0];
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    /**
	 * Get minimum value in an array of doubles.
	 * @param d
	 * @return double
	 */
    public static double min(double[] d) {
        double min = d[0];
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    /**
	 * Get ln of values in an array of doubles.
	 * @param d
	 * @return double[]
	 */
    public static double[] log(double[] d) {
        double[] output = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            output[i] = Math.log(d[i]);
        }
        return output;
    }

    /**
	 * Get index of a double value in an array of doubles
	 * @param d
	 * @param val
	 * @return int
	 */
    public static int indexOf(double[] d, double val) {
        int index = 0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] == val) {
                index = i;
                return index;
            }
        }
        return index;
    }

    /**
	 * Get absolute values for an array of doubles.
	 * @param d
	 * @return double[]
	 */
    public static double[] abs(double[] d) {
        double[] output = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            output[i] = Math.abs(d[i]);
        }
        return output;
    }

    /**
	 * Get derivatives for an array of doubles.
	 * @param d
	 * @return double[]
	 */
    public static double[] derivative(double[] d) {
        double[] deriv = new double[d.length];
        deriv[0] = d[1] - d[0];
        deriv[d.length - 1] = d[d.length - 1] - d[d.length - 2];
        for (int j = 1; j < deriv.length - 1; j++) {
            deriv[j] = (d[j + 1] - d[j - 1]) / 2;
        }
        return deriv;
    }

    /**
	 * Scalar divide for an array of doubles.
	 * @param d Array of doubles
	 * @param q Divisor
	 * @return double[]
	 */
    public static double[] divide(double[] d, double q) {
        double[] answer = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            answer[i] = d[i] / q;
        }
        return answer;
    }

    /**
	 * Scalar add for an array of doubles
	 * @param d Array of doubles.
	 * @param a Scalar.
	 * @return double[]
	 */
    public static double[] add(double[] d, double a) {
        double[] answer = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            answer[i] = d[i] + a;
        }
        return answer;
    }

    /**
	 * Scalar subtract for an array of doubles
	 * @param d Array of doubles.
	 * @param s Scalar
	 * @return double[]
	 */
    public static double[] subtract(double[] d, double s) {
        double[] answer = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            answer[i] = d[i] - s;
        }
        return answer;
    }

    /**
	 * Scalar multiply for an array of doubles
	 * @param d Array of doubles
	 * @param m Scalar
	 * @return double[]
	 */
    public static double[] multiply(double[] d, double m) {
        double[] answer = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            answer[i] = d[i] * m;
        }
        return answer;
    }

    /**
	 * 
	 * @param d Array of doubles
	 * @return Index of last value before 0 crossing.
	 */
    public static int zeroCross(double[] d) {
        for (int i = 0; i < d.length - 1; i++) {
            if (sign(d[i]) < sign(d[i + 1])) {
                return i;
            }
        }
        return 0;
    }

    /**
	 *
	 * @param d
	 * @return int
	 */
    private static int sign(double d) {
        int s;
        if (d == 0) {
            s = 0;
        } else if (d > 0) {
            s = 1;
        } else {
            s = -1;
        }
        return s;
    }
}
