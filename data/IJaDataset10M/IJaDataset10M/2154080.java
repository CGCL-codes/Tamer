package org.hswgt.teachingbox.core.rl.policy.explorationrate;

import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.learner.Learner;

/**
 * @author Thomas Wanschik
 * Abstract interface to calculate exploration rates.
 * Learner interface matches perfectly so we just use it.
 */
public interface EpsilonCalculator extends Learner {

    /**
     * Get the parameter epsilon of the EpsilonGreedy policy.
     * @param state The actual state (for the case, epsilon depends on it).
     * @return Value of epsilon.
     */
    public double getEpsilon(State state);
}
