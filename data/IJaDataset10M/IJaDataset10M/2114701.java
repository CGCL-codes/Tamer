package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;
import java.math.BigInteger;

public final class logbitp extends Primitive {

    private logbitp() {
        super("logbitp", "index integer");
    }

    @Override
    public LispObject execute(LispObject first, LispObject second) throws ConditionThrowable {
        int index = -1;
        if (first instanceof Fixnum) {
            index = first.intValue();
        } else if (first instanceof Bignum) {
            if (first.bigIntegerValue().signum() > 0) index = Integer.MAX_VALUE;
        }
        if (index < 0) return type_error(first, SymbolConstants.UNSIGNED_BYTE);
        BigInteger n;
        if (second.isInteger()) n = second.bigIntegerValue(); else return type_error(second, SymbolConstants.INTEGER);
        if (index == Integer.MAX_VALUE) return n.signum() < 0 ? T : NIL;
        return n.testBit(index) ? T : NIL;
    }

    private static final Primitive LOGBITP = new logbitp();
}
