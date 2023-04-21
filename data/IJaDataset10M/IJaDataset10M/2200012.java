package org.deri.iris.builtins;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IGDay;

/**
 * Checks if a term is of type 'GDay'.
 */
public class IsGDayBuiltin extends BooleanBuiltin {

    /**
	 * Constructor.
	 * @param t The list of terms. Must always be of length 1 in this case.
	 */
    public IsGDayBuiltin(final ITerm... t) {
        super(PREDICATE, t);
    }

    protected boolean computeResult(ITerm[] terms) {
        assert terms.length == 1;
        return terms[0] instanceof IGDay;
    }

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = org.deri.iris.factory.Factory.BASIC.createPredicate("IS_GDAY", 1);
}
