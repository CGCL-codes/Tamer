package org.taak.function;

import java.util.Map;
import org.taak.Context;

/**
 * Base class for functions with three arguments.
 */
public abstract class TernaryFunction extends Function {

    public Object apply(Context context, Object[] args) {
        return apply(context, args[0], args[1], args[2]);
    }

    public abstract Object apply(Context context, Object arg1, Object arg2, Object arg3);
}
