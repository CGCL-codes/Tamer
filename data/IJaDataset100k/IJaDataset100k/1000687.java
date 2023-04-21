package miko.math;

import java.util.Random;

public class StaticRandom {

    private static Random randomNumberGenerator;

    static {
        randomNumberGenerator = new Random();
    }

    private StaticRandom() {
    }

    public static boolean nextBoolean() {
        return randomNumberGenerator.nextBoolean();
    }

    public static void nextBytes(byte[] bytes) {
        randomNumberGenerator.nextBytes(bytes);
    }

    public static double nextDouble() {
        return randomNumberGenerator.nextDouble();
    }

    public static float nextFloat() {
        return randomNumberGenerator.nextFloat();
    }

    public static synchronized double nextGaussian() {
        return randomNumberGenerator.nextGaussian();
    }

    public static int nextInt() {
        return randomNumberGenerator.nextInt();
    }

    public static int nextInt(int n) {
        return randomNumberGenerator.nextInt(n);
    }

    public static int nextInt(int l, int u) {
        if ((u - l) <= 0) return l;
        return l + randomNumberGenerator.nextInt(u - l);
    }

    public static long nextLong() {
        return randomNumberGenerator.nextLong();
    }

    public static synchronized void setSeed(long seed) {
        randomNumberGenerator.setSeed(seed);
    }
}
