package com.l2jserver.util;

/**$
 *
 * @author Balancer
 *
 */
public class Rnd {

    public static final double get() {
        return MTRandom.getInstance().getSecureRandom().nextDouble();
    }

    /**
	 * Gets a random number from 0(inclusive) to n(exclusive)
	 *
	 * @param n The superior limit (exclusive)
	 * @return A number from 0 to n-1
	 */
    public static final int get(int n) {
        return (int) (MTRandom.getInstance().getSecureRandom().nextDouble() * n);
    }

    public static final int get(int min, int max) {
        return min + (int) Math.floor(MTRandom.getInstance().getSecureRandom().nextDouble() * (max - min + 1));
    }

    public static final int nextInt(int n) {
        return (int) Math.floor(MTRandom.getInstance().getSecureRandom().nextDouble() * n);
    }

    public static final int nextInt() {
        return MTRandom.getInstance().getSecureRandom().nextInt();
    }

    public static final double nextDouble() {
        return MTRandom.getInstance().getSecureRandom().nextDouble();
    }

    public static final double nextGaussian() {
        return MTRandom.getInstance().getSecureRandom().nextGaussian();
    }

    public static final boolean nextBoolean() {
        return MTRandom.getInstance().getSecureRandom().nextBoolean();
    }

    public static final void nextBytes(final byte[] array) {
        MTRandom.getInstance().getSecureRandom().nextBytes(array);
    }
}
