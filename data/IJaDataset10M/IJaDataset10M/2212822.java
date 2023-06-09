package net.maizegenetics.pal.statistics;

/**
 * Likelihood ratio test based on chi-square statistics
 *
 * @version $Id: LikelihoodRatioTest.java,v 1.1 2007/01/12 03:26:16 tcasstevens Exp $ 
 *
 * @author Korbinian Strimmer
 */
public class LikelihoodRatioTest {

    /**
	 * compute significance level for the differences
	 * in log-likelihood (based on chi-square distribution)
	 *
	 * @param deltaL  difference of Log Likelihood values (>=0)
	 * @param df      degrees of freedom
	 *
	 * @return        significance level
	 */
    public static double getSignificance(double deltaL, int df) {
        return 1.0 - ChiSquareDistribution.cdf(2.0 * deltaL, df);
    }
}
