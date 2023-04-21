package org.apache.commons.math.distribution;

/**
 * Base interface for discrete distributions.
 *
 * @version $Revision: 670469 $ $Date: 2008-06-23 10:01:38 +0200 (Mo, 23 Jun 2008) $
 */
public interface DiscreteDistribution extends Distribution {

    /**
     * For a random variable X whose values are distributed according
     * to this distribution, this method returns P(X = x). In other words, this
     * method represents the probability mass function, or PMF for the distribution.
     * 
     * @param x the value at which the probability mass function is evaluated.
     * @return the value of the probability mass function at x
     */
    double probability(double x);
}
