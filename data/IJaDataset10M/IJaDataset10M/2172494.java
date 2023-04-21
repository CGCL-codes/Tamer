package com.rapidminer.operator.validation.significance;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.performance.PerformanceCriterion;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.report.Readable;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.math.FDistribution;
import com.rapidminer.tools.math.SignificanceTestResult;

/**
 * Determines if the null hypothesis (all actual mean values are the same) holds
 * for the input performance vectors. This operator uses a simple (pairwise)
 * t-test to determine the probability that the null hypothesis is wrong. Since
 * a t-test can only be applied on two performance vectors this test will be
 * applied to all possible pairs. The result is a significance matrix. However,
 * pairwise t-test may introduce a larger type I error. It is recommended to
 * apply an additional ANOVA test to determine if the null hypothesis is wrong
 * at all.
 * 
 * @author Ingo Mierswa
 */
public class TTestSignificanceTestOperator extends SignificanceTestOperator {

    /** The result for a paired t-test. */
    public static class TTestSignificanceTestResult extends SignificanceTestResult implements Readable {

        private static final long serialVersionUID = -5412090499056975997L;

        private final PerformanceVector[] allVectors;

        private final double[][] probMatrix;

        private double alpha = 0.05d;

        public TTestSignificanceTestResult(PerformanceVector[] allVectors, double[][] probMatrix, double alpha) {
            this.allVectors = allVectors;
            this.probMatrix = probMatrix;
            this.alpha = alpha;
        }

        @Override
        public String getName() {
            return "Pairwise t-Test";
        }

        /** Returns NaN since no single probability will be delivered. */
        @Override
        public double getProbability() {
            return Double.NaN;
        }

        @Override
        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("Probabilities for random values with the same result:" + Tools.getLineSeparator());
            for (int i = 0; i < allVectors.length; i++) {
                for (int j = 0; j < allVectors.length; j++) {
                    if (!Double.isNaN(probMatrix[i][j])) result.append(Tools.formatNumber(probMatrix[i][j]) + "\t"); else result.append("-----\t");
                }
                result.append(Tools.getLineSeparator());
            }
            result.append("Values smaller than alpha=" + Tools.formatNumber(alpha) + " indicate a probably significant difference between the mean values!" + Tools.getLineSeparator());
            result.append("List of performance values:" + Tools.getLineSeparator());
            for (int i = 0; i < allVectors.length; i++) {
                result.append(i + ": " + Tools.formatNumber(allVectors[i].getMainCriterion().getAverage()) + " +/- " + Tools.formatNumber(Math.sqrt(allVectors[i].getMainCriterion().getVariance())) + Tools.getLineSeparator());
            }
            return result.toString();
        }

        @Override
        public boolean isInTargetEncoding() {
            return false;
        }

        public PerformanceVector[] getAllVectors() {
            return allVectors;
        }

        public double[][] getProbMatrix() {
            return this.probMatrix;
        }

        public double getAlpha() {
            return this.alpha;
        }
    }

    public TTestSignificanceTestOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public SignificanceTestResult performSignificanceTest(PerformanceVector[] allVectors, double alpha) {
        double[][] resultMatrix = new double[allVectors.length][allVectors.length];
        for (int i = 0; i < allVectors.length; i++) {
            for (int j = 0; j < (i + 1); j++) resultMatrix[i][j] = Double.NaN;
            for (int j = i + 1; j < allVectors.length; j++) {
                resultMatrix[i][j] = getProbability(allVectors[i].getMainCriterion(), allVectors[j].getMainCriterion());
            }
        }
        return new TTestSignificanceTestResult(allVectors, resultMatrix, alpha);
    }

    private double getProbability(PerformanceCriterion pc1, PerformanceCriterion pc2) {
        double totalDeviation = ((pc1.getAverageCount() - 1) * pc1.getVariance() + (pc2.getAverageCount() - 1) * pc2.getVariance()) / (pc1.getAverageCount() + pc2.getAverageCount() - 2);
        double factor = 1.0d / (1.0d / pc1.getAverageCount() + 1.0d / pc2.getAverageCount());
        double diff = pc1.getAverage() - pc2.getAverage();
        double t = factor * diff * diff / totalDeviation;
        FDistribution fDist = new FDistribution(1, pc1.getAverageCount() + pc2.getAverageCount() - 2);
        double prob = fDist.getProbabilityForValue(t);
        prob = prob < 0 ? 1.0d : 1.0d - prob;
        return prob;
    }

    @Override
    public int getMinSize() {
        return 2;
    }

    @Override
    public int getMaxSize() {
        return Integer.MAX_VALUE;
    }
}
