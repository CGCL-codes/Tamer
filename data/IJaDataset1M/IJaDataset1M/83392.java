package org.taak.operator;

import org.taak.*;
import org.taak.error.*;
import org.taak.util.*;

public class And extends Code {

    private Code arg1;

    private Code arg2;

    public And(Code arg1, Code arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Object eval(Context context) {
        Object obj1 = context.eval(arg1);
        if (Util.booleanValue(obj1)) {
            Object obj2 = context.eval(arg2);
            return Util.booleanValue(obj2) ? Boolean.TRUE : Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    public String toString() {
        return "(" + arg1 + "&&" + arg2 + ")";
    }
}
