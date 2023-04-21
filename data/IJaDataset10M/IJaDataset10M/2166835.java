package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;
import java.lang.reflect.Method;

public final class jmethod_return_type extends Primitive {

    private jmethod_return_type() {
        super(SymbolConstants.JMETHOD_RETURN_TYPE, "method", "Returns a reference to the Class object that represents the formal return type of METHOD.");
    }

    @Override
    public LispObject execute(LispObject arg) throws ConditionThrowable {
        if (arg instanceof IJavaObject) {
            Object method = ((IJavaObject) arg).getObject();
            if (method instanceof Method) return makeNewJavaObject(((Method) method).getReturnType());
        }
        return error(new LispError(arg.writeToString() + " does not designate a Java method."));
    }

    private static final Primitive JMETHOD_RETURN_TYPE = new jmethod_return_type();
}
