package org.da.expressionj.expr;

import org.da.expressionj.model.Expression;

/**
 * Represent a "abs" expression.
 *
 * @version 0.9.2
 */
public class ExprABS extends AbstractUnaryNumericExpression {

    public ExprABS() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExprABS abs = new ExprABS();
        abs.setExpression(expr);
        abs.block = block;
        return abs;
    }

    @Override
    public String getExpressionName() {
        return "abs";
    }

    @Override
    public void setExpression(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Expression getExpression() {
        return expr;
    }

    @Override
    public final int evalAsInt() {
        return Math.abs(expr.evalAsInt());
    }

    @Override
    public final float evalAsFloat() {
        return Math.abs(expr.evalAsFloat());
    }

    @Override
    public Object eval() throws ArithmeticException {
        Object o = expr.eval();
        if (o instanceof Float) {
            return (Math.abs(((Float) o).floatValue()));
        } else if (o instanceof Double) {
            return (Math.abs(((Double) o).floatValue()));
        } else if (o instanceof Integer) {
            return (Math.abs(((Integer) o).intValue()));
        } else {
            throw new ArithmeticException("ABS Expression use non numeric elements");
        }
    }

    @Override
    public short getResultStructure() {
        return STRUCT_SCALAR;
    }
}
