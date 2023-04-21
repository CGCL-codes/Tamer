package org.vizzini.example.geneticprogramming.keplersequation;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import org.vizzini.ai.geneticprogramming.AbstractFunction1;

/**
 * @author  Jeffrey M. Thompson
 */
public class SquareRootFunction extends AbstractFunction1 {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * @see  ec.gp.GPNode#eval(ec.EvolutionState, int, ec.gp.GPData,
     *       ec.gp.ADFStack, ec.gp.GPIndividual, ec.Problem)
     */
    @Override
    public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack, final GPIndividual individual, final Problem problem) {
        Context context = (Context) input;
        children[0].eval(state, thread, input, stack, individual, problem);
        double result = context._x;
        if (result >= 0.0) {
            context._x = Math.sqrt(result);
        } else {
            context._x = 1.0;
        }
    }

    /**
     * @see  ec.gp.GPNode#toString()
     */
    @Override
    public String toString() {
        return "sqrt";
    }
}
