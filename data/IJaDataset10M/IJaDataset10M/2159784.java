package ec.app.multiplexer.func;

import ec.*;
import ec.app.multiplexer.*;
import ec.gp.*;
import ec.util.*;

/**
 * @author Sean Luke
 * @version 1.0 
 */
public class Or extends GPNode {

    public String toString() {
        return "or";
    }

    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 2) state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        MultiplexerData md = (MultiplexerData) input;
        long[] dat_11 = null;
        long dat_6 = 0L;
        byte dat_3 = 0;
        children[0].eval(state, thread, input, stack, individual, problem);
        if (md.status == MultiplexerData.STATUS_3) dat_3 = md.dat_3; else if (md.status == MultiplexerData.STATUS_6) dat_6 = md.dat_6; else {
            dat_11 = md.popDat11();
            System.arraycopy(md.dat_11, 0, dat_11, 0, MultiplexerData.MULTI_11_NUM_BITSTRINGS);
        }
        children[1].eval(state, thread, input, stack, individual, problem);
        if (md.status == MultiplexerData.STATUS_3) md.dat_3 |= dat_3; else if (md.status == MultiplexerData.STATUS_6) md.dat_6 |= dat_6; else {
            for (int x = 0; x < MultiplexerData.MULTI_11_NUM_BITSTRINGS; x++) md.dat_11[x] |= dat_11[x];
            md.pushDat11(dat_11);
        }
    }
}
