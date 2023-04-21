package org.taak.module.math;

import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.literal.*;
import org.taak.util.*;

/**
 * Function to return the arc tangent of the argument.
 */
public class ArcTangent extends UnaryFunction {

    public static final ArcTangent INSTANCE = new ArcTangent();

    public static final String NAME = "atan";

    /**
     * Returns the arc tangent of the argument.
     */
    public Object apply(Context context, Object arg) {
        Object result = null;
        if (arg == null) {
            throw new NullArgument("Null argument to atan()");
        }
        if (arg instanceof Number) {
            result = new Double(Math.atan(((Number) arg).doubleValue()));
        } else {
            throw new TypeError("Wrong argument type to atan()");
        }
        return result;
    }

    public String getName() {
        return NAME;
    }
}
