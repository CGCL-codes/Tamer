package org.matsim.contrib.freight.replanning;

import java.util.ArrayList;
import java.util.List;
import org.matsim.contrib.freight.carrier.Actor;
import org.matsim.core.gbl.MatsimRandom;

public class PlanStrategyManager<T extends Actor> {

    private List<PlanStrategy<T>> strategies = new ArrayList<PlanStrategy<T>>();

    private List<Double> weights = new ArrayList<Double>();

    public void addStrategy(PlanStrategy<T> strategy, double weight) {
        strategies.add(strategy);
        weights.add(weight);
    }

    public PlanStrategy<T> nextStrategy() {
        double randValue = MatsimRandom.getRandom().nextDouble();
        double sumOfWeights = 0.0;
        for (int i = 0; i < strategies.size(); i++) {
            sumOfWeights += weights.get(i);
            if (randValue <= sumOfWeights) {
                return strategies.get(i);
            }
        }
        throw new IllegalStateException("no strat found");
    }
}
