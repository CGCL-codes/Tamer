package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.INOTATION;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Checks if a term is of type 'NOTATION'.
 * 
 * @author Adrian Marte
 */
public class IsNOTATIONBuiltin extends BooleanBuiltin {

    /** The predicate defining this built-in. */
    public static final IPredicate PREDICATE = BASIC.createPredicate("IS_NOTATION", 1);

    /**
	 * Constructor.
	 * 
	 * @param terms The list of terms. Must always be of length 1 in this case.
	 */
    public IsNOTATIONBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected boolean computeResult(ITerm[] terms) {
        return isNOTATION(terms[0]);
    }

    public static boolean isNOTATION(ITerm term) {
        return term instanceof INOTATION;
    }
}
