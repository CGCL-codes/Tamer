package org.deri.iris.builtins.date;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IYearMonthDuration;
import org.deri.iris.builtins.DivideBuiltin;

/**
 * <p>
 * Represents the RIF built-in function
 * func:divide-yearMonthDuration-by-yearMonthDuration.
 * </p>
 */
public class YearMonthDurationDivideByYearMonthDurationBuiltin extends DivideBuiltin {

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = BASIC.createPredicate("YEARMONTHDURATION_DIVIDE_BY_YEARMONTHDURATION", 3);

    /**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms The terms.
	 * @throws NullPointerException If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException If the number of terms submitted is not
	 *             3.
	 */
    public YearMonthDurationDivideByYearMonthDurationBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    @Override
    protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) throws EvaluationException {
        if (terms[0] instanceof IYearMonthDuration && terms[1] instanceof IYearMonthDuration) {
            return super.computeMissingTerm(missingTermIndex, terms);
        }
        return null;
    }
}
