package org.taak.module.core;

import java.util.*;
import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.util.*;

/**
 * Core function to replace consecutive whitespace characters with a
 * single space.
 */
public class Squeeze extends UnaryFunction {

    public static final Squeeze INSTANCE = new Squeeze();

    public static final String NAME = "squeeze";

    /**
     * Replace consecutive whitespace characters with a single space.
     */
    public Object apply(Context context, Object arg) {
        if (arg == null) {
            throw new NullArgument("Null argument to squeeze()");
        }
        if (!(arg instanceof String)) {
            throw new TypeError("First argument to squeeze() is not String");
        }
        return StringUtil.squeeze((String) arg);
    }

    public String getName() {
        return NAME;
    }
}
