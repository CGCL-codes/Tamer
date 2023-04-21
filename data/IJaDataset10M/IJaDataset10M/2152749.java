package org.hswgt.teachingbox.learner.stepsize;

import org.hswgt.teachingbox.datastructures.ActionSet;
import org.hswgt.teachingbox.env.Action;
import org.hswgt.teachingbox.env.State;
import org.hswgt.teachingbox.policy.VdbeBoltzmannDecayPolicy;
import org.hswgt.teachingbox.valuefunctions.QFunction;

public class VdbeBoltzmannDecayAlpha extends VdbeBoltzmannDecayPolicy implements StepSizeCalculator {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public VdbeBoltzmannDecayAlpha(QFunction Q, ActionSet as, double temperature) {
        super(Q, as, temperature);
    }

    public double getAlpha(State s, Action a) {
        return this.epsilon.getValue(s, epsilonAction);
    }
}
