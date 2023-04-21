package de.lodde.jnumwu.formula;

import de.lodde.jnumwu.core.Value;

/**
 * Represnts the sinus function.
 * @author Georg Lodde
 */
public class ArcSinus extends Unary {

    public ArcSinus(Expression expression) {
        super(expression);
    }

    @Override
    public Expression newUnary(Expression expression) {
        return new Sinus(expression);
    }

    @Override
    public boolean hasConstant() {
        return false;
    }

    @Override
    public Expression evaluate() {
        Expression evaluate = expression.evaluate();
        if (evaluate instanceof Constant) {
            if (evaluate == NEUTRAL) return NEUTRAL;
            Constant c = (Constant) evaluate;
            Value value = c.getValue();
            double doubleValue = value.doubleValue();
            double asin = convertRadDegee(Math.asin(doubleValue));
            Value newValue = new Value(asin, value.getUnits());
            return new Constant(newValue);
        }
        if (evaluate == expression) return this;
        return new Sinus(evaluate);
    }

    @Override
    public boolean isBraceless(Expression parent) {
        return true;
    }

    @Override
    public boolean isAssociative(Expression parent) {
        return true;
    }

    @Override
    protected String getOperation() {
        return "asin";
    }
}
