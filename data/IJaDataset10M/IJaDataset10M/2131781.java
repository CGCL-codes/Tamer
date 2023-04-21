package com.ibm.tuningfork.infra.stream.expression.base;

import com.ibm.tuningfork.infra.stream.expression.types.ExpressionType;

/**
 * A specialization of Expression for expressions of String type
 */
public abstract class StringExpression extends Expression {

    /**
     * A StringExpression
     */
    protected StringExpression() {
        super(ExpressionType.STRING);
    }

    /**
     * Every subclass must implement this
     * @param context the StreamContext in which this expression is being evaluated
     * @return the value as a String
     */
    public abstract String getStringValue(StreamContext context);

    public Object getValue(StreamContext context) {
        return getStringValue(context);
    }

    public abstract StringExpression resolve(Expression[] arguments, int depth);
}
