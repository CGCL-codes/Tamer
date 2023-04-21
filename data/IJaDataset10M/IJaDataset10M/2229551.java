package org.statcato.statistics.inferential.nonparametrics;

import org.statcato.utils.HelperFunctions;
import org.statcato.utils.OrderingFunctions;
import org.statcato.statistics.NormalProbabilityDistribution;
import org.statcato.statistics.inferential.HypothesisTest;
import java.util.Vector;
import java.util.Iterator;

/**
 * Wilcoxon Rank Sum test.  A nonparametic test that uses ranks of
 * samples from two independent populations to test a claim about the medians 
 * of two populations.
 *
 * The two independent samples are combined into one sample, are sorted in
 * ascending order and are ranked based on its place in the one sample.
 * The sum of the ranks corresponding to one of the two samples are computed
 * and is used as test statistics.
 *
 * @author Margaret Yau
 * @version %I%, %G%
 * @since 1.0
 */
public class WilcoxonRankSumTest {

    /**
     * Sample size of the two samples
     */
    private int n1, n2;

    /**
     * Rank sum of sample 1
     */
    private double rankSum1;

    /**
     * Rank sum of sample 2
     */
    private double rankSum2;

    /**
     * The type of test
     */
    private int testType;

    /**
     * Significance of the test.
     */
    public double significance;

    /**
     * Constructor, given two samples of data values.
     *
     * @param data1 vector of double values (sample 1)
     * @param data2 vector of double values (sample 2)
     * @param testMedian hypothesized median
     * @param testType type of alternative hypothesis
     */
    public WilcoxonRankSumTest(Vector<Double> data1, Vector<Double> data2, int testType, double significance) {
        this.testType = testType;
        this.significance = significance;
        n1 = data1.size();
        n2 = data2.size();
        this.rankSum1 = getRankSum(data1, data2);
        this.rankSum2 = (n1 + n2) * (n1 + n2 + 1) / 2 - rankSum1;
    }

    /**
     * Returns the test statistic z = (R - mu) / sigma, where R is the
     * rank sum of the first same, mu the mean of the distribution of rank sum,
     * and sigma the standard deviatioin of the distribution of rank sum.
     *
     * @return test statistic z
     */
    public double testStatistic_R() {
        if (testType == HypothesisTest.LEFT_TAIL) return (rankSum1 - mu_R() + 0.5) / sigma_R(); else return (rankSum1 - mu_R() - 0.5) / sigma_R();
    }

    /**
     * Returns the mean of the distribution of rank sum,
     * mu = n1 * (n1 + n2 + 1) / 2.
     *
     * @return mean
     */
    public double mu_R() {
        return n1 * (n1 + n2 + 1) / 2.0;
    }

    /**
     * Returns the standard deviation of the distribution of rank sum,
     * sigma = (n1 * n2 * (n1 + n2 + 1) / 12)^0.5
     *
     * @return standard deviation
     */
    public double sigma_R() {
        return Math.sqrt(n1 * n2 * (n1 + n2 + 1) / 12);
    }

    /**
     * Returns the p-value.  
     *
     * @return p-value
     */
    public double pValue_R() {
        NormalProbabilityDistribution dist = new NormalProbabilityDistribution(0, 1);
        double p = dist.cumulativeProbability(testStatistic_R());
        if (testType == HypothesisTest.LEFT_TAIL) return p; else if (testType == HypothesisTest.RIGHT_TAIL) return 1 - p; else return 2 * (p <= 0.5 ? p : 1 - p);
    }

    public double criticalValue_R() {
        NormalProbabilityDistribution dist = new NormalProbabilityDistribution(0, 1);
        if (testType == HypothesisTest.RIGHT_TAIL) return dist.inverseCumulativeProbability(1 - significance); else if (testType == HypothesisTest.LEFT_TAIL) return dist.inverseCumulativeProbability(significance); else return Math.abs(dist.inverseCumulativeProbability(significance / 2));
    }

    @Override
    public String toString() {
        String s = "";
        s += "Wilcoxon Rank Sum Test: <br>";
        s += "<table border='1'>";
        s += "<tr>";
        s += "<th>n<sub>1</sub></th><th>n<sub>2</sub></th>" + "<th>R<sub>1</sub></th><th>R<sub>2</sub></th>";
        s += "<th>&mu;<sub>R</sub></th><th>&sigma;<sub>R</sub></th>";
        s += "<th>Test Statistics z<sub>R</sub></th><th>Critical Value</th><th>p-Value</th>";
        s += "</tr>";
        s += "<td>" + n1 + "</td>";
        s += "<td>" + n2 + "</td>";
        s += "<td>" + rankSum1 + "</td>";
        s += "<td>" + rankSum2 + "</td>";
        s += "<td>" + HelperFunctions.formatFloat(mu_R(), 3) + "</td>";
        s += "<td>" + HelperFunctions.formatFloat(sigma_R(), 3) + "</td>";
        s += "<td>" + HelperFunctions.formatFloat(testStatistic_R(), 3) + "</td>";
        s += "<td>" + (testType == HypothesisTest.TWO_TAIL ? "&plusmn;" : "") + HelperFunctions.formatFloat(criticalValue_R(), 3) + "</td>";
        s += "<td>" + HelperFunctions.formatFloat(pValue_R(), 5) + "</td>";
        s += "</tr>";
        s += "</table><br>";
        return s;
    }

    /**
     * Returns the rank sum, which is the sum of ranks for the first sample.
     * Ranks are computed after the two samples are combined and sorted in
     * ascending order.
     * 
     * @param data vector of Double values
     * @return rank sum
     */
    public double getRankSum(Vector<Double> data1, Vector<Double> data2) {
        Vector<Double> dataVector = new Vector<Double>();
        Iterator it = data1.iterator();
        while (it.hasNext()) {
            Double next = (Double) it.next();
            if (next != null) {
                dataVector.add(next);
            }
        }
        it = data2.iterator();
        while (it.hasNext()) {
            Double next = (Double) it.next();
            if (next != null) {
                dataVector.add(next);
            }
        }
        double[] ranks = OrderingFunctions.rankDoubleVector(dataVector);
        double rankSum = 0;
        for (int i = 0; i < data1.size(); ++i) {
            Double next = data1.elementAt(i);
            if (next != null) {
                rankSum += ranks[dataVector.indexOf(next)];
            }
        }
        return rankSum;
    }
}
