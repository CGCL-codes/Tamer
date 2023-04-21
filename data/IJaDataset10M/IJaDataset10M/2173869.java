package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

final class FunctionBinding {

    LispObject name;

    LispObject value;

    final FunctionBinding next;

    FunctionBinding() {
        next = null;
    }

    FunctionBinding(LispObject name, LispObject value, FunctionBinding next) {
        this.name = name;
        this.value = value;
        this.next = next;
    }
}
