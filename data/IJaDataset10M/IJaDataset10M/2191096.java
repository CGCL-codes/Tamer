package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IUnsignedInt;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * Checks if a term is of type 'UnsignedInt'.
 * 
 * @author Adrian Marte
 */
public class IsUnsignedIntBuiltin extends BooleanBuiltin {

    /** The predicate defining this built-in. */
    public static final IPredicate PREDICATE = BASIC.createPredicate("IS_UNSIGNEDINT", 1);

    /**
	 * Constructor.
	 * 
	 * @param terms The list of terms. Must always be of length 1 in this case.
	 */
    public IsUnsignedIntBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected boolean computeResult(ITerm[] terms) {
        return isUnsignedInt(terms[0]);
    }

    public static boolean isUnsignedInt(ITerm term) {
        return term instanceof IUnsignedInt;
    }
}
