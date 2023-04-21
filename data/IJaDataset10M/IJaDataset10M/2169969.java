package jopt.csp.variable;

/**
 * Interface implemented by double precision floating point numeric expressions.  
 * The domain of this type of object is computed and direct changes to it will have no
 * effect.
 */
public interface CspDoubleExpr extends CspDoubleCast {

    /**
	 * Returns minimal value of expression
	 * @return minimal value of expression
	 */
    public double getMin();

    /**
	 * Returns maximum value of expression
	 * @return maximim value of expressioni
	 */
    public double getMax();

    /**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param d		value added to this for creation of 
	 * @return		CspDoubleExpr representing this + d
	 */
    public CspDoubleExpr add(double d);

    /**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param expr	double expression to be added to this
	 * @return		CspDoubleExpr representing this + expr
	 */
    public CspDoubleExpr add(CspDoubleCast expr);

    /**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param d 	double value to be subtracted from this
	 * @return		double representing this - d
	 */
    public CspDoubleExpr subtract(double d);

    /**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param expr	double expression to be subtracted from this 
	 * @return		CspDoubleExpr representing this - expr
	 */
    public CspDoubleExpr subtract(CspDoubleCast expr);

    /**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param d 	double to be multiplied by this
	 * @return		CspDoubleExpr representing this * d
	 */
    public CspDoubleExpr multiply(double d);

    /**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param expr	double expression to be multiplied by this
	 * @return		CspDoubleExpr representing this * expr
	 */
    public CspDoubleExpr multiply(CspDoubleCast expr);

    /**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param d 	double value to divide this by
	 * @return		CspDoubleExpr representing this / d
	 */
    public CspDoubleExpr divide(double d);

    /**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param expr	double expression to divide this by
	 * @return		CspDoubleExpr representing this / expr
	 */
    public CspDoubleExpr divide(CspDoubleCast expr);

    /**
	 * Returns constraint restricting this expression to a value
	 * @param	val		value to constrain this expression to
	 * @return			CspConstraint constraining this to val
	 */
    public CspConstraint eq(double val);

    /**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		value to constrain this expression to be less than or equal to
	 * @return			CspConstraint constraining this to be less than or equal to val
	 */
    public CspConstraint leq(double val);

    /**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		value to constrain this expression to be greater than or equal to
	 * @return			CspConstraint constraining this to be greater than or equal to val
	 */
    public CspConstraint geq(double val);

    /**
	 * Returns constraint restricting this expression to a value
	 * @param	val		generic constants to constrain this expression to
	 * @return			CspConstraint constraining this to val
	 */
    public CspConstraint eq(CspGenericDoubleConstant val);

    /**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		generic constants to constrain this expression to less than or equal to
	 * @return			CspConstraint constraining this to be less than or equal to val
	 */
    public CspConstraint leq(CspGenericDoubleConstant val);

    /**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		generic constants to constrain this expression to greater than or equal to
	 * @return			CspConstraint constraining this to be greater than or equal to val
	 */
    public CspConstraint geq(CspGenericDoubleConstant val);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(double min, boolean minExclusive, double max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(double min, double max);

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
    public CspConstraint notBetween(double min, boolean minExclusive, double max, boolean maxExclusive);

    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(double min, double max);

    /**
     * Sets precision associated with this expression
     * @param	p		double representing the level of precision to associate with this expression
     */
    public void setPrecision(double p);

    /**
     * Returns precision associated with this expression
     * @return	precision associated with this expression
     */
    public double getPrecision();
}
