package com.newisys.langschema.java;

/**
 * Represents a Java less-or-equal operation.
 * 
 * @author Trevor Robinson
 */
public final class JavaLessOrEqual extends JavaRelationalOperation {

    public JavaLessOrEqual(JavaSchema schema, JavaExpression op1, JavaExpression op2) {
        super(schema, op1, op2);
    }

    public void accept(JavaExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
