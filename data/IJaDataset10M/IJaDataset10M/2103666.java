package org.hswgt.teachingbox.core.rl.datastructures;

import java.util.List;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;

/**
 * The TransitionFunction. If the agent is in state s, then the 
 * TransitionFunction gives the probability of being in state sn after action s
 * <pre>p(s'|s,a)</pre>
 */
public interface TransitionFunction extends java.io.Serializable {

    /**
     * A list of possible successor states with their probabilities
     * <pre>
     * List[0] = p(s_0|s,a)
     * List[1] = p(s_1|s,a)
     * List[2] = p(s_2|s,a)
     * ...</pre>
     * @param s The current state
     * @param a The action planned to do
     * @return The list of possible successor states with their probabilities
     */
    public List<TransitionProbability> getTransitionProbabilities(State state, Action action);
}
