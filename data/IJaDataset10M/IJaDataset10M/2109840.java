package drjava.smyle.core;

import drjava.smyle.meta.*;

public class DynFilter<T extends Struct<T>> extends Filter<T> {

    public <A> void functionEquals(Function<T, A> f, A value) {
        clauses.add(new Filter.FunctionEquals<A>(f, value));
    }
}
