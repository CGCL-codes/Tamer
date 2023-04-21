package org.matsim.contrib.freight.vrp.basics;

import java.util.Random;

public class RandomNumberGeneration {

    private static long DEFAULT_SEED = 4711;

    private static Random random = new Random(DEFAULT_SEED);

    public static Random getRandom() {
        return random;
    }

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    public static void reset() {
        random.setSeed(DEFAULT_SEED);
    }
}
