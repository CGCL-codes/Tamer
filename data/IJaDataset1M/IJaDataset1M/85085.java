package org.vikamine.kernel.formula.operators;

import org.vikamine.kernel.formula.EvaluationData;
import org.vikamine.kernel.formula.FormulaNumberElement;

/**
 * Maximum term
 * 
 * @author Tobias Vogele, atzmueller
 */
public class Max extends AbstractTwoNumberArgumentsTerm implements FormulaNumberElement {

    /**
     * Creates a new FormulaTerm with null-arguments.
     */
    public Max() {
        this(null, null);
    }

    /**
     * Creates a new FormulaTerm max(arg1, arg2)
     * 
     * @param arg1
     *            first argument of the term
     * @param arg2
     *            second argument of the term
     */
    public Max(FormulaNumberElement arg1, FormulaNumberElement arg2) {
        setArg1(arg1);
        setArg2(arg2);
        setSymbol("max");
    }

    /**
     * @return the maximum of the evaluated arguments
     */
    @Override
    public synchronized Double eval(EvaluationData data) {
        if (!evalArguments(data)) return null; else return new Double(Math.max(getEvaluatedArg1().doubleValue(), getEvaluatedArg2().doubleValue()));
    }
}
