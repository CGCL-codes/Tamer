package org.deri.iris.builtins.datatype;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Checks whether a term is not a string.
 */
public class IsNotStringBuiltin extends BooleanBuiltin {

    public IsNotStringBuiltin(final ITerm... t) {
        super(PREDICATE, t);
    }

    protected boolean computeResult(ITerm[] terms) {
        return !IsStringBuiltin.isString(terms[0]);
    }

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = org.deri.iris.factory.Factory.BASIC.createPredicate("IS_NOT_STRING", 1);
}
