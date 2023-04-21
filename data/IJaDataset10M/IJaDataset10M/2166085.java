package net.sourceforge.piqle.strategies.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.sourceforge.piqle.environment.states.nn.TypedAction;
import net.sourceforge.piqle.qlearning.LearningActionValueEstimator;
import net.sourceforge.piqle.strategies.AbstractMemoryBasedStrategy;
import net.sourceforge.piqle.strategies.ExploratoryStrategy;
import net.sourceforge.piqle.strategies.configuration.ExplorationFactor;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class BoltzmannStrategy<TState, TAction> extends AbstractMemoryBasedStrategy<TState, TAction> implements ExploratoryStrategy<TState, TAction> {

    /** The parameter for Boltzmann Action selection Strategy */
    private double tau = 0.5;

    private Random generator;

    private Provider<Double> explorationFactorProvider;

    private Object lastActionType = null;

    @Inject
    public BoltzmannStrategy(LearningActionValueEstimator memory, Random generator, @ExplorationFactor Provider<Double> explorationFactorProvider) {
        super(memory);
        this.generator = generator;
        this.explorationFactorProvider = explorationFactorProvider;
    }

    public TAction chooseAction(TState state, Collection<TAction> actions) {
        if (actions.size() == 0) return null;
        double sum = 0;
        List<Double> boltzmannValues = new ArrayList<Double>(actions.size());
        double explorationFactor = explorationFactorProvider.get();
        if (explorationFactor == 0.0) return getBestAction(state, actions);
        for (TAction action : actions) {
            double expectedReward = memory.get(state, action);
            if (Double.isNaN(expectedReward)) {
                throw new RuntimeException("Illegal value for reward: " + expectedReward);
            }
            double value = Math.exp(expectedReward * (1 - explorationFactor) / explorationFactor);
            sum += value;
            boltzmannValues.add(value);
            if (!((TypedAction) action).getType().equals(lastActionType)) lastActionType = ((TypedAction) action).getType();
        }
        double choice = generator.nextDouble() * sum;
        int i = 0;
        TAction chosenAction = null;
        for (TAction action : actions) {
            Double value = boltzmannValues.get(i++);
            if (choice <= value) {
                chosenAction = action;
                break;
            }
            choice -= value;
        }
        if (chosenAction == null) throw new RuntimeException("Something went wrong with the rewards: " + boltzmannValues);
        return chosenAction;
    }

    public void setExplorationFactor(double explorationFactor) {
        this.tau = explorationFactor + 0.0001;
    }

    @Override
    public double getExplorationFactor() {
        return tau;
    }
}
