package org.da.expressionj.expr;

/** Represent a "Less or Equal than" expression.
 *
 * @version 0.9.2
 */
public class ExprLT extends AbstractAryExpression {

    public ExprLT() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExprLT lt = new ExprLT();
        lt.setExpression1(expr1);
        lt.setExpression2(expr2);
        lt.block = block;
        return lt;
    }

    @Override
    public String getExpressionName() {
        return "<";
    }

    @Override
    public final boolean evalAsBoolean() {
        if ((expr1.getResultType() == TYPE_INTEGER) && (expr1.getResultType() == TYPE_INTEGER)) {
            int value2 = expr1.evalAsInt();
            int value1 = expr2.evalAsInt();
            return (value1 < value2);
        } else {
            float value2 = expr1.evalAsFloat();
            float value1 = expr2.evalAsFloat();
            return (value1 < value2);
        }
    }

    @Override
    public Object eval() throws ArithmeticException {
        Object o1 = expr2.eval();
        Object o2 = expr1.eval();
        if ((o1 instanceof Number) && (o2 instanceof Number)) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            return n1.floatValue() < n2.floatValue();
        } else {
            throw new ArithmeticException("Comparison Expression use non numeric elements");
        }
    }

    @Override
    public short getResultType() {
        return TYPE_BOOL;
    }

    @Override
    public short getResultStructure() {
        return STRUCT_SCALAR;
    }
}
