package org.openscience.cdk.tools;

import java.util.Random;

/**
 * The <code>RNG</code> class supplies useful methods to generate random
 * numbers.
 * <p>
 * This class isn't supposed to be instantiated. You should use it by calling
 * its static methods.
 */
public class RandomNumbersTool extends Random {

    private static java.util.Random random;

    private static long randomSeed;

    static {
        randomSeed = System.currentTimeMillis();
        random = new java.util.Random(randomSeed);
    }

    /**
     * Sets the base generator to be used by this class.
     * <p>
     * @param base_random a <code>java.util.Random</code> subclass.
     */
    public static void setRandom(java.util.Random base_random) {
        random = base_random;
    }

    /**
     * Sets the seed of this random number generator using a single
     * <code>long</code> seed.
     *
     * @param new_seed  the seed to be used by the random number generator.
     */
    public static void setRandomSeed(long new_seed) {
        randomSeed = new_seed;
        random.setSeed(randomSeed);
    }

    /**
     * Returns the seed being used by this random number generator.
     * <p>
     * @return the <code>long</code> seed.
     */
    public static long getRandomSeed() {
        return randomSeed;
    }

    /**
     * Generates a random integer between <code>0</code> and <code>1</code>.
     * <p>
     * @return a random integer between <code>0</code> and <code>1</code>.
     */
    public static int randomInt() {
        return randomInt(0, 1);
    }

    /**
     * Generates a random integer between the specified values.
     * <p>
     * @param lo the lower bound for the generated integer.
     * @param hi the upper bound for the generated integer.
     * @return a random integer between <code>lo</code> and <code>hi</code>.
     */
    public static int randomInt(int lo, int hi) {
        return (Math.abs(random.nextInt()) % (hi - lo + 1)) + lo;
    }

    /**
     * Generates a random long between <code>0</code> and <code>1</code>.
     * <p>
     * @return a random long between <code>0</code> and <code>1</code>.
     */
    public static long randomLong() {
        return randomLong(0, 1);
    }

    /**
     * Generates a random long between the specified values.
     * <p>
     * @param lo the lower bound for the generated long.
     * @param hi the upper bound for the generated long.
     * @return a random long between <code>lo</code> and <code>hi</code>.
     */
    public static long randomLong(long lo, long hi) {
        return (Math.abs(random.nextLong()) % (hi - lo + 1)) + lo;
    }

    /**
     * Generates a random float between <code>0</code> and <code>1</code>.
     * <p>
     * @return a random float between <code>0</code> and <code>1</code>.
     */
    public static float randomFloat() {
        return random.nextFloat();
    }

    /**
     * Generates a random float between the specified values.
     * <p>
     * @param lo the lower bound for the generated float.
     * @param hi the upper bound for the generated float.
     * @return a random float between <code>lo</code> and <code>hi</code>.
     */
    public static float randomFloat(float lo, float hi) {
        return (hi - lo) * random.nextFloat() + lo;
    }

    /**
     * Generates a random double between <code>0</code> and <code>1</code>.
     * <p>
     * @return a random double between <code>0</code> and <code>1</code>.
     */
    public static double randomDouble() {
        return random.nextDouble();
    }

    /**
     * Generates a random double between the specified values.
     * <p>
     * @param lo the lower bound for the generated double.
     * @param hi the upper bound for the generated double.
     * @return a random double between <code>lo</code> and <code>hi</code>.
     */
    public static double randomDouble(double lo, double hi) {
        return (hi - lo) * random.nextDouble() + lo;
    }

    /**
     * Generates a random boolean.
     * <p>
     * @return a random boolean.
     */
    public static boolean randomBoolean() {
        return (randomInt() == 1);
    }

    /**
     * Generates a random bit: either <code>0</code> or <code>1</code>.
     * <p>
     * @return a random bit.
     */
    public static int randomBit() {
        return randomInt();
    }

    /**
     * Returns a boolean value based on a biased coin toss.
     * <p>
     * @param p the probability of success.
     * @return <code>true</code> if a success was found; <code>false</code>
     * otherwise.
     */
    public static boolean flipCoin(double p) {
        return (randomDouble() < p ? true : false);
    }

    /**
     * Generates a random float from a Gaussian distribution with the specified
     * deviation.
     * <p>
     * @param dev the desired deviation.
     * @return a random float from a Gaussian distribution with deviation
     * <code>dev</code>.
     */
    public static float gaussianFloat(float dev) {
        return (float) random.nextGaussian() * dev;
    }

    /**
     * Generates a random double from a Gaussian distribution with the specified
     * deviation.
     * <p>
     * @param dev the desired deviation.
     * @return a random double from a Gaussian distribution with deviation
     * <code>dev</code>.
     */
    public static double gaussianDouble(double dev) {
        return random.nextGaussian() * dev;
    }

    /**
     * Generates a random float from an Exponential distribution with the specified
     * mean value.
     * <p>
     * @param mean the desired mean value.
     * @return a random float from an Exponential distribution with mean value
     * <code>mean</code>.
     */
    public static float exponentialFloat(float mean) {
        return (float) (-mean * Math.log(randomDouble()));
    }

    /**
     * Generates a random double from an Exponential distribution with the specified
     * mean value.
     * <p>
     * @param mean the desired mean value.
     * @return a random double from an Exponential distribution with mean value
     * <code>mean</code>.
     */
    public static double exponentialDouble(double mean) {
        return -mean * Math.log(randomDouble());
    }
}
