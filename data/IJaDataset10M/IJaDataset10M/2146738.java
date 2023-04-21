package org.primordion.ealontro.ec.app.antforaging;

import ec.*;
import ec.gp.*;
import ec.util.*;

public class MoveToAdjacentFoodElse extends GPNode {

    private static final long serialVersionUID = 1L;

    public String toString() {
        return this.getXhcName();
    }

    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 2) state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
    }
}
