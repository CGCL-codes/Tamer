package org.apache.commons.math3.stat.descriptive;

/**
 *  Reporting interface for basic univariate statistics.
 *
  * @version $Id: StatisticalSummary.java 1244107 2012-02-14 16:17:55Z erans $
 */
public interface StatisticalSummary {

    /**
     * Returns the <a href="http://www.xycoon.com/arithmetic_mean.htm">
     * arithmetic mean </a> of the available values
     * @return The mean or Double.NaN if no values have been added.
     */
    double getMean();

    /**
     * Returns the variance of the available values.
     * @return The variance, Double.NaN if no values have been added
     * or 0.0 for a single value set.
     */
    double getVariance();

    /**
     * Returns the standard deviation of the available values.
     * @return The standard deviation, Double.NaN if no values have been added
     * or 0.0 for a single value set.
     */
    double getStandardDeviation();

    /**
     * Returns the maximum of the available values
     * @return The max or Double.NaN if no values have been added.
     */
    double getMax();

    /**
    * Returns the minimum of the available values
    * @return The min or Double.NaN if no values have been added.
    */
    double getMin();

    /**
     * Returns the number of available values
     * @return The number of available values
     */
    long getN();

    /**
     * Returns the sum of the values that have been added to Univariate.
     * @return The sum or Double.NaN if no values have been added
     */
    double getSum();
}
