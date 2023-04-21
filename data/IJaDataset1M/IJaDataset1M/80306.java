package org.qtitools.qti.node.expression.operator;

import org.qtitools.qti.node.expression.ExpressionParent;
import org.qtitools.qti.node.expression.RandomExpression;
import org.qtitools.qti.value.BaseType;
import org.qtitools.qti.value.ListValue;
import org.qtitools.qti.value.NullValue;
import org.qtitools.qti.value.Value;

/**
 * The random operator takes A sub-expression with A multiple or ordered container value and any base-type.
 * The result is A single value randomly selected from the container. The result has the same base-type
 * as the sub-expression but single cardinality.
 * If the sub-expression is NULL then the result is also NULL.
 *
 * @see org.qtitools.qti.value.Cardinality
 * @see org.qtitools.qti.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class Random extends RandomExpression {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "random";

    /**
	 * Constructs expression.
	 *
	 * @param parent parent of this expression
	 */
    public Random(ExpressionParent parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    @Override
    protected Long getSeedAttributeValue() {
        return null;
    }

    @Override
    public BaseType[] getRequiredBaseTypes(int index) {
        return getRequiredSameBaseTypes(index, true);
    }

    @Override
    public BaseType[] getProducedBaseTypes() {
        if (getChildren().size() == 1) return getChildren().get(0).getProducedBaseTypes();
        return super.getProducedBaseTypes();
    }

    @Override
    protected Value evaluateSelf(int depth) {
        if (isAnyChildNull()) return new NullValue();
        java.util.Random randomGenerator = getRandomGenerator(depth);
        int randomIndex = randomGenerator.nextInt(((ListValue) getFirstChild().getValue()).size());
        Value value = ((ListValue) getFirstChild().getValue()).get(randomIndex);
        if (value == null || value.isNull()) return new NullValue();
        return value;
    }
}
