package nz.org.venice.parser.expression;

import nz.org.venice.parser.*;
import nz.org.venice.quote.*;
import nz.org.venice.util.Locale;

/**
 * An expression which divides two sub-expressions.
 *
 * @author Andrew Leppard
 */
public class DivideExpression extends ArithmeticExpression {

    public DivideExpression(Expression left, Expression right) {
        super(left, right);
    }

    public double evaluate(Variables variables, QuoteBundle quoteBundle, Symbol symbol, int day) throws EvaluationException {
        double right = getChild(1).evaluate(variables, quoteBundle, symbol, day);
        if (right != 0.0D) return getChild(0).evaluate(variables, quoteBundle, symbol, day) / right; else {
            EvaluationException e = EvaluationException.DIVIDE_BY_ZERO_EXCEPTION;
            e.setMessage(this, "", right);
            throw e;
        }
    }

    public Expression simplify() {
        Expression simplified = super.simplify();
        if (simplified.equals(this)) {
            NumberExpression left = (simplified.getChild(0) instanceof NumberExpression ? (NumberExpression) simplified.getChild(0) : null);
            NumberExpression right = (simplified.getChild(1) instanceof NumberExpression ? (NumberExpression) simplified.getChild(1) : null);
            if (left != null && left.equals(0.0D)) return new NumberExpression(0.0D, simplified.getType()); else if (right != null && right.equals(1.0D)) return simplified.getChild(0); else if (simplified.getChild(0).equals(simplified.getChild(1))) return new NumberExpression(1.0D, simplified.getType());
        }
        return simplified;
    }

    public String toString() {
        return super.toString("/");
    }

    public Object clone() {
        return new DivideExpression((Expression) getChild(0).clone(), (Expression) getChild(1).clone());
    }
}
