package org.deri.iris.builtins.date;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.ITime;
import org.deri.iris.builtins.NotEqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:time-not-equal.
 * </p>
 */
public class TimeNotEqualBuiltin extends NotEqualBuiltin {

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = BASIC.createPredicate("TIME_NOT_EQUAL", 2);

    public TimeNotEqualBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    @Override
    protected boolean computeResult(ITerm[] terms) {
        if (terms[0] instanceof ITime && terms[1] instanceof ITime) {
            return super.computeResult(terms);
        }
        return false;
    }
}
