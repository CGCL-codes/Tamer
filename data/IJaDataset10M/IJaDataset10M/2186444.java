package org.taak.operator;

import org.taak.*;
import org.taak.error.*;
import org.taak.util.*;

public class Conditional extends Code {

    private Code arg1;

    private Code arg2;

    private Code arg3;

    public Conditional(Code arg1, Code arg2, Code arg3) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public Object eval(Context context) {
        Object res = null;
        Object cond = context.eval(arg1);
        if (Util.booleanValue(cond)) {
            res = context.eval(arg2);
        } else {
            res = context.eval(arg3);
        }
        return res;
    }

    public String toString() {
        return "(" + arg1 + "?" + arg2 + ":" + arg3 + ")";
    }
}
