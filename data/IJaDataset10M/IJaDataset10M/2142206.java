package net.sourceforge.piqle.algorithms.impl;

import net.sourceforge.piqle.environment.PossibleActionsCalculator;
import net.sourceforge.piqle.qlearning.ActionStatePair;
import net.sourceforge.piqle.qlearning.LearningActionValueEstimator;
import net.sourceforge.piqle.rewards.ActionComparatorFactory;
import net.sourceforge.piqle.traces.EligibilityTrace;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Peng's implementation of Q(lambda) <br>
 * <a href="http://www.cs.ualberta.ca/~sutton/book/ebook/node78.html"> Sutton
 * and Barto chap 7.6 page 182</a><br>
 * 
 * Reference : <a
 * href="ftp://ftp.ccs.neu.edu/pub/people/rjw/qlambda-ml-96.ps">Peng et Williams
 * 1996</a>
 * 
 * @author Francesco De Comite (decomite at lifl.fr)
 * @version $Revision: 1.0 $
 * 
 * 
 * 
 */
@Singleton
public class PengLearner<TState, TAction> extends QLambdaLearner<TState, TAction> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param rewardStore
	 *            TODO
	 * @param eligibilityTraceProvider TODO
	 * @param comparatorFactory 
	 * @param actionsCalculator 
	 * @param learningRate 
	 */
    @Inject
    public PengLearner(LearningActionValueEstimator rewardStore, Provider<EligibilityTrace> eligibilityTraceProvider, ActionComparatorFactory comparatorFactory, PossibleActionsCalculator actionsCalculator, Provider<Double> learningRate) {
        super(rewardStore, eligibilityTraceProvider, comparatorFactory, actionsCalculator, learningRate);
    }

    /**
	 * Learning from experience.
	 * 
	 * @param s1
	 *            the start state.
	 * @param s2
	 *            the arrival state.
	 * @param a
	 *            the chosen action. <br>
	 *            <a
	 *            href="ftp://ftp.ccs.neu.edu/pub/people/rjw/qlambda-ml-96.ps">Peng
	 *            et Williams 1996</a><br>
	 * 
	 * The implemented algorithm is described in the above mentioned paper, at
	 * page 5
	 * @param reward
	 *            immediate reward.
	 * 
	 */
    public void learn(TState s1, TState s2, TAction a, double reward) {
        double maximumSuccessorStateValue = getStateValue(s2);
        double maximumPredecessorStateValue = getStateValue(s1);
        double expectedReturn = getExpectedReturn(s1, a);
        double lostReturn = reward + getDiscountRate() * maximumSuccessorStateValue - maximumPredecessorStateValue;
        double estimationError = reward + getDiscountRate() * maximumSuccessorStateValue - expectedReturn;
        for (ActionStatePair<TState, TAction> currentStateAction : eligibilityTrace) {
            TAction action = currentStateAction.getAction();
            TState state = currentStateAction.getState();
            double old = getExpectedReturn(state, action);
            learn(state, action, old + getLearningRate() * eligibilityTrace.get(currentStateAction) * lostReturn);
        }
        learn(s1, a, expectedReturn + getLearningRate() * estimationError);
        eligibilityTrace.increment(new ActionStatePair<TState, TAction>(a, s1), 1.0);
    }
}
