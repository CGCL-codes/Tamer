package ec.gp;

import ec.*;

/**
 * An ADM is an ADF which doesn't evaluate its arguments beforehand, but
 * instead only evaluates them (and possibly repeatedly) when necessary
 * at runtime.  For more information, see ec.gp.ADF.
 * @see ec.gp.ADF
 *
 * @author Sean Luke
 * @version 1.0 
 */
public class ADM extends ADF {

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        ADFContext c = stack.push(stack.get());
        c.prepareADM(this);
        individual.trees[associatedTree].child.eval(state, thread, input, stack, individual, problem);
        if (stack.pop(1) != 1) state.output.fatal("Stack prematurely empty for " + toStringForError());
    }
}
