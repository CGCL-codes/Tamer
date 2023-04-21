package org.hswgt.teachingbox.policy;

import org.hswgt.teachingbox.datastructures.ActionSet;
import org.hswgt.teachingbox.env.Action;
import org.hswgt.teachingbox.env.State;
import org.hswgt.teachingbox.valuefunctions.QFunction;

public class DecayBestOnlyPolicy extends VdbeV1Policy {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5936754634113681186L;

    public DecayBestOnlyPolicy(QFunction Q, ActionSet as) {
        super(Q, as, 0);
    }

    protected double vdbeValueUpdate(double oldValue, double newValue, State s, Action a) {
        double newEpsilon = epsilon.getValue(s, epsilonAction);
        if ((this.Q.getValue(s, a) - this.Q.getMaxValue(s)) < 0.0001) {
            final ActionSet valid = as.getValidActions(s);
            double decay = 1.0 / (double) valid.size();
            newEpsilon = (1 - decay) * newEpsilon;
        }
        return newEpsilon;
    }
}
