package org.jquantlib.math.randomnumbers;

public class SeedGenerator {

    /**
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
    private static volatile SeedGenerator instance;

    private MersenneTwisterUniformRng rng;

    private SeedGenerator() {
        if (System.getProperty("EXPERIMENTAL") == null) {
            throw new UnsupportedOperationException("Work in progress");
        }
        this.rng = new MersenneTwisterUniformRng(42);
        initialize();
    }

    private void initialize() {
        final long firstSeed = System.currentTimeMillis();
        final MersenneTwisterUniformRng first = new MersenneTwisterUniformRng(firstSeed);
        final long secondSeed = first.nextInt32();
        final MersenneTwisterUniformRng second = new MersenneTwisterUniformRng(secondSeed);
        final long skip = second.nextInt32() % 1000;
        final int init[] = new int[4];
        init[0] = (int) second.nextInt32();
        init[1] = (int) second.nextInt32();
        init[2] = (int) second.nextInt32();
        init[3] = (int) second.nextInt32();
        this.rng = new MersenneTwisterUniformRng(init);
        for (long i = 0; i < skip; i++) {
            rng.nextInt32();
        }
    }

    public static SeedGenerator getInstance() {
        if (instance == null) {
            synchronized (SeedGenerator.class) {
                if (instance == null) {
                    instance = new SeedGenerator();
                }
            }
        }
        return instance;
    }

    public long get() {
        return rng.nextInt32();
    }
}
