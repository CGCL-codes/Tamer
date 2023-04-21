package org.vikamine.kernel.formula.operators;

import org.vikamine.kernel.formula.EvaluationData;
import org.vikamine.kernel.formula.FormulaNumberElement;

/**
 * Minimum term
 * 
 * @author Tobias Vogele, atzmueller
 */
public class Min extends AbstractTwoNumberArgumentsTerm implements FormulaNumberElement {

    /**
     * Creates a new FormulaTerm with null-arguments.
     */
    public Min() {
        this(null, null);
    }

    /**
     * Creates a new FormulaTerm min(arg1, arg2)
     * 
     * @param arg1
     *            first argument of the term
     * @param arg2
     *            second argument of the term
     */
    public Min(FormulaNumberElement arg1, FormulaNumberElement arg2) {
        setArg1(arg1);
        setArg2(arg2);
        setSymbol("min");
    }

    /**
     * @return the minimum of the evaluated arguments
     */
    @Override
    public synchronized Double eval(EvaluationData data) {
        if (!evalArguments(data)) return null; else return new Double(Math.min(getEvaluatedArg1().doubleValue(), getEvaluatedArg2().doubleValue()));
    }
}
