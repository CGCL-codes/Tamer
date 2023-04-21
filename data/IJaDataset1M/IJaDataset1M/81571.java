package com.thesett.aima.logic.fol.prolog.builtins;

import com.thesett.aima.logic.fol.Functor;
import com.thesett.aima.logic.fol.NumericType;
import com.thesett.aima.logic.fol.Term;

/**
 * Implements the integer/1 runtime type check, that checks its argument is instantiated to an integer.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Checks that a term is instantiated to an integer value. <td> {@link ResolutionState}.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class IntegerCheck extends BaseBuiltIn implements BuiltIn {

    /**
     * Creates an integer built-in to implement the specified functor.
     *
     * @param functor The functor to implement as a built in.
     */
    public IntegerCheck(Functor functor) {
        super(functor);
    }

    /** {@inheritDoc} */
    public boolean proofStep(ResolutionState state) {
        Functor goalTerm = state.getGoalStack().poll().getFunctor();
        Term argument = goalTerm.getArgument(0).getValue();
        return argument.isNumber() && ((NumericType) argument.getValue()).isInteger();
    }
}
