package org.deri.iris.builtins.datatype;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTimeStamp;
import org.deri.iris.builtins.BooleanBuiltin;

/**
 * <p>
 * Checks whether a term is of type dateTimeStamp.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IsDateTimeStampBuiltin extends BooleanBuiltin {

    public IsDateTimeStampBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    protected boolean computeResult(ITerm[] terms) {
        return isDateTimeStamp(terms[0]);
    }

    public static boolean isDateTimeStamp(ITerm term) {
        return term instanceof IDateTimeStamp;
    }

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = org.deri.iris.factory.Factory.BASIC.createPredicate("IS_DATETIMESTAMP", 1);
}
