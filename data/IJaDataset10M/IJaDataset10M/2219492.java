package org.waveprotocol.wave.client.common.util;

/**
 * General maths utility functions that don't have anywhere else to live.
 *
 */
public final class MathUtil {

    private MathUtil() {
    }

    /**
   * Rounds a double to an int.
   *
   * @param x value to round
   * @return closest int value to x.
   */
    public static int roundToInt(double x) {
        return (int) Math.floor(x + 0.5);
    }

    /**
   * Clips a value within a range.
   *
   * @param lower  lower bound
   * @param upper  upper bound
   * @param x value to clip
   * @return clipped value of {@code x}
   */
    public static int clip(int lower, int upper, int x) {
        return Math.min(Math.max(x, lower), upper);
    }

    /**
   * Clips a value within a range.
   *
   * @param lower  lower bound
   * @param upper  upper bound
   * @param x value to clip
   * @return clipped value of {@code x}
   */
    public static double clip(double lower, double upper, double x) {
        return Math.min(Math.max(x, lower), upper);
    }
}
