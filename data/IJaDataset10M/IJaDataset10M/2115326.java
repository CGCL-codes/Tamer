package org.cantaloop.cgimlet.lang.java;

import org.cantaloop.cgimlet.lang.Type;

public class JNullExpr extends AbstractJExpr {

    public String getExpressionCode() {
        return "null";
    }

    public Type getType() {
        return new JType(Object.class);
    }

    public boolean isPrimitiveExpression() {
        return true;
    }
}
