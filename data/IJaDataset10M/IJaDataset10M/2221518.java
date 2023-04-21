package jopt.csp.variable;

/**
 * Base interface for numeric-based expressions
 */
public abstract interface CspNumExpr {

    /**
     * Returns the name of this expression
     * @return	name of this expression
     */
    public String getName();

    /**
     * Sets the name of this expression
     * @param name	new name for this expression
     */
    public void setName(String name);

    /**
     * Returns a constraint restricting this expression to a value
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be equal to val
     */
    public CspConstraint eq(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to values below
     * a given maximum
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be less than val
     */
    public CspConstraint lt(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to values below
     * and including a given maximum
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be less than or equal to val
     */
    public CspConstraint leq(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to values above
     * a given minimum
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be greater than val
     */
    public CspConstraint gt(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to values above
     * and including a given minimum
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be greater than or equal to val
     */
    public CspConstraint geq(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to all values
     * not equivalent to supplied value
     * @param	val		expression to constain this expression to
     * @return			constraint constraining this expression to be not equal to val
     */
    public CspConstraint neq(CspNumExpr val);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min          value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(CspNumExpr min, boolean minExclusive, CspNumExpr max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to be between or equal
     * min and max.
     * 
     * @param  min	value that this expression must be greater than
     * @param  max	value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(CspNumExpr min, CspNumExpr max);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min	value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max	value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(CspGenericNumConstant min, boolean minExclusive, CspGenericNumConstant max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min	value that this expression must be greater than
     * @param  max	value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(CspGenericNumConstant min, CspGenericNumConstant max);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * from a min to max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          start of values that this expression may not contain
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspNumExpr min, boolean minExclusive, CspNumExpr max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspNumExpr min, CspNumExpr max);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * from a min to max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          start of values that this expression may not contain
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspGenericNumConstant min, boolean minExclusive, CspGenericNumConstant max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(CspGenericNumConstant min, CspGenericNumConstant max);

    /**
     * Returns true if this expression's domain is bound to a value
     * @return true if expression domain is bound to a single value
     */
    public boolean isBound();
}
