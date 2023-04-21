package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public final class FloatingPointUnderflow extends ArithmeticError {

    public FloatingPointUnderflow(LispObject initArgs) throws ConditionThrowable {
        super(StandardClass.FLOATING_POINT_UNDERFLOW);
        initialize(initArgs);
    }

    @Override
    public LispObject typeOf() {
        return SymbolConstants.FLOATING_POINT_UNDERFLOW;
    }

    @Override
    public LispObject classOf() {
        return StandardClass.FLOATING_POINT_UNDERFLOW;
    }

    @Override
    public LispObject typep(LispObject type) throws ConditionThrowable {
        if (type == SymbolConstants.FLOATING_POINT_UNDERFLOW) return T;
        if (type == StandardClass.FLOATING_POINT_UNDERFLOW) return T;
        return super.typep(type);
    }
}
