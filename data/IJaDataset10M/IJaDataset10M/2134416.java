package org.primordion.ealontro.ec.app.cartcentering;

import ec.*;
import ec.gp.*;
import ec.util.*;

public class XPosition extends GPNode {

    private static final long serialVersionUID = 1L;

    public String toString() {
        return "x";
    }

    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 0) state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        DoubleData rd = ((DoubleData) (input));
        rd.x = ((CartCentering) problem).xPosition;
    }
}
