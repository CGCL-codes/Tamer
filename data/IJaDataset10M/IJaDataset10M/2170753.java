package com.volantis.xml.expression.impl.jxpath.compiler;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.impl.jxpath.JXPathExpression;
import our.apache.commons.jxpath.ri.EvalContext;
import our.apache.commons.jxpath.ri.compiler.Expression;
import our.apache.commons.jxpath.ri.model.NodePointer;

/**
 * This wrapper converts computed expression result, if it is an instance of
 * {@link Value}, to instance of Java class
 */
public class ConvertingToJavaObjectExpressionWrapper extends Expression {

    /**
     * Wrapped expression
     */
    private Expression internalExpression;

    public ConvertingToJavaObjectExpressionWrapper(Expression expression) {
        internalExpression = expression;
    }

    public Object compute(EvalContext evalContext) {
        return convertValue(internalExpression.compute(evalContext));
    }

    public boolean computeContextDependent() {
        return internalExpression.computeContextDependent();
    }

    public Object computeValue(EvalContext evalContext) {
        return convertValue(internalExpression.computeValue(evalContext));
    }

    private Object convertValue(Object value) {
        if (value instanceof EvalContext) {
            value = ((EvalContext) value).getSingleNodePointer();
        }
        if (value instanceof NodePointer) {
            value = ((NodePointer) value).getValue();
        }
        return JXPathExpression.asJavaValue(value);
    }
}
