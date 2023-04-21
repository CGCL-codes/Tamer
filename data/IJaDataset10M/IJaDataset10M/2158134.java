package com.imaginaryday.ec.ecj.functions;

import com.imaginaryday.ec.ecj.PolyData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.util.Parameter;

/**
 * <b>
 * User: jlowens<br>
 * Date: Dec 9, 2006<br>
 * Time: 1:59:55 PM<br>
 * </b>
 */
public class IsMoving extends GPRoboNode {

    private static final long serialVersionUID = 711537334805440150L;

    public String toString() {
        return "isMoving";
    }

    @Override
    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 0) {
            state.output.error("incorrect # of children for " + toStringForError() + " at " + individualBase);
        }
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        PolyData data = (PolyData) input;
        double dr = getAgent().getDistanceRemaining();
        double tr = getAgent().getTurnRemainingRadians();
        data.b = Math.abs(dr) >= 0.0001 && Math.abs(tr) >= 0.0001;
    }
}
