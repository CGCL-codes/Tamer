package org.maverickdbms.basic;

public interface MathInterface {

    /**
    * Gets the absolute value for the number.
    * @param result value to store the absolute number in
    * @param a the number
    * @return the absolute value
    */
    public MaverickString ABS(MaverickString result, ConstantString a);

    public MaverickString add(MaverickString result, ConstantString a, ConstantString b);

    /**
    * Returns the bitwise AND of the 2 numbers
    * @param result - the number to store the result
    * @param a - the first operand
    * @param b - the second operand
    * @return the bitwise AND
    */
    public MaverickString BITAND(MaverickString result, ConstantString a, ConstantString b);

    /**
    * Returns the cosine of the number
    * @param result - the number to store the result
    * @param a - the angle, in radians
    * @return the cosine
    */
    public MaverickString COS(MaverickString result, ConstantString a);

    public MaverickString divide(MaverickString result, ConstantString a, ConstantString b);

    /**
    * Returns the <I>e</I> to the power of the number
    * @param result - the number to store the result
    * @param a - the number
    * @return the result
    */
    public MaverickString EXP(MaverickString result, ConstantString a);

    /**
    * Determines whether a is greater than or equal to b.
    * @param a the first number to be compared
    * @param b the second number to be compared
    * @return true if a is greater than or equal to b
    */
    public boolean GE(ConstantString a, ConstantString b);

    /**
    * Returns the current PRECISION value
    */
    public int getPrecision();

    /**
    * Determines whether a is greater than b.
    * @param a the first number to be compared
    * @param b the second number to be compared
    * @return true if a is greater than b
    */
    public boolean GT(ConstantString a, ConstantString b);

    /**
    * Returns the integer portion of the number
    * @param a the number to store the result in
    * @return the truncated number
    */
    public MaverickString INT(MaverickString result, ConstantString a);

    /**
    * Determines whether a is less than or equal to b.
    * @param a the first number to be compared
    * @param b the second number to be compared
    * @return true if a is less than or equal to b
    */
    public boolean LE(ConstantString a, ConstantString b);

    /** 
    * Returns the natural logarithm of the number
    * @param result the number to store the result
    * @param a a number greater than zero
    * @return the natural logarithm
    */
    public MaverickString LN(MaverickString result, ConstantString a);

    /**
    * Determines whether a is less than b.
    * @param a the first number to be compared
    * @param b the second number to be compared
    * @return true if a is less than b
    */
    public boolean LT(ConstantString a, ConstantString b);

    /**
    * Returns the modulus of a divided by b
    * @param a the numerator
    * @param b the denominator
    * @param result the modulus 
    * @return the modulus 
    */
    public MaverickString MOD(MaverickString result, ConstantString a, ConstantString b);

    public MaverickString multiply(MaverickString result, ConstantString a, ConstantString b);

    public void PRECISION(ConstantString val);

    /**
    * Returns the number raised to the power.
    * @param result the number to store the result
    * @param a the number
    * @param b the power to be raised
    * @return the result
    */
    public MaverickString PWR(MaverickString result, ConstantString a, ConstantString b);

    public MaverickString RND(MaverickString result, ConstantString range);

    /**
    * Returns the sine of the number
    * @param result - the number to store the result
    * @param a - the angle, in radians
    * @return the sine
    */
    public MaverickString SIN(MaverickString result, ConstantString a);

    public MaverickString SQRT(MaverickString result, ConstantString a);

    public MaverickString subtract(MaverickString result, ConstantString a, ConstantString b);

    /**
    * Returns the tangent of the number
    * @param result - the number to store the result
    * @param a - the angle, in radians
    * @return the tangent
    */
    public MaverickString TAN(MaverickString result, ConstantString a);
}
