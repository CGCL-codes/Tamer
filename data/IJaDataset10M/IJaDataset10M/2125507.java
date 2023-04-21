package org.da.expressionj.expr;

import org.da.expressionj.model.Expression;

/**
 * Represent a "sqrt" expression.
 *
 * @version 0.9.2
 */
public class ExprSQRT extends AbstractUnaryExpression {

    public ExprSQRT() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExprSQRT sqrt = new ExprSQRT();
        sqrt.setExpression(expr);
        sqrt.block = block;
        return sqrt;
    }

    @Override
    public String getExpressionName() {
        return "sqrt";
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
    public float evalAsFloat() {
        return (float) Math.sqrt(expr.evalAsFloat());
    }

    @Override
    public Object eval() throws ArithmeticException {
        Object o = expr.eval();
        if (o instanceof Float) {
            return ((float) Math.sqrt(((Float) o).floatValue()));
        } else if (o instanceof Double) {
            return ((float) Math.sqrt(((Double) o).floatValue()));
        } else if (o instanceof Integer) {
            return ((float) Math.sqrt(((Integer) o).intValue()));
        } else {
            throw new ArithmeticException("SQRT Expression use non numeric elements");
        }
    }

    @Override
    public short getResultType() {
        return TYPE_FLOAT;
    }

    @Override
    public short getResultStructure() {
        return STRUCT_SCALAR;
    }
}
