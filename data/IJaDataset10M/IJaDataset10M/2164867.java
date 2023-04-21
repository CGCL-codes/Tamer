package it.diamonds.tests.engine;

import it.diamonds.engine.RandomGenerator;
import it.diamonds.engine.RandomGeneratorInterface;
import junit.framework.TestCase;

public class TestRandomGenerator extends TestCase {

    private RandomGenerator generator1;

    private RandomGenerator generator2;

    private long commonSeed;

    public void setUp() {
        commonSeed = System.nanoTime();
        generator1 = new RandomGenerator(commonSeed);
        generator2 = new RandomGenerator(commonSeed);
    }

    public void testFirstRandomNumberWhitConstructor() {
        assertTrue("First random number should be the same", generator1.extract(100000) == generator2.extract(100000));
    }

    private void extractTenHundredNumbers(RandomGeneratorInterface one, RandomGeneratorInterface two) {
        for (int i = 0; i < 1000; ++i) {
            one.extract(100000);
            two.extract(100000);
        }
    }

    public void testNextRandomAfterTenHundred() {
        extractTenHundredNumbers(generator1, generator2);
        for (int i = 0; i < 1000; ++i) {
            assertTrue("Next random number should be the same", generator1.extract(100000) == generator2.extract(100000));
        }
    }

    public void testGettingSeed() {
        assertEquals(commonSeed, generator1.getSeed());
    }

    public void testSettingSeedValue() {
        generator1.setSeed(commonSeed + 1000);
        assertEquals(commonSeed + 1000, generator1.getSeed());
    }

    public void testSequenceAfterSeedWasSet() {
        generator1.setSeed(commonSeed + 1000);
        generator2 = new RandomGenerator(generator1.getSeed());
        assertTrue("First random number should be the same", generator1.extract(100000) == generator2.extract(100000));
        extractTenHundredNumbers(generator1, generator2);
        for (int i = 0; i < 1000; ++i) {
            assertTrue("Next random number should be the same", generator1.extract(100000) == generator2.extract(100000));
        }
    }

    public void testClone() {
        generator2 = (RandomGenerator) generator1.clone();
        assertTrue("First random number should be the same", generator1.extract(100000) == generator2.extract(100000));
        extractTenHundredNumbers(generator1, generator2);
        for (int i = 0; i < 1000; ++i) {
            assertTrue("Next random number should be the same", generator1.extract(100000) == generator2.extract(100000));
        }
    }
}
