package org.taak.module.core;

import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.util.*;

/**
 * The built in function to retrieve a member of an object.
 */
public class GetMember extends BinaryFunction {

    public static final GetMember INSTANCE = new GetMember();

    public static final String NAME = "getMember";

    public Object apply(Context context, Object arg1, Object arg2) {
        if (arg1 == null) {
            throw new NullArgument("First argument to getMember is null");
        }
        if (arg2 == null) {
            throw new NullArgument("Second argument to getMember is null");
        }
        if (!(arg2 instanceof String)) {
            throw new TypeError("Second argument to getMember is not String");
        }
        return ObjectUtil.getMember(arg1, (String) arg2);
    }

    public String getName() {
        return NAME;
    }
}
