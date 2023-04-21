package com.newisys.langschema.constraint;

public final class ConsAnd extends ConsBitwiseOperation {

    public ConsAnd(ConsExpression op1, ConsExpression op2) {
        super(op1, op2);
    }

    public String toSourceString() {
        return "(" + operands[0] + " & " + operands[1] + ")";
    }

    public void accept(ConsConstraintExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
