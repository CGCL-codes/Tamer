package netkit.util;

import java.util.Random;

public class VectorMath {

    private static long seed = System.currentTimeMillis();

    public static Random pickRandom = new Random(seed);

    public static double sum(double[] array) {
        double sum = 0;
        if (array != null) for (double d : array) sum += d;
        return sum;
    }

    public static int sum(int[] array) {
        int sum = 0;
        if (array != null) for (int i : array) sum += i;
        return sum;
    }

    public static double dotproduct(double[] arr1, double[] arr2) {
        if (arr1 == null && arr2 == null) return 0;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot calculate dotproduct two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        double value = 0;
        for (int i = 0; i < arr1.length; i++) value += arr1[i] * arr2[i];
        return value;
    }

    public static int dotproduct(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) return 0;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot calculate dotproduct two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        int value = 0;
        for (int i = 0; i < arr1.length; i++) value += arr1[i] * arr2[i];
        return value;
    }

    public static double l1diff(double[] arr1, double[] arr2) {
        if (arr1 == null && arr2 == null) return 0;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot calculate dotproduct two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        double value = 0;
        for (int i = 0; i < arr1.length; i++) value += Math.abs(arr1[i] - arr2[i]);
        return value;
    }

    public static double l1diff(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) return 0;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot calculate dotproduct two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        double value = 0;
        for (int i = 0; i < arr1.length; i++) value += Math.abs(arr1[i] - arr2[i]);
        return value;
    }

    /**
   * combine arrays 1 and 2:  result = alpha*arr1 + (1-alpha)*arr2
   * @param alpha
   * @param arr1
   * @param arr2
   * @return alpha*arr1 + (1-alpha)*arr2
   */
    public static double[] merge(double alpha, double[] arr1, double[] arr2) {
        if (arr1 == null && arr2 == null) return null;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot merge two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        if (alpha < 0 || alpha > 1) throw new ArithmeticException("invalid alpha=" + alpha + " (must lie in the range 0-1 inclusive)");
        double[] result = new double[arr1.length];
        merge(alpha, arr1, arr2, result);
        if (alpha == 1) System.arraycopy(arr2, 0, result, 0, arr2.length); else if (alpha == 0) System.arraycopy(arr1, 0, result, 0, arr2.length); else {
            double beta = 1 - alpha;
            for (int c = 0; c < result.length; c++) {
                result[c] = alpha * arr1[c] + beta * arr2[c];
            }
        }
        return result;
    }

    /**
   * combine arrays 1 and 2:  result = alpha*arr1 + (1-alpha)*arr2
   * @param alpha
   * @param arr1
   * @param arr2
   * @param result
   */
    public static void merge(double alpha, double[] arr1, double[] arr2, double[] result) {
        if (arr1 == null && arr2 == null) return;
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new ArithmeticException("cannot merge two arrays of different sizes arr1=" + ((arr1 == null) ? -1 : arr1.length) + " arr2=" + ((arr2 == null) ? -1 : arr2.length));
        if (result == null || result.length < arr2.length) throw new ArithmeticException("result array length=" + ((result == null) ? -1 : result.length) + " is too small.  need to have length=" + ((arr2 == null) ? -1 : arr2.length));
        if (alpha < 0 || alpha > 1) throw new ArithmeticException("invalid alpha=" + alpha + " (must lie in the range 0-1 inclusive)");
        if (alpha == 1) System.arraycopy(arr1, 0, result, 0, arr1.length); else if (alpha == 0) System.arraycopy(arr2, 0, result, 0, arr2.length); else {
            double beta = 1 - alpha;
            for (int c = 0; c < result.length; c++) {
                result[c] = alpha * arr1[c] + beta * arr2[c];
            }
        }
    }

    public static void normalize(double[] e) {
        double tot = 0;
        for (double d : e) tot += d;
        if (tot == 0) {
            tot = 1.0 / (double) e.length;
            for (int i = 0; i < e.length; i++) e[i] = tot;
        } else if (tot != 1.0) {
            for (int i = 0; i < e.length; i++) e[i] /= tot;
        }
    }

    /**
   * Add array 2 into array 1
   * @param arr1
   * @param arr2
   */
    public static void add(double[] arr1, double[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new IllegalArgumentException("Arrays1 and 2 are not of same length!");
        for (int i = 0; i < arr1.length; i++) arr1[i] += arr2[i];
    }

    /**
   * Subtract array 2 from array 1
   * @param arr1
   * @param arr2
   */
    public static void subtract(double[] arr1, double[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new IllegalArgumentException("Arrays1 and 2 are not of same length!");
        for (int i = 0; i < arr1.length; i++) arr1[i] -= arr2[i];
    }

    /**
   * Divide array by given factor
   * @param arr
   * @param div
   */
    public static void divide(double[] arr, double div) {
        if (arr == null || arr.length == 0) return;
        for (int i = 0; i < arr.length; i++) arr[i] /= div;
    }

    /**
   * Multiply array by given factor
   * @param arr
   * @param factor
   */
    public static void multiply(double[] arr, double factor) {
        if (arr == null || arr.length == 0) return;
        for (int i = 0; i < arr.length; i++) arr[i] *= factor;
    }

    /**
   * check if arrays are identical
   * @param arr1
   * @param arr2
   */
    public static boolean equals(double[] arr1, double[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++) if (arr1[i] != arr2[i]) return false;
        return true;
    }

    /**
   * check if arrays have at most epsilon difference at each index
   * @param arr1
   * @param arr2
   * @param epsilon
   */
    public static boolean equals(double[] arr1, double[] arr2, double epsilon) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) return false;
        return (maxDist(arr1, arr2) <= epsilon);
    }

    public static double maxDist(double[] arr1, double[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) throw new IllegalArgumentException("Arrays1 and 2 are not of same length!");
        if (arr1.length == 0) return 0;
        double maxDist = Math.abs(arr1[0] - arr2[0]);
        for (int i = 1; i < arr1.length; i++) {
            double diff = Math.abs(arr1[i] - arr2[i]);
            if (diff > maxDist) maxDist = diff;
        }
        return maxDist;
    }

    public static void setSeed(long seed) {
        VectorMath.seed = seed;
        pickRandom.setSeed(seed);
    }

    public static long getSeed() {
        return seed;
    }

    public static <T> void randomize(T[] array) {
        randomize(array, array, array.length);
    }

    public static <T> void randomize(T[] array, int numItems) {
        randomize(array, array, numItems);
    }

    public static <T> void randomize(T[] src, T[] dst) {
        randomize(src, dst, src.length);
    }

    public static <T> void randomize(T[] src, T[] dst, int numItems) {
        if (src == null) return;
        if (numItems > dst.length || numItems > src.length) numItems = Math.min(dst.length, src.length);
        if (src != dst) System.arraycopy(src, 0, dst, 0, numItems);
        for (int i = 0; i < numItems; i++) {
            int j = pickRandom.nextInt(numItems);
            final T s = dst[i];
            dst[i] = dst[j];
            dst[j] = s;
        }
    }

    public static void randomize(int[] array) {
        randomize(array, array, array.length);
    }

    public static void randomize(int[] array, int numItems) {
        randomize(array, array, numItems);
    }

    public static void randomize(int[] src, int[] dst) {
        randomize(src, dst, src.length);
    }

    public static void randomize(int[] src, int[] dst, int numItems) {
        if (src == null) return;
        if (numItems > dst.length || numItems > src.length) numItems = Math.min(dst.length, src.length);
        if (src != dst) System.arraycopy(src, 0, dst, 0, numItems);
        for (int i = 0; i < numItems; i++) {
            int j = pickRandom.nextInt(numItems);
            final int s = dst[i];
            dst[i] = dst[j];
            dst[j] = s;
        }
    }

    public static void randomize(double[] array) {
        randomize(array, array, array.length);
    }

    public static void randomize(double[] array, int numItems) {
        randomize(array, array, numItems);
    }

    public static void randomize(double[] src, double[] dst) {
        randomize(src, dst, src.length);
    }

    public static void randomize(double[] src, double[] dst, int numItems) {
        if (src == null) return;
        if (numItems > dst.length || numItems > src.length) numItems = Math.min(dst.length, src.length);
        if (src != dst) System.arraycopy(src, 0, dst, 0, numItems);
        for (int i = 0; i < numItems; i++) {
            int j = pickRandom.nextInt(numItems);
            final double s = dst[i];
            dst[i] = dst[j];
            dst[j] = s;
        }
    }

    public static int sampleIdx(double[] vals) {
        if (vals == null) return -1;
        double v = sum(vals) * pickRandom.nextDouble();
        double s = 0;
        for (int i = 0; i < vals.length; i++) {
            s += vals[i];
            if (s >= v) return i;
        }
        return vals.length - 1;
    }

    public static int getMaxIdx(double[] vals) {
        if (vals == null) return -1;
        double m = vals[0];
        int mIdx = 0;
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] < m) continue;
            if (vals[i] > m || pickRandom.nextDouble() > 0.5) {
                m = vals[i];
                mIdx = i;
            }
        }
        return mIdx;
    }

    public static int getMinIdx(double[] vals) {
        if (vals == null) return -1;
        double m = vals[0];
        int mIdx = 0;
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] > m) continue;
            if (vals[i] < m || pickRandom.nextDouble() > 0.5) {
                m = vals[i];
                mIdx = i;
            }
        }
        return mIdx;
    }

    public static double getMaxValue(double[] vals) {
        if (vals == null) return -1;
        double max = vals[0];
        for (double v : vals) if (v > max) max = v;
        return max;
    }

    public static double getMinValue(double[] vals) {
        if (vals == null) return -1;
        double min = vals[0];
        for (double v : vals) if (v < min) min = v;
        return min;
    }

    public static int getMaxIdx(int[] vals) {
        if (vals == null) return -1;
        double m = vals[0];
        int mIdx = 0;
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] < m) continue;
            if (vals[i] > m || pickRandom.nextDouble() > 0.5) {
                m = vals[i];
                mIdx = i;
            }
        }
        return mIdx;
    }

    public static int getMinIdx(int[] vals) {
        if (vals == null) return -1;
        double m = vals[0];
        int mIdx = 0;
        for (int i = 1; i < vals.length; i++) {
            if (vals[i] > m) continue;
            if (vals[i] < m || pickRandom.nextDouble() > 0.5) {
                m = vals[i];
                mIdx = i;
            }
        }
        return mIdx;
    }

    public static int getMaxValue(int[] vals) {
        if (vals == null) return -1;
        int max = vals[0];
        for (int v : vals) if (v > max) max = v;
        return max;
    }

    public static int getMinValue(int[] vals) {
        if (vals == null) return -1;
        int min = vals[0];
        for (int v : vals) if (v < min) min = v;
        return min;
    }
}
