package org.apache.commons.math.distribution;

/**
 * The Binomial Distribution.
 *
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/BinomialDistribution.html">
 * Binomial Distribution</a></li>
 * </ul>
 * </p>
 *
 * @version $Revision: 670469 $ $Date: 2008-06-23 10:01:38 +0200 (Mo, 23 Jun 2008) $
 */
public interface BinomialDistribution extends IntegerDistribution {

    /**
     * Access the number of trials for this distribution.
     * @return the number of trials.
     */
    int getNumberOfTrials();

    /**
     * Access the probability of success for this distribution.
     * @return the probability of success.
     */
    double getProbabilityOfSuccess();

    /**
     * Change the number of trials for this distribution.
     * @param trials the new number of trials.
     */
    void setNumberOfTrials(int trials);

    /**
     * Change the probability of success for this distribution.
     * @param p the new probability of success.
     */
    void setProbabilityOfSuccess(double p);
}
