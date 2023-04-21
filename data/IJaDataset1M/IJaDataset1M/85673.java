package com.strategicgains.jbel.builder;

import com.strategicgains.jbel.expression.Expression;

/**
 * An AbstractExpressionBuilder is the root object for specialized expression builders.  Essentially,
 * its only function is to contain the underlying expression that is being built.
 * 
 * @author Todd Fredrich
 * @since Aug 26, 2005
 * @version $Revision: 1.9 $
 */
public abstract class AbstractExpressionBuilder {

    private Expression expression;

    public AbstractExpressionBuilder() {
        super();
    }

    public Expression getExpression() {
        return expression;
    }

    protected void setExpression(Expression expression) {
        this.expression = expression;
    }
}
