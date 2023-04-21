package com.newisys.langschema.java;

/**
 * Base class for Java increment/decrement operations.
 * 
 * @author Trevor Robinson
 */
public abstract class JavaIncDecOperation extends JavaUnaryOperation {

    public JavaIncDecOperation(JavaSchema schema, JavaExpression op1) {
        super(op1);
        checkVarRefExpr(op1);
        assert (schema.isNumeric(op1.getResultType()));
    }

    public JavaType getResultType() {
        return operands[0].getResultType();
    }

    public boolean isConstant() {
        return false;
    }
}
