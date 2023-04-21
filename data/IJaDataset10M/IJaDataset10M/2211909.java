package co.edu.unal.ungrid.util;

import java.util.Random;

public class RandomHelper {

    private RandomHelper() {
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static int[] randInt(int n1) {
        return randInt(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static int[][] randInt(int n1, int n2) {
        return randInt(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static int[][][] randInt(int n1, int n2, int n3) {
        return randInt(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static int[] randInt(final Random random, int n1) {
        int[] rx = new int[n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static int[][] randInt(final Random random, int n1, int n2) {
        int[][] rx = new int[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static int[][][] randInt(final Random random, int n1, int n2, int n3) {
        int[][][] rx = new int[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(int[] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(int[][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(int[][][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, int[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextInt();
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, int[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, int[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static long[] randLong(int n1) {
        return randLong(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static long[][] randLong(int n1, int n2) {
        return randLong(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static long[][][] randLong(int n1, int n2, int n3) {
        return randLong(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static long[] randLong(final Random random, int n1) {
        long[] rx = new long[n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static long[][] randLong(final Random random, int n1, int n2) {
        long[][] rx = new long[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static long[][][] randLong(final Random random, int n1, int n2, int n3) {
        long[][][] rx = new long[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(long[] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(long[][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(long[][][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, long[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextLong();
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, long[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, long[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static float[] randFloat(int n1) {
        return randFloat(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static float[][] randFloat(int n1, int n2) {
        return randFloat(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static float[][][] randFloat(int n1, int n2, int n3) {
        return randFloat(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static float[] randFloat(final Random random, int n1) {
        float[] rx = new float[n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static float[][] randFloat(final Random random, int n1, int n2) {
        float[][] rx = new float[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static float[][][] randFloat(final Random random, int n1, int n2, int n3) {
        float[][][] rx = new float[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(float[] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(float[][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(float[][][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, float[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextFloat();
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, float[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, float[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static float[] complexRandFloat(int n1) {
        return complexRandFloat(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static float[][] complexRandFloat(int n1, int n2) {
        return complexRandFloat(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static float[][][] complexRandFloat(int n1, int n2, int n3) {
        return complexRandFloat(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static float[] complexRandFloat(final Random random, int n1) {
        float[] cx = new float[2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static float[][] complexRandFloat(final Random random, int n1, int n2) {
        float[][] cx = new float[n2][2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static float[][][] complexRandFloat(final Random random, int n1, int n2, int n3) {
        float[][][] cx = new float[n3][n2][2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(float[] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(float[][] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(float[][][] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, float[] cx) {
        rand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, float[][] cx) {
        rand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, float[][][] cx) {
        rand(random, cx);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static double[] randDouble(int n1) {
        return randDouble(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static double[][] randDouble(int n1, int n2) {
        return randDouble(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static double[][][] randDouble(int n1, int n2, int n3) {
        return randDouble(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static double[] randDouble(final Random random, int n1) {
        double[] rx = new double[n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static double[][] randDouble(final Random random, int n1, int n2) {
        double[][] rx = new double[n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static double[][][] randDouble(final Random random, int n1, int n2, int n3) {
        double[][][] rx = new double[n3][n2][n1];
        rand(random, rx);
        return rx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(double[] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(double[][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param rx the array.
	 */
    public static void rand(double[][][] rx) {
        rand(random, rx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, double[] rx) {
        int n1 = rx.length;
        for (int i1 = 0; i1 < n1; ++i1) {
            rx[i1] = random.nextDouble();
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, double[][] rx) {
        int n2 = rx.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            rand(random, rx[i2]);
        }
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param rx the array.
	 */
    public static void rand(final Random random, double[][][] rx) {
        int n3 = rx.length;
        for (int i3 = 0; i3 < n3; ++i3) {
            rand(random, rx[i3]);
        }
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 */
    public static double[] complexRandDouble(int n1) {
        return complexRandDouble(random, n1);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static double[][] complexRandDouble(int n1, int n2) {
        return complexRandDouble(random, n1, n2);
    }

    /**
	 * Returns a new array of random values.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static double[][][] complexRandDouble(int n1, int n2, int n3) {
        return complexRandDouble(random, n1, n2, n3);
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 */
    public static double[] complexRandDouble(final Random random, int n1) {
        double[] cx = new double[2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 */
    public static double[][] complexRandDouble(final Random random, int n1, int n2) {
        double[][] cx = new double[n2][2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Returns a new array of random values.
	 * @param random random number generator.
	 * @param n1 1st array dimension.
	 * @param n2 2nd array dimension.
	 * @param n3 3rd array dimension.
	 */
    public static double[][][] complexRandDouble(final Random random, int n1, int n2, int n3) {
        double[][][] cx = new double[n3][n2][2 * n1];
        complexRand(random, cx);
        return cx;
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(double[] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(double[][] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param cx the array.
	 */
    public static void complexRand(double[][][] cx) {
        complexRand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, double[] cx) {
        rand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, double[][] cx) {
        rand(random, cx);
    }

    /**
	 * Fills the specified array with random values.
	 * @param random random number generator.
	 * @param cx the array.
	 */
    public static void complexRand(final Random random, double[][][] cx) {
        rand(random, cx);
    }

    public static double getRandInterpol(double fMin, double fMax) {
        double r = Math.random();
        double c = 1.0 - r;
        return ((fMin * c) + (fMax * r));
    }

    public static int getBernoulli(double p) {
        double r = Math.random();
        return (r <= p ? 1 : 0);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(getRandInterpol(-200, +200));
        }
        System.out.println("----------------------------------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(getRandInterpol(0.9, 1.1));
        }
    }

    private static final Random random = new Random();
}
