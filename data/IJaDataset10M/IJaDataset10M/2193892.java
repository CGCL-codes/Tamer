package com.winance.optimizer.metrics.rebalancers;

import java.util.ArrayList;

public abstract class RebalancerBase implements IPortfolioRebalancer {

    public void rebalance(final ArrayList<Double> rebalancingKoeffs, int[] percents, final ArrayList<Double> currentRatios) {
        double portfolioRatio = 0;
        for (int i = 0; i < currentRatios.size(); i++) {
            portfolioRatio += currentRatios.get(i) * (percents[i] / 100.) * rebalancingKoeffs.get(i);
        }
        for (int i = 0; i < currentRatios.size(); i++) {
            double newRatio = 0;
            if (0 != percents[i]) {
                newRatio = portfolioRatio / currentRatios.get(i);
            }
            rebalancingKoeffs.set(i, newRatio);
        }
    }
}
