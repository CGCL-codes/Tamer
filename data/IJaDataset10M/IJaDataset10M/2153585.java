package org.apache.commons.math.distribution;

import org.apache.commons.math.MathException;

/**
 * Interface representing the Poisson Distribution.
 * 
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/PoissonDistribution.html">
 * Poisson distribution</a></li>
 * </ul>
 * </p>
 * 
 * @version $Revision: 670469 $ $Date: 2008-06-23 10:01:38 +0200 (Mo, 23 Jun 2008) $
 */
public interface PoissonDistribution extends IntegerDistribution {

    /**
     * Get the mean for the distribution.
     * 
     * @return the mean for the distribution.
     */
    public double getMean();

    /**
     * Set the mean for the distribution.
     * The parameter value must be positive; otherwise an 
     * <code>IllegalArgument</code> is thrown.
     * 
     * @param p the mean
     * @throws IllegalArgumentException if p &le; 0
     */
    public void setMean(double p);

    /**
     * Calculates the Poisson distribution function using a normal approximation.
     * 
     * @param x the upper bound, inclusive
     * @return the distribution function value calculated using a normal approximation
     * @throws MathException if an error occurs computing the normal approximation
     */
    public double normalApproximateProbability(int x) throws MathException;
}
