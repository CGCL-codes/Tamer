package nz.org.venice.parser.expression;

import nz.org.venice.parser.*;
import nz.org.venice.quote.*;

/**
 *
 * An expression which adds two sub-expressions.
 *
 */
public class AddExpression extends ArithmeticExpression {

    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }

    public double evaluate(Variables variables, QuoteBundle quoteBundle, Symbol symbol, int day) throws EvaluationException {
        return getChild(0).evaluate(variables, quoteBundle, symbol, day) + getChild(1).evaluate(variables, quoteBundle, symbol, day);
    }

    public Expression simplify() {
        Expression simplified = super.simplify();
        if (simplified.equals(this)) {
            NumberExpression left = (simplified.getChild(0) instanceof NumberExpression ? (NumberExpression) simplified.getChild(0) : null);
            NumberExpression right = (simplified.getChild(1) instanceof NumberExpression ? (NumberExpression) simplified.getChild(1) : null);
            if (right != null && right.equals(0.0D)) {
                return simplified.getChild(0);
            }
            if (left != null && left.equals(0.0D)) {
                return simplified.getChild(1);
            } else if (simplified.getChild(0).equals(simplified.getChild(1))) {
                return new MultiplyExpression(new NumberExpression(2.0D, simplified.getChild(0).getType()), simplified.getChild(0));
            }
        }
        return simplified;
    }

    public boolean equals(Object object) {
        if (object instanceof AddExpression) {
            AddExpression expression = (AddExpression) object;
            if (getChild(0).equals(expression.getChild(0)) && getChild(1).equals(expression.getChild(1))) return true;
            if (getChild(0).equals(expression.getChild(1)) && getChild(1).equals(expression.getChild(0))) return true;
        }
        return false;
    }

    public int hashCode() {
        Expression c1 = getChild(0);
        Expression c2 = getChild(1);
        assert c1 != null;
        assert c2 != null;
        return (c1.hashCode() ^ c2.hashCode());
    }

    public String toString() {
        return super.toString("+");
    }

    public Object clone() {
        return new AddExpression((Expression) getChild(0).clone(), (Expression) getChild(1).clone());
    }
}
