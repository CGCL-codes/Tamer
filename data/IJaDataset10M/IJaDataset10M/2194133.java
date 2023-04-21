package org.expasy.jpl.utils.math.combinatorial;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class CombinationsTest {

    static Log logger = LogFactory.getLog(CombinationsTest.class);

    Combinations combinations;

    @Test(expected = IllegalArgumentException.class)
    public void testBadCombinations1() {
        combinations = Combinations.generateBasic(-1, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadCombinations2() {
        combinations = Combinations.generateBasic(6, 5);
    }

    @Test
    public void testGenerate2x2Combinations() {
        combinations = Combinations.generate(2, 2);
        Assert.assertEquals(Combinations.computeNumber(2, 2), combinations.getNb());
    }

    @Test
    public void testGenerateAllCombinations() {
        combinations = Combinations.generateBasic(6, 20);
        Assert.assertEquals(Combinations.computeNumber(6, 20), combinations.getNb());
    }

    @Test
    public void testGenerateAllCombinationsFaster() {
        combinations = Combinations.generate(6, 20);
        Assert.assertEquals(Combinations.computeNumber(6, 20), combinations.getNb());
    }

    @Test
    public void testCombNbComputing() {
        logger.info("C(20, 30) = " + Combinations.computeNumber(20, 30));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadCombNbComputing() {
        Combinations.computeNumber(22, 30);
    }
}
