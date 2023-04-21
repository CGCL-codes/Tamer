package org.taak.module.core;

import java.util.Collection;
import java.lang.reflect.Array;
import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.util.*;

/**
 * The core function to check whether a container is empty.
 */
public class IsEmpty extends UnaryFunction {

    public static final IsEmpty INSTANCE = new IsEmpty();

    public static final String NAME = "isEmpty";

    public Object apply(Context context, Object arg) {
        boolean result = true;
        if (arg == null) {
        } else if (arg instanceof Collection) {
            Collection coll = (Collection) arg;
            result = coll.isEmpty();
        } else if (arg instanceof String) {
            String s = (String) arg;
            result = s.length() == 0;
        } else if (arg.getClass().isArray()) {
            result = Array.getLength(arg) == 0;
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getName() {
        return NAME;
    }
}
