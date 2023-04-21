package com.ibm.tuningfork.infra.stream.expression.base;

import com.ibm.tuningfork.infra.stream.expression.types.ExpressionType;

/**
 * A special case of Expression whose value is known to be numeric.  Adds methods to return numeric
 *  scalars.  Warning: default implementations exist for getIntValue(), getLongValue(), and getDoubleValue() defined
 *  in terms of each other.  At least one must be overridden to avoid infinite regress.  More specifically:  expressions
 *  whose natural type is int should override getIntValue, then define getLongValue and getValue in terms of getIntValue.
 *  Expressions whose natural type is long should override getLongValue, then define getValue in terms of getLongValue.
 *  Expressions whose natural type is double should override getDoubleValue, then define getValue in terms of getDoubleValue.
 */
public abstract class NumericExpression extends Expression {

    /**
     * Subclass-callable constructor permitting type to be set later (typically because the Unit
     *   must be determined by the subclass)
     * @param dummy ignored (in the signature so that there is no default constructor, therefore forcing
     *   the subclass to carefully consider type initialization)
     */
    protected NumericExpression(boolean dummy) {
    }

    /**
     * Subclass-callable constructor specifying the type
     * @param type the ExpressionType, which is constrained to be numeric
     */
    protected NumericExpression(ExpressionType type) {
        setType(type);
    }

    /**
     * Return value as a double (perhaps lossily).  Override this method if a non-lossy getDoubleValue()
     *   is possible and a non-lossy getLongValue() is not
     * @param context the StreamContext in which this expression is being evaluated
     * @return the value as a double
     * @throws MissingValueException if this expression is null or any of the subexpressions needed to evaluate it are null
     */
    public double getDoubleValue(StreamContext context) throws MissingValueException {
        return getLongValue(context);
    }

    /**
     * Return value as an int (perhaps lossily).  Override iff int is the actual type.
     * @param context the StreamContext in which this expression is being evaluated
     * @return the value as an int
     * @throws MissingValueException if this expression is null or any of the subexpressions needed to evaluate it are null
     */
    public int getIntValue(StreamContext context) throws MissingValueException {
        return (int) getLongValue(context);
    }

    /**
     * Return value as a long (perhaps lossily).  Override this method if a non-lossy getLongValue() is
     *   possible.
     * @param context the StreamContext in which this expression is being evaluated
     * @return the value as a long
     * @throws MissingValueException if this expression is null or any of the subexpressions needed to evaluate it are null
     */
    public long getLongValue(StreamContext context) throws MissingValueException {
        return (long) getDoubleValue(context);
    }

    public abstract NumericExpression resolve(Expression[] arguments, int depth);

    protected void setType(ExpressionType type) {
        checkAssertion(type.isNumeric(), "Wrong type for numeric expression.  Supplied type was " + type);
        super.setType(type);
    }
}
