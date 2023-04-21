package org.hswgt.teachingbox.core.rl.learner;

import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import org.hswgt.teachingbox.core.rl.valuefunctions.DifferentiableQFunction;
import cern.colt.matrix.DoubleMatrix1D;
import org.hswgt.teachingbox.core.rl.datastructures.ActionSet;
import org.hswgt.teachingbox.core.rl.feature.FeatureFunction;

/**
 * Gradient descent QLearning
 * 
 * @see <a href="http://www.cs.ualberta.ca/%7Esutton/book/ebook/node89.html">http://www.cs.ualberta.ca/%7Esutton/book/ebook/node89.html</a>
 */
public class GradientDescentQLearner extends GradientDescentTdLearner {

    private static final long serialVersionUID = 1316859261056571809L;

    protected DifferentiableQFunction Q;

    /**
     * Constructs a new QLearner that uses gradient descent
     * to learn a Q-Function
     * @param Q The Q-Function to learn
     */
    public GradientDescentQLearner(DifferentiableQFunction Q, FeatureFunction featureFunction, ActionSet actionSet) {
        super(Q, featureFunction, actionSet);
        this.Q = Q;
    }

    public DoubleMatrix1D getTdErrors(State s, Action a, State sn, Action an, double r, boolean isTerminalState) {
        double q = Q.getValue(s, a);
        double qn = 0;
        if (!isTerminalState) {
            qn = Q.getMaxValue(sn);
        }
        return Q.getWeights().like().assign(r + gamma * qn - q);
    }

    public DoubleMatrix1D getGradient(State s, Action a, State sn, Action an, double r, boolean isTerminalState) {
        return Q.getGradient(s, a);
    }
}
