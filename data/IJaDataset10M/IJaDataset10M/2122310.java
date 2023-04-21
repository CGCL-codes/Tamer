package org.qtitools.qti.node.expression.operator;

import org.qtitools.qti.node.expression.AbstractExpression;
import org.qtitools.qti.node.expression.ExpressionParent;
import org.qtitools.qti.value.BooleanValue;
import org.qtitools.qti.value.NullValue;
import org.qtitools.qti.value.NumberValue;
import org.qtitools.qti.value.Value;

/**
 * The lte operator takes two sub-expressions which must both have single cardinality and have A numerical
 * base-type. The result is A single boolean with A value of true if the first expression is numerically
 * less than or equal to the second and false if it is greater than the second.
 * If either sub-expression is NULL then the operator results in NULL.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class Lte extends AbstractExpression {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "lte";

    /**
	 * Constructs expression.
	 *
	 * @param parent parent of this expression
	 */
    public Lte(ExpressionParent parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    @Override
    protected Value evaluateSelf(int depth) {
        if (isAnyChildNull()) return new NullValue();
        double firstNumber = ((NumberValue) getFirstChild().getValue()).doubleValue();
        double secondNumber = ((NumberValue) getSecondChild().getValue()).doubleValue();
        return new BooleanValue(firstNumber <= secondNumber);
    }
}
