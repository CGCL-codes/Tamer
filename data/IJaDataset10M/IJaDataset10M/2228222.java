package net.sourceforge.piqle.algorithms;

public interface RewardLearner<TState, TAction> {

    /** Learn 
	 @param previous The state the agent was in before the action is performed.
	 * @param current The state the agent is in after performing the action. 
	 * @param action The action the agent took.
	 * @param reward The reward obtained for this move.
	 */
    public abstract void learn(TState previous, TState current, TAction action, double reward);
}
