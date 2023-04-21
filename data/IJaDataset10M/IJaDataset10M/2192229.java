package org.josef.test.math;

import org.josef.math.CStatistics;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * JUnit test class for class {@link CStatistics}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 240 $
 */
public class CStatisticsTest {

    /**
     * Accuracy for methods that return a double value.
     */
    private static final double DELTA = 1.0E-10;

    /**
     * The statistical data.
     * <br>This data can't be changed without re-evaluating the outcome of all
     * the different tests!
     */
    private final double[] data = { 0.0, 1.0, 2.0, 2.0, 2.0, 1.0, 0.0 };

    /**
     * A population.
     */
    private CStatistics population;

    /**
     * A sample.
     */
    private CStatistics sample;

    /**
     * Initializes the statistical data.
     */
    @Before
    public final void init() {
        population = CStatistics.computeStatistics(false, data);
        sample = CStatistics.computeStatistics(true, data);
    }

    /**
     * Test method for {@link CStatistics#getMinimum()}.
     */
    @Test
    public final void getMinimum() {
        assertEquals(0.0, sample.getMinimum(), 0.0);
        assertEquals(0.0, population.getMinimum(), 0.0);
    }

    /**
     * Test method for {@link CStatistics#getMaximum()}.
     */
    @Test
    public final void getMaximum() {
        assertEquals(2.0, sample.getMaximum(), 0.0);
        assertEquals(2.0, population.getMaximum(), 0.0);
    }

    /**
     * Test method for {@link CStatistics#getSum()}.
     */
    @Test
    public final void getSum() {
        assertEquals(computeSum(), sample.getSum(), 0.0);
        assertEquals(computeSum(), population.getSum(), 0.0);
    }

    /**
     * Test method for {@link CStatistics#getAverage()}.
     */
    @Test
    public final void getAverage() {
        assertEquals(computeSum() / data.length, sample.getAverage(), 0.0);
        assertEquals(computeSum() / data.length, population.getAverage(), 0.0);
    }

    /**
     * Test method for {@link CStatistics#getMedian()}.
     */
    @Test
    public final void median() {
        assertEquals(1.0, sample.getMedian(), 0.0);
        assertEquals(1.0, population.getMedian(), 0.0);
        assertEquals(1.0, CStatistics.computeStatistics(true, 0, 2).getMedian(), 0.0);
    }

    /**
     * Test method for {@link CStatistics#getVariance()}.
     */
    @Test
    public final void getVariance() {
        final double sampleVariance = 0.80952380952380952380952380952333;
        final double populationVariance = 0.69387755102040816326530612244857;
        assertEquals(sampleVariance, sample.getVariance(), DELTA);
        assertEquals(populationVariance, population.getVariance(), DELTA);
    }

    /**
     * Test method for {@link CStatistics#getStandardDeviation()}.
     */
    @Test
    public final void getStandardDeviation() {
        final double sampleStandardDeviation = 0.89973541084243734671942228347739;
        final double populationStandardDeviation = 0.83299312783504292441059326822021;
        assertEquals(sampleStandardDeviation, sample.getStandardDeviation(), DELTA);
        assertEquals(populationStandardDeviation, population.getStandardDeviation(), DELTA);
    }

    /**
     * Test method for {@link CStatistics#getSkewness()}.
     */
    @Test
    public final void getSkewness() {
        final double sampleSkweness = -0.2723801058145726;
        final double populationSkewness = -0.3530449674378281;
        assertEquals(sampleSkweness, sample.getSkewness(), DELTA);
        assertEquals(populationSkewness, population.getSkewness(), DELTA);
        System.out.println(sample.getSkewness());
        System.out.println(population.getSkewness());
    }

    /**
     * Computes the sum of the statistical data.
     * @return  The sum of the statistical data.
     */
    private double computeSum() {
        double sum = 0.0;
        for (int i = 0; i < data.length; ++i) {
            sum += data[i];
        }
        return sum;
    }
}
