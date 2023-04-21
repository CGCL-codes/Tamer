package org.taak.module.math;

import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.literal.*;
import org.taak.util.*;

/**
 * Function to return the arc cosine of the argument.
 */
public class ArcCosine extends UnaryFunction {

    public static final ArcCosine INSTANCE = new ArcCosine();

    public static final String NAME = "acos";

    /**
     * Returns the arc cosine of the argument.
     */
    public Object apply(Context context, Object arg) {
        Object result = null;
        if (arg == null) {
            throw new NullArgument("Null argument to acos()");
        }
        if (arg instanceof Number) {
            result = new Double(Math.acos(((Number) arg).doubleValue()));
        } else {
            throw new TypeError("Wrong argument type to acos()");
        }
        return result;
    }

    public String getName() {
        return NAME;
    }
}
