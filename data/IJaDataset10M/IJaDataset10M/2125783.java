package common;

import java.util.Random;

/**
 * This class performs operations with random numbers.
 */
public class RND {

    /**
     * The seed of random numbers.
     */
    private static Random seed;

    private static final long RND_SEED = 1111L;

    private static volatile long seedUniquifier = 8682522807148012L;

    /**
     * Initializes the random number generator. The seed is generated from time.
     * @return Time generated seed - usable in <i>initialize(int oseed)</i>.
     */
    public static long initializeTime() {
        long timeSeed = ++seedUniquifier + System.nanoTime();
        seed = new Random(timeSeed);
        return timeSeed;
    }

    /**
     * Initializes the random number generator with a constant.
     */
    public static void initialize() {
        seed = new Random(RND_SEED);
    }

    /**
     * Initializes the random number generator.
     * @param oseed Seed.
     */
    public static void initialize(long oseed) {
        seed = new Random(oseed);
    }

    /**
     * Returns a random boolean.
     *
     * @return random boolean
     */
    public static boolean getBoolean() {
        return seed.nextBoolean();
    }

    /**
     * Returns a random double from the given range.
     *
     * @param omin the lower bound
     * @param omax the higher bound
     * @return double from <i>&lt;omin; omax&gt;</i>
     */
    public static double getDouble(double omin, double omax) {
        return (omax - omin) * seed.nextDouble() + omin;
    }

    /**
     * Returns a random double.
     *
     * @return double from <i>&lt;0; 1&gt;</i>
     */
    public static double getDouble() {
        return seed.nextDouble();
    }

    /**
     * Returns a random integer from given range.
     * @param omin the lower bound
     * @param omax the higher bound
     * @return the int from <i>&lt;omin; omax&gt;</i>
     */
    public static int getInt(int omin, int omax) {
        return seed.nextInt(omax - omin + 1) + omin;
    }
}
