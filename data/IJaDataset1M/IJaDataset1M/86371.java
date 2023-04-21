package org.hswgt.teachingbox.core.rl.env;

import java.util.LinkedList;
import java.util.List;
import org.hswgt.teachingbox.core.rl.agent.Agent;
import org.hswgt.teachingbox.core.rl.learner.Learner;

/**
 * Environment which delegates all methods to some other environment with the
 * difference that any rewards will be delayed by a specific delay.
 *
 * This environment can be used to study how to solve environments including
 * such delays (DelayedLearner is one possible way)
 */
public class RewardDelayedEnvironment implements Environment {

    protected Environment env;

    protected int delay = 0;

    protected List<Double> rewards = new LinkedList<Double>();

    protected boolean terminalStateReached = false;

    protected int stepsTillReturningDelayedRewards = this.delay;

    protected Learner learner;

    protected Agent agent;

    boolean learnerRemoved;

    public RewardDelayedEnvironment(Environment env, int delay, Agent agent, Learner learner) {
        this.env = env;
        this.delay = delay;
        this.agent = agent;
        this.learner = learner;
    }

    public double doAction(Action a) {
        double reward = this.env.doAction(a);
        if (!this.terminalStateReached) this.rewards.add(reward); else this.stepsTillReturningDelayedRewards--;
        if (this.rewards.size() > this.delay || (this.terminalStateReached && this.stepsTillReturningDelayedRewards < 0)) {
            if (this.learnerRemoved) {
                this.learnerRemoved = false;
                this.agent.addObserver(learner);
            }
            return this.rewards.remove(0);
        }
        return 0.0;
    }

    public State getState() {
        return this.env.getState();
    }

    public boolean isTerminalState() {
        if (this.env.isTerminalState() && !this.terminalStateReached) {
            this.terminalStateReached = true;
            this.stepsTillReturningDelayedRewards = this.delay - this.rewards.size();
        }
        if (this.terminalStateReached && this.rewards.size() == 0) {
            return true;
        }
        return false;
    }

    public void initRandom() {
        this.clear();
        this.env.initRandom();
        learner.updateNewEpisode(env.getState());
    }

    public void init(State state) {
        this.clear();
        this.env.init(state);
        learner.updateNewEpisode(env.getState());
    }

    protected void clear() {
        this.agent.removeObserver(learner);
        this.learnerRemoved = true;
        this.rewards.clear();
        this.terminalStateReached = false;
        this.stepsTillReturningDelayedRewards = this.delay;
    }

    public Environment getEnv() {
        return env;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
