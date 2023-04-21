package tefkat.engine.runtime.functions;

import tefkat.engine.runtime.Binding;
import tefkat.engine.runtime.Context;
import tefkat.engine.runtime.Function;

public final class Subtract implements Function {

    public Object call(Context ctxt, Binding binding, Object[] params) {
        Number lhs = (Number) params[0];
        Number rhs = (Number) params[1];
        if (lhs instanceof Float || rhs instanceof Float || lhs instanceof Double || rhs instanceof Double) {
            double lval = lhs.doubleValue();
            double rval = rhs.doubleValue();
            return new Double(lval - rval);
        } else {
            long lval = lhs.longValue();
            long rval = rhs.longValue();
            return new Long(lval - rval);
        }
    }
}
