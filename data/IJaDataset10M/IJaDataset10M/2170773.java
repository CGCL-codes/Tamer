package statisticalCalculatorTests;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import statisticalCalculator.BinomialDistribution;

public class BinomialDistributionTests {

    private final double delta = 0.0001;

    BinomialDistribution binDist;

    @Before
    public void setUp() {
        binDist = new BinomialDistribution(2.0, 1.0, 0.1);
    }

    @After
    public void tearDown() {
        binDist = null;
    }

    @Test
    public void BinomialDistributionClassIsNotNull() {
        assertNotNull(binDist);
    }

    @Test
    public void canSetNumberOfEventsAndNumberOFsuccessAndProbabiltyPfSuccess() {
        assertNotNull(binDist);
    }

    @Test
    public void canSetNumberOfEvents() {
        assertEquals(2.0, binDist.getNumberOfEvents(), delta);
    }

    @Test
    public void canSetNumOfSuccesesss() {
        assertEquals(1.0, binDist.getNumberOfSuccess(), delta);
    }

    @Test
    public void canSetProbabilty() {
        assertEquals(0.1, binDist.getProbabilty(), delta);
    }

    @Test
    public void canCalculateFactorialOfANumber() {
        int expected = 24;
        assertEquals(expected, binDist.calculateFactorial(4));
    }

    @Test
    public void CanCalculateCombinatorialOfTwoNumbers() {
        int expeced = 6;
        assertEquals(expeced, binDist.calculateCombination(4, 2));
    }

    @Test
    public void canCalculateBinomialDistributionGivenTheParamers() {
        double expected = 0.18;
        assertEquals(expected, binDist.calculateBinomialDistribution(), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNumOfEventsIsLessThanZero() throws Exception {
        new BinomialDistribution(-1, 3, 0.2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNumOfSuccessesIsLessThanZero() throws Exception {
        new BinomialDistribution(2, -3, 0.2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenNumOfSuccessesIsGreaterThanNumberOfEvents() throws Exception {
        new BinomialDistribution(2, 3, 0.2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenProbabilityIsGreaterThanOne() throws Exception {
        new BinomialDistribution(2, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionIsThrownWhenProbabilityIsLessThanZero() throws Exception {
        new BinomialDistribution(2, 3, -1);
    }
}
