package org.uc3m.verbus.bpa.impl;

import org.uc3m.verbus.bpa.ExprBinaryOpBool;
import org.uc3m.verbus.bpa.Expression;
import org.uc3m.verbus.bpa.ExpressionInteger;

public abstract class ExprBinaryOpBoolImpl implements ExprBinaryOpBool {

    protected ExpressionInteger left;

    protected ExpressionInteger right;

    public ExpressionInteger getLeftOperand() {
        return left;
    }

    public ExpressionInteger getRightOperand() {
        return right;
    }

    public void setLeftOperand(ExpressionInteger expression) {
        left = expression;
    }

    public void setRightOperand(ExpressionInteger expression) {
        right = expression;
    }

    public short getDataType() {
        return Expression.DT_BOOLEAN;
    }
}
