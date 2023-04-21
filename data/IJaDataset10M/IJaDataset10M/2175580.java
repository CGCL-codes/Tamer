package com.sun.tools.javac.effect;

import com.sun.tools.javac.code.*;
import com.sun.tools.javac.code.Symbol.*;

public class FieldEffect extends Effect {

    public ClassSymbol clazz;

    public VarSymbol field;

    public FieldEffect(ClassSymbol clazz, VarSymbol field) {
        this.clazz = clazz;
        this.field = field;
    }

    public boolean equals(Object e) {
        if (!(e instanceof FieldEffect)) return false;
        FieldEffect fe = (FieldEffect) e;
        return symbolsEquiv(clazz, fe.clazz) && symbolsEquiv(field, fe.field);
    }

    public int hashCode() {
        return makeHashCode(clazz) + makeHashCode(field);
    }
}
