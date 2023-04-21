package org.taak.operator;

import org.taak.*;

public class BitOr extends Code {

    private Code arg1;

    private Code arg2;

    public BitOr(Code arg1, Code arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Object eval(Context context) {
        Object res = null;
        Object o1 = context.eval(arg1);
        Object o2 = context.eval(arg2);
        if (o1 == null || o2 == null) return null;
        if (o1 instanceof Number && o2 instanceof Number) {
            long n1 = ((Number) o1).longValue();
            long n2 = ((Number) o2).longValue();
            res = new Long(n1 | n2);
        }
        return res;
    }

    public String toString() {
        return "(" + arg1 + "|" + arg2 + ")";
    }
}
