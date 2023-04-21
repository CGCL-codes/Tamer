package org.mov.parser.expression;

import org.mov.parser.*;
import org.mov.quote.*;

/**
 * An expression which performs boolean <code>and</code> on two 
 * sub-expressions.
 */
public class AndExpression extends LogicExpression {

    public AndExpression(Expression left, Expression right) {
        super(left, right);
    }

    public double evaluate(Variables variables, QuoteBundle quoteBundle, Symbol symbol, int day) throws EvaluationException {
        if (getChild(0).evaluate(variables, quoteBundle, symbol, day) >= TRUE_LEVEL && getChild(1).evaluate(variables, quoteBundle, symbol, day) >= TRUE_LEVEL) return TRUE; else return FALSE;
    }

    public Expression simplify() {
        super.simplify();
        NumberExpression left = (getChild(0) instanceof NumberExpression ? (NumberExpression) getChild(0) : null);
        NumberExpression right = (getChild(1) instanceof NumberExpression ? (NumberExpression) getChild(1) : null);
        if (left != null && left.getValue() >= TRUE_LEVEL) return getChild(1); else if (right != null && right.getValue() >= TRUE_LEVEL) return getChild(0); else if ((left != null && left.getValue() < TRUE_LEVEL) || (right != null && right.getValue() < TRUE_LEVEL)) return new NumberExpression(false); else if (getChild(0).equals(getChild(1))) return new NumberExpression(true); else return this;
    }

    public boolean equals(Object object) {
        if (object instanceof AndExpression) {
            AndExpression expression = (AndExpression) object;
            if (getChild(0).equals(expression.getChild(0)) && getChild(1).equals(expression.getChild(1))) return true;
            if (getChild(0).equals(expression.getChild(1)) && getChild(1).equals(expression.getChild(0))) return true;
        }
        return false;
    }

    public String toString() {
        return super.toString("and");
    }

    public Object clone() {
        return new AndExpression((Expression) getChild(0).clone(), (Expression) getChild(1).clone());
    }
}
