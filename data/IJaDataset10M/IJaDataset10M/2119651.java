package org.taak.operator;

import org.taak.*;
import org.taak.error.*;
import org.taak.util.*;

public class NotEqual extends Code {

    private Code arg1;

    private Code arg2;

    public NotEqual(Code arg1, Code arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Object eval(Context context) {
        Object o1 = context.eval(arg1);
        Object o2 = context.eval(arg2);
        return Util.equals(o1, o2) ? Boolean.FALSE : Boolean.TRUE;
    }

    public String toString() {
        return "(" + arg1 + "!=" + arg2 + ")";
    }
}
