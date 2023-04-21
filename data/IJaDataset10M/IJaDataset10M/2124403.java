package org.hswgt.teachingbox.core.rl.learner;

import cern.colt.function.DoubleFunction;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.valuefunctions.DifferentiableQFunction;
import cern.colt.matrix.DoubleMatrix1D;
import org.hswgt.teachingbox.core.rl.datastructures.ActionSet;
import org.hswgt.teachingbox.core.rl.feature.FeatureFunction;

/**
 * Modified Gradient descent method similar to SARSA. It's neither SARSA nor
 * Q-learning because it uses the q-value of the (nextState, currentAction) for 
 * the td-error. 
 * See update rule from "Learning to reacj by reinforcement learning using a
 * receptive field based function approximation approach with continuous actions"
 * from Minija Tamosiunaite
 * 
 * @see <a href="http://www.cs.ualberta.ca/%7Esutton/book/ebook/node89.html">http://www.cs.ualberta.ca/%7Esutton/book/ebook/node89.html</a>
 */
public class GradientDescentLinearAverageStraigthenLearner extends GradientDescentTdLearner {

    private static final long serialVersionUID = 2571781809714199751L;

    protected DifferentiableQFunction Q;

    /**
     * Constructs a new SARSA Learner that uses gradient descent
     * to learn a Q-Function
     * @param Q The Q-Function to learn
     * @param pi The policy
     */
    public GradientDescentLinearAverageStraigthenLearner(DifferentiableQFunction Q, FeatureFunction featureFunction, ActionSet actionSet) {
        super(Q, featureFunction, actionSet);
        this.Q = Q;
    }

    public DoubleMatrix1D getTdErrors(State s, Action a, State sn, Action an, double r, boolean isTerminalState) {
        double qn = 0;
        if (!isTerminalState) {
            qn = Q.getValue(sn, a);
        }
        DoubleMatrix1D weights = Q.getWeights();
        final double tmp = r + gamma * qn;
        DoubleMatrix1D tdErrors = weights.copy().assign(new DoubleFunction() {

            public double apply(double weight) {
                return tmp - weight;
            }
        });
        return tdErrors;
    }

    public DoubleMatrix1D getGradient(State s, Action a, State sn, Action an, double r, boolean isTerminalState) {
        return Q.getGradient(s, a);
    }
}
