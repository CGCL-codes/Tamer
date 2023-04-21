package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Checks if a term is not of type 'ID'.
 * 
 * @author Adrian Marte
 */
public class IsNotIDBuiltin extends BooleanBuiltin {

    /** The predicate defining this built-in. */
    public static final IPredicate PREDICATE = BASIC.createPredicate("IS_NOT_ID", 1);

    /**
	 * Constructor.
	 * 
	 * @param terms The list of terms. Must always be of length 1 in this case.
	 */
    public IsNotIDBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected boolean computeResult(ITerm[] terms) {
        return !IsIDBuiltin.isID(terms[0]);
    }
}
