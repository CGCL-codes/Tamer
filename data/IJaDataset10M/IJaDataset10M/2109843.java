package ec.app.edge.func;

import ec.*;
import ec.app.edge.*;
import ec.gp.*;
import ec.util.*;

/**
 * @author Sean Luke
 * @version 1.0 
 */
public class Reverse extends GPNode {

    public String toString() {
        return "reverse";
    }

    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 1) state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        int edge = ((EdgeData) (input)).edge;
        Edge prob = (Edge) problem;
        int swap = prob.from[edge];
        prob.from[edge] = prob.to[edge];
        prob.to[edge] = swap;
        children[0].eval(state, thread, input, stack, individual, problem);
    }
}
