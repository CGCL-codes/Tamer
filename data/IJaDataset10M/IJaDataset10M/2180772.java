package net.sourceforge.jabm.strategy;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.learning.StimuliResponseLearner;
import org.springframework.beans.factory.ObjectFactory;
import cern.jet.random.engine.RandomEngine;

public class RlStrategyWithImitation extends RlStrategy implements ImitableStrategy, ImitatingStrategy {

    protected RandomEngine prng;

    protected ObjectFactory<Strategy> mutationFactory;

    public RlStrategyWithImitation(Agent agent, ObjectFactory<Strategy> strategyFactory, StimuliResponseLearner learner) {
        super(agent, strategyFactory, learner);
    }

    public RlStrategyWithImitation(ObjectFactory<Strategy> strategyFactory, StimuliResponseLearner learner) {
        super(strategyFactory, learner);
    }

    public void imitate(Agent otherAgent) {
        if (otherAgent.getStrategy() instanceof ImitableStrategy) {
            ImitableStrategy otherStrategy = (ImitableStrategy) otherAgent.getStrategy();
            Strategy strategyToImitiate = (Strategy) otherStrategy.createMimicStrategy();
            strategyToImitiate.setAgent(this.getAgent());
            int worst = this.learner.worstAction();
            disposeOfAction(worst);
            this.actions[worst] = strategyToImitiate;
        }
    }

    @Override
    public Strategy createMimicStrategy() {
        try {
            int best = this.learner.bestAction();
            Strategy bestStrategy = this.actions[best];
            if (logger.isDebugEnabled()) {
                logger.debug("Someone else is copying my best strategy = " + bestStrategy);
                logger.debug("learner = " + learner);
            }
            return (Strategy) bestStrategy.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public RandomEngine getPrng() {
        return prng;
    }

    public void setPrng(RandomEngine prng) {
        this.prng = prng;
    }

    public void mutate() {
        int action = learner.worstAction();
        Strategy newStrategy = mutationFactory.getObject();
        newStrategy.setAgent(this.getAgent());
        disposeOfAction(action);
        actions[action] = newStrategy;
    }

    public void disposeOfAction(int action) {
        actions[action].unsubscribeFromEvents();
        actions[action].setAgent(null);
    }

    public ObjectFactory<Strategy> getMutationFactory() {
        return mutationFactory;
    }

    public void setMutationFactory(ObjectFactory<Strategy> mutationFactory) {
        this.mutationFactory = mutationFactory;
    }
}
