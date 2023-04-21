package edu.gatech.cc.concolic.expression;

public class LongConstant extends SymbolicConstant implements LongExpression {

    public LongConstant(long value) {
        super(value);
    }

    public void accept(SymbolicExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
