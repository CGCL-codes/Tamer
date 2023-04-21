package org.deri.iris.rdb.evaluation;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * Helper methods for the tests in this package.
 */
public class Helper {

    public static ITerm createTerm(Object t) {
        if (t instanceof Integer) return CONCRETE.createInteger((Integer) t); else if (t instanceof String) return TERM.createVariable((String) t); else if (t instanceof ITerm) return (ITerm) t;
        throw new RuntimeException("Unsupported term type in Helper.createTerm()");
    }

    public static List<ITerm> createTerms(Object... termObjects) {
        List<ITerm> terms = new ArrayList<ITerm>();
        for (Object o : termObjects) terms.add(createTerm(o));
        return terms;
    }

    public static ITuple createTuple(Object... termObjects) {
        return BASIC.createTuple(createTerms(termObjects));
    }

    public static ITerm createConstant(Object t) {
        if (t instanceof Integer) return CONCRETE.createInteger((Integer) t); else if (t instanceof String) return TERM.createString((String) t); else if (t instanceof ITerm) return (ITerm) t;
        throw new RuntimeException("Unsupported term type in Helper.createTerm()");
    }

    public static List<ITerm> createConstants(Object... termObjects) {
        List<ITerm> terms = new ArrayList<ITerm>();
        for (Object o : termObjects) terms.add(createConstant(o));
        return terms;
    }

    public static ITuple createConstantTuple(Object... termObjects) {
        return BASIC.createTuple(createConstants(termObjects));
    }

    public static ITerm createConstructedTerm(String symbol, Object... termObjects) {
        return TERM.createConstruct(symbol, createTerms(termObjects));
    }

    public static ILiteral createLiteral(boolean positive, String predicate, Object... termObjects) {
        ITuple tuple = createTuple(termObjects);
        return BASIC.createLiteral(positive, BASIC.createPredicate(predicate, tuple.size()), tuple);
    }
}
