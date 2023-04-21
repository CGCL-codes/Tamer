package org.deri.iris.builtins.date;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.builtins.BuiltinHelper;
import org.deri.iris.builtins.FunctionalBuiltin;

/**
 * Represents the RIF built-in function func:timezone-from-date.
 */
public class TimezoneFromDateBuiltin extends FunctionalBuiltin {

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = BASIC.createPredicate("TIMEZONE_FROM_DATE", 2);

    /**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms The terms.
	 * @throws NullPointerException If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException If the number of terms submitted is not
	 *             2.
	 */
    public TimezoneFromDateBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected ITerm computeResult(ITerm[] terms) throws EvaluationException {
        if (terms[0] instanceof IDateTerm) {
            return BuiltinHelper.timezonePart(terms[0]);
        }
        return null;
    }
}
