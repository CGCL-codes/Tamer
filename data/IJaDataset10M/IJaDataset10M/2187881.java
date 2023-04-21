package org.inexact.expression.operator;

import org.inexact.expression.FuzzyExpression;

public abstract class FuzzyOperator extends FuzzyExpression {

    protected FuzzyExpression[] expressions = null;

    public FuzzyOperator(FuzzyExpression expr1, FuzzyExpression expr2) {
        expressions = new FuzzyExpression[2];
        expressions[0] = expr1;
        expressions[1] = expr2;
    }

    public abstract double evaluate();
}
