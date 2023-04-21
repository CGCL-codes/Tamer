package com.bluemarsh.jswat.expr;

import com.bluemarsh.jswat.parser.java.node.Token;

/**
 * Class LtEqOperatorNode implements the less than operator (<).
 *
 * @author  Nathan Fiedler
 */
class LtEqOperatorNode extends BinaryOperatorNode {

    /**
     * Constructs a LtEqOperatorNode.
     *
     * @param  node  lexical token.
     */
    public LtEqOperatorNode(Token node) {
        super(node);
    }

    /**
     * Returns the value of this node.
     *
     * @param  context  evaluation context.
     * @return  a Boolean.
     * @throws  EvaluationException
     *          if an error occurred during evaluation.
     */
    protected Object eval(EvaluationContext context) throws EvaluationException {
        Object o1 = getChild(0).evaluate(context);
        Object o2 = getChild(1).evaluate(context);
        if (isNumber(o1) || isNumber(o2)) {
            if (isNumber(o1) && isNumber(o2)) {
                if (isFloating(o1) || isFloating(o2)) {
                    if (isDouble(o1) || isDouble(o2)) {
                        double d1 = getDoubleValue(o1);
                        double d2 = getDoubleValue(o2);
                        long l1 = Double.doubleToLongBits(d1);
                        long l2 = Double.doubleToLongBits(d2);
                        return d1 < d2 || l1 == l2 ? Boolean.TRUE : Boolean.FALSE;
                    } else {
                        float f1 = getFloatValue(o1);
                        float f2 = getFloatValue(o2);
                        int i1 = Float.floatToIntBits(f1);
                        int i2 = Float.floatToIntBits(f2);
                        return f1 < f2 || i1 == i2 ? Boolean.TRUE : Boolean.FALSE;
                    }
                } else {
                    if (isLong(o1) || isLong(o2)) {
                        long l1 = getLongValue(o1);
                        long l2 = getLongValue(o2);
                        return l1 <= l2 ? Boolean.TRUE : Boolean.FALSE;
                    } else {
                        int i1 = getIntValue(o1);
                        int i2 = getIntValue(o2);
                        return i1 <= i2 ? Boolean.TRUE : Boolean.FALSE;
                    }
                }
            } else {
                throw new EvaluationException(Bundle.getString("error.oper.equals.type"), getToken());
            }
        } else {
            throw new EvaluationException(Bundle.getString("error.oper.comp.type"), getToken());
        }
    }

    /**
     * Returns this operator's precedence value. The lower the value the
     * higher the precedence. The values are equivalent to those
     * described in the Java Language Reference book (2nd ed.), p 106.
     *
     * @return  precedence value.
     */
    public int precedence() {
        return 9;
    }
}
