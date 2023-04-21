package ec.app.multiplexer.func;

import ec.*;
import ec.app.multiplexer.*;
import ec.gp.*;
import ec.util.*;

/**
 * @author Sean Luke
 * @version 1.0 
 */
public class D4 extends GPNode {

    static final int bitpos = 4;

    public String toString() {
        return "d4";
    }

    public void checkConstraints(final EvolutionState state, final int tree, final GPIndividual typicalIndividual, final Parameter individualBase) {
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length != 0) state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
    }

    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        MultiplexerData md = (MultiplexerData) input;
        if (md.status == MultiplexerData.STATUS_3) md.dat_3 = Fast.M_3[bitpos + MultiplexerData.STATUS_3]; else if (md.status == MultiplexerData.STATUS_6) md.dat_6 = Fast.M_6[bitpos + MultiplexerData.STATUS_6]; else System.arraycopy(Fast.M_11[bitpos + MultiplexerData.STATUS_11], 0, md.dat_11, 0, MultiplexerData.MULTI_11_NUM_BITSTRINGS);
    }
}
