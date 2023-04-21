package org.deri.iris.builtins;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Checks whether two terms have exactly the same type.
 */
public class SameTypeBuiltin extends BooleanBuiltin {

    /**
	 * Constructor.
	 * @param terms List of exactly one term.
	 */
    public SameTypeBuiltin(final ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected boolean computeResult(ITerm[] terms) {
        assert terms.length == 2;
        return terms[0].getClass() == terms[1].getClass();
    }

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = org.deri.iris.factory.Factory.BASIC.createPredicate("SAME_TYPE", 2);
}
