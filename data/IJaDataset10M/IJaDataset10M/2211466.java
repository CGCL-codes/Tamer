package org.taak.module.core;

import java.util.*;
import org.taak.*;
import org.taak.error.*;
import org.taak.function.*;
import org.taak.util.*;

/**
 * Core function to print information about an object for debugging.
 */
public class Dump extends UnaryFunction {

    public static final Dump INSTANCE = new Dump();

    public static final String NAME = "dump";

    public Object apply(Context context, Object arg) {
        if (arg == null) {
            System.out.println("null");
        } else if (arg instanceof Scope) {
            Scope scope = (Scope) arg;
            String[] names = scope.getNames();
            System.out.println(Arrays.toString(names));
        } else if (arg instanceof Obj) {
            Obj obj = (Obj) arg;
            StringBuffer b = new StringBuffer();
            b.append(obj.getType().getName());
            b.append(obj.getMembers());
            System.out.println(b.toString());
        } else if (arg instanceof Type) {
            Type type = (Type) arg;
            StringBuffer b = new StringBuffer();
            type.dump(b);
            System.out.println(b.toString());
        } else {
            StringUtil.toString(arg.toString());
        }
        return null;
    }

    public Class getReturnType() {
        return Long.class;
    }

    public String getName() {
        return NAME;
    }
}
