package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;
import net.sf.ngrease.core.ast.ElementListHelper;

public class ChildExistsExpressionBuilder implements ExpressionBuilder {

    public Expression buildExpression(Element expressionElement) {
        Element ofElement = ElementListHelper.getTheOnlyChildOfChildBySymbol(expressionElement.getChildren(), "of");
        Element symbolElement = ElementListHelper.getTheOnlyChildOfChildBySymbol(expressionElement.getChildren(), "symbol");
        Expression expression = new ChildExistsExpression(expressionElement, ofElement, symbolElement);
        return expression;
    }
}
