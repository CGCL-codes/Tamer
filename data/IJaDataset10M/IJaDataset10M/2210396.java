package edu.cmu.ece.wnp5.bandit;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

/**
 *
 * @author shahriyar
 */
public class IntervalEstimationGambler extends AbstractGamblerBase {

    private double alpha;

    private int lastPulledLever = 0;

    public IntervalEstimationGambler(double alpha) {
        if (alpha <= 0.0 || alpha >= 1.0) {
        }
        this.alpha = alpha;
    }

    public int play(int horizon) {
        if (observedLeverCount < 1 || twiceObservedLeverCount < 1) {
            if (observationCounts[lastPulledLever] == 1) {
                return lastPulledLever;
            }
            return random.nextInt(getLeverCount());
        }
        double maxPrice = Double.NEGATIVE_INFINITY;
        int maxPriceIndex = -1;
        for (int i = 0; i < getLeverCount(); i++) {
            if (observationCounts[i] > 0) {
                double price = 0;
                if (observationCounts[i] > 1) {
                    price = price(leverMean(i), leverSigma(i), observationCounts[i]);
                } else {
                    price = price(leverMean(i), leverSigmaSum / twiceObservedLeverCount, 1);
                }
                if (price > maxPrice) {
                    maxPrice = price;
                    maxPriceIndex = i;
                }
            }
        }
        if (observedLeverCount < getLeverCount()) {
            double uPrice = price(leverMeanSum / observedLeverCount, leverSigmaSum / twiceObservedLeverCount, 1);
            if (uPrice > maxPrice) {
                maxPrice = uPrice;
                int uIndex = random.nextInt(getLeverCount() - observedLeverCount);
                int uCount = 0;
                for (int i = 0; i < getLeverCount(); i++) {
                    if (observationCounts[i] == 0) {
                        if (uCount == uIndex) {
                            maxPriceIndex = i;
                            break;
                        } else uCount++;
                    }
                }
            }
        }
        lastPulledLever = maxPriceIndex;
        return maxPriceIndex;
    }

    private double price(double mean, double sigma, int count) {
        try {
            NormalDistribution nd = new NormalDistributionImpl(mean, sigma / Math.sqrt(Math.max(count, 1)));
            return nd.inverseCumulativeProbability(1 - alpha);
        } catch (MathException ex) {
            return 0.0;
        }
    }
}
