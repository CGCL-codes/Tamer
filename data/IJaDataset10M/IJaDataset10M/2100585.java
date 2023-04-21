package jopt.csp.variable;

/**
 * Interface for generic float expressions
 */
public interface CspGenericFloatExpr extends CspGenericFloatCast, CspFloatExpr {

    /**
	 * Returns a numeric expression from the internal array
	 * 
	 * @param offset  Offset of expression in internal expression array
	 * @return	float expression indexed at the specified offset
	 */
    public CspFloatCast getExpression(int offset);

    /**
	 * Returns the largest maximal value of all variables in the internal array
	 * @return	float value of the largest maximum value of all wrapped float expressions
	 */
    public float getLargestMax();

    /**
	 * Returns the largest maximal value of all variables in the internal array within
	 * start and end offsets
	 * @return	float value of the largest maximum value of all wrapped float expressions
	 */
    public float getLargestMax(int start, int end);

    /**
	 * Returns the smallest maximal value of all variables in the internal array
	 * @return	float value of the smallest maximum value of all wrapped float expressions
	 */
    public float getSmallestMax();

    /**
	 * Returns the smallest maximal value of all variables in the internal array within
	 * start and end offsets
	 * 
	 * @return	float value of the smallest maximum value of all wrapped float expressions
	 */
    public float getSmallestMax(int start, int end);

    /**
	 * Returns the largest minimal value of all variables in the internal array
	 * @return	float value of the largest minimum value of all wrapped float expressions
	 */
    public float getLargestMin();

    /**
	 * Returns the largest minimal value of all variables in the internal array within
	 * start and end offsets
	 * @return	float value of the largest minimum value of all wrapped float expressions
	 */
    public float getLargestMin(int start, int end);

    /**
	 * Returns the smallest minimal value of all variables in the internal array
	 * @return	float value of the smallest minimum value of all wrapped float expressions
	 */
    public float getSmallestMin();

    /**
	 * Returns the smallest minimal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	offset of the beginning of the range to 
	 * @return	float value of the smallest minimum value of all wrapped float expressions
	 */
    public float getSmallestMin(int start, int end);

    /**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	f	generic constant float to add to this expression
	 * @return	generic float expression representing this + f
	 */
    public CspGenericFloatExpr add(CspGenericFloatConstant f);

    /**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic constant double to add to this expression
	 * @return	generic double expression representing this + d
	 */
    public CspGenericDoubleExpr add(CspGenericDoubleConstant d);

    /**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * 
	 * @param	f	generic constant float to subtract from this expression
	 * @return	generic float expression representing this - f
	 */
    public CspGenericFloatExpr subtract(CspGenericFloatConstant f);

    /**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic constant double to subtract from this expression
	 * @return	generic double expression representing this - d
	 */
    public CspGenericDoubleExpr subtract(CspGenericDoubleConstant d);

    /**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * 
	 * @param	f	generic constant float to multiply by this expression
	 * @return	generic float expression representing this * f
	 */
    public CspGenericFloatExpr multiply(CspGenericFloatConstant f);

    /**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic constant double to multiply by this expression
	 * @return	generic double expression representing this * d
	 */
    public CspGenericDoubleExpr multiply(CspGenericDoubleConstant d);

    /**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * 
	 * @param	f	generic constant float to divide this expression by
	 * @return	generic float expression representing this / f
	 */
    public CspGenericFloatExpr divide(CspGenericFloatConstant f);

    /**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic constant double to divide this expression by
	 * @return	generic double expression representing this / d
	 */
    public CspGenericDoubleExpr divide(CspGenericDoubleConstant d);

    /**
	 * Returns a constraint restricting this expression to a value
	 */
    public CspConstraint eq(CspGenericFloatConstant val);

    /**
	 * Returns a constraint restricting this expression to values below
	 * and including a given maximum
	 */
    public CspConstraint leq(CspGenericFloatConstant val);

    /**
	 * Returns a constraint restricting this expression to values above
	 * and including a given minimum
	 */
    public CspConstraint geq(CspGenericFloatConstant val);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(float min, boolean minExclusive, float max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(float min, float max);

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
    public CspConstraint notBetween(float min, boolean minExclusive, float max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(float min, float max);
}
