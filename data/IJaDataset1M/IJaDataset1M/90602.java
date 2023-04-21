package org.vizzini.ai.geneticprogramming;

import ec.EvolutionState;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

/**
 * @author Jeffrey M. Thompson
 *
 */
public abstract class AbstractTerminal extends GPNode {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * @see  ec.gp.GPNode#checkConstraints(ec.EvolutionState, int,
     *       ec.gp.GPIndividual, ec.util.Parameter)
     */
    @Override
    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 0) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
}
