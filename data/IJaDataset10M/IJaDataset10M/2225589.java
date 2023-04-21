package uk.org.ogsadai.expression.arithmetic;

/**
 * Plus expression.
 *
 * @author The OGSA-DAI Project Team.
 */
public class Plus extends BinaryExpression {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /**
     * Constructs a new expression with the given operands.
     * 
     * @param child1
     *            left hand operand
     * @param child2
     *            right hand operand
     */
    public Plus(ArithmeticExpression child1, ArithmeticExpression child2) {
        super(child1, child2);
    }

    /**
     * Constructs a copy of the given plus expression with the given operands.
     * 
     * @param child1
     *            left hand operand
     * @param child2
     *            right hand operand
     * @param plus
     *            expression to copy
     */
    public Plus(ArithmeticExpression child1, ArithmeticExpression child2, Plus plus) {
        super(child1, child2);
        mType = plus.mType;
        mEval = plus.mEval;
    }

    /**
     * {@inheritDoc}
     */
    public Number localEvaluate(Number value1, Number value2) {
        return mEval.plus(value1, value2);
    }

    /**
     * {@inheritDoc}
     */
    public void accept(ArithmeticExpressionVisitor visitor) {
        visitor.visitPlus(this);
    }

    /**
     * {@inheritDoc}
     */
    protected void printOperator(StringBuffer buf) {
        buf.append("+");
    }
}
