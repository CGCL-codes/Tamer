package com.bluemarsh.jswat.expr;

import com.bluemarsh.jswat.parser.java.node.Token;

/**
 * Class MultBinaryOperatorNode implements the multiplication binary
 * operator (*).
 *
 * @author  Nathan Fiedler
 */
class MultBinaryOperatorNode extends BinaryOperatorNode {

    /**
     * Constructs a MultBinaryOperatorNode.
     *
     * @param  node  lexical token.
     */
    public MultBinaryOperatorNode(Token node) {
        super(node);
    }

    /**
     * Returns the value of this node.
     *
     * @param  context  evaluation context.
     * @return  a Number.
     * @throws  EvaluationException
     *          if an error occurred during evaluation.
     */
    protected Object eval(EvaluationContext context) throws EvaluationException {
        Object o1 = getChild(0).evaluate(context);
        Object o2 = getChild(1).evaluate(context);
        if (isNumber(o1) && isNumber(o2)) {
            if (isFloating(o1) || isFloating(o2)) {
                if (isDouble(o1) || isDouble(o2)) {
                    double d1 = getDoubleValue(o1);
                    double d2 = getDoubleValue(o2);
                    return new Double(d1 * d2);
                } else {
                    float f1 = getFloatValue(o1);
                    float f2 = getFloatValue(o2);
                    return new Float(f1 * f2);
                }
            } else {
                if (isLong(o1) || isLong(o2)) {
                    long l1 = getLongValue(o1);
                    long l2 = getLongValue(o2);
                    return new Long(l1 * l2);
                } else {
                    int i1 = getIntValue(o1);
                    int i2 = getIntValue(o2);
                    return new Integer(i1 * i2);
                }
            }
        } else {
            throw new EvaluationException(Bundle.getString("error.oper.num"), getToken());
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
        return 6;
    }
}
