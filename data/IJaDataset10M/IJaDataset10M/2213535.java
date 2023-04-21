package net.sf.ngrease.core.metalanguage;

import net.sf.ngrease.core.ast.Element;

/**
 * TODO maybe this shouldn't be a selector, the symbol check should be made
 * outside
 */
public class ExpressionBuilderFixedExpressionImpl implements ExpressionBuilderSelector, ExpressionBuilder {

    private final String symbol;

    private final Expression expression;

    public ExpressionBuilderFixedExpressionImpl(String symbol, Expression expression) {
        this.symbol = symbol;
        this.expression = expression;
    }

    public boolean isUnconditional() {
        return false;
    }

    public ExpressionBuilder selectExpressionBuilder(Element expressionElement) {
        if (symbol.equals(expressionElement.getSymbol())) {
            return this;
        } else {
            return null;
        }
    }

    public Expression buildExpression(Element expressionElement) {
        return expression;
    }
}
