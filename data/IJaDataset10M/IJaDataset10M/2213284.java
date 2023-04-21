package org.deri.iris.builtins.numeric;

import static org.deri.iris.factory.Factory.BASIC;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.EqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:numeric-equal.
 * </p>
 */
public class NumericEqualBuiltin extends EqualBuiltin {

    /** The predicate defining this built-in. */
    private static final IPredicate PREDICATE = BASIC.createPredicate("NUMERIC_EQUAL", 2);

    public NumericEqualBuiltin(ITerm... terms) {
        super(PREDICATE, terms);
    }

    @Override
    protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
        if (checkTypes(missingTermIndex, terms, INumericTerm.class)) {
            return super.computeMissingTerm(missingTermIndex, terms);
        }
        return null;
    }
}
