package org.statcato.statistics.inferential;

import org.statcato.statistics.*;
import org.statcato.utils.HelperFunctions;

/**
 * A hypothesis test for 1 population variance
 * 
 * @author Margaret Yau
 * @version %I%, %G%
 * @since 1.0
 */
public class HypothesisTest1Var {

    int n;

    double sigma2;

    double confidenceLevel;

    int testType;

    /**
     * Constructor.
     * 
     * @param n sample size
     * @param sigma2 hypothesized variance
     * @param confidenceLevel   confidence level
     * @param testType (as defined in {@link HypothesisTest})
     */
    public HypothesisTest1Var(int n, double sigma2, double confidenceLevel, int testType) {
        this.n = n;
        this.sigma2 = sigma2;
        this.confidenceLevel = confidenceLevel;
        this.testType = testType;
    }

    /**
     * Returns the test statistics for the given sample variance s2.
     * <p>
     * X2 = (n-1)^2 * s2 / sigma2
     *  
     * @param s2 hypothesized population variance
     * @return test statistic X2 (chi-square)
     */
    public double testStatistics(double s2) {
        return (n - 1) * s2 / sigma2;
    }

    /**
     * Returns the critical value corresponding to the given confidence
     * level and type of test.  Uses the chi-square distribution.
     * 
     * @return critical value
     * @see HypothesisTest
     */
    public String criticalValue() {
        double alpha = 1 - confidenceLevel;
        ChiSquareProbabilityDistribution dist = new ChiSquareProbabilityDistribution(n - 1);
        if (testType == HypothesisTest.RIGHT_TAIL) return HelperFunctions.formatFloat(dist.inverseCumulativeProbability(1 - alpha), 3); else if (testType == HypothesisTest.LEFT_TAIL) return HelperFunctions.formatFloat(dist.inverseCumulativeProbability(alpha), 3); else return HelperFunctions.formatFloat(dist.inverseCumulativeProbability(alpha / 2), 3) + ", " + HelperFunctions.formatFloat(dist.inverseCumulativeProbability(1 - alpha / 2), 3);
    }

    /**
     * Returns the p-value corresponding to s2 in a chi-square distribution.
     * 
     * @param s2 variance
     * @return p-value
     */
    public double pValue(double s2) {
        double ts = testStatistics(s2);
        ChiSquareProbabilityDistribution dist = new ChiSquareProbabilityDistribution(n - 1);
        if (testType == HypothesisTest.LEFT_TAIL) return dist.cumulativeProbability(ts); else if (testType == HypothesisTest.RIGHT_TAIL) return 1 - dist.cumulativeProbability(ts); else {
            double area = dist.cumulativeProbability(ts);
            if (area > 0.5) area = 1 - area;
            return 2 * area;
        }
    }
}
