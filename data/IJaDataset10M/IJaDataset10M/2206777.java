package org.jrcaf.rule.internal.bool;

import org.jrcaf.rule.IExpressionDefinition;
import org.jrcaf.rule.IRule;
import org.jrcaf.rule.bool.IExpression;

/**
 * NOT operator expression.
 */
public class NOT implements IExpression {

    private final NOTDefinition definition;

    private IExpression expression;

    /**
    * Creates a new intance of an NOT expression.
    * @param aObject The object the expression is assiciated with.
    * @param aRule The enclosing rule.
    * @param aDefinition The definition.
    */
    public NOT(Object aObject, NOTDefinition aDefinition, IRule aRule) {
        definition = aDefinition;
        IExpressionDefinition expressionDefinition = definition.getExpressionDefinition();
        expression = expressionDefinition.create(aObject, aRule);
    }

    /**
    * @see org.jrcaf.rule.bool.IExpression#evaluate()
    */
    public boolean evaluate() {
        return expression.evaluate() == false;
    }

    /**
    * @see org.jrcaf.rule.bool.IExpression#remove()
    */
    public void remove() {
        expression.remove();
    }
}
