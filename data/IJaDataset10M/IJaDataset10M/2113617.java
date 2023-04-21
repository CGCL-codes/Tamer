package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;

public class TreeAppendExpressionBuilder implements ExpressionBuilder {

    public Expression buildExpression(Element expressionElement) {
        Expression expression = new TreeAppendExpression(expressionElement, expressionElement.getChildren());
        return expression;
    }
}
