package org.metaphile.type;

/**
 * Custom data type for storing rational numbers. Rational numbers are
 * fractions - they have a numerator and denominator. eg 100/1, 2/3
 * 
 * @author stuart
 */
public class Rational extends Number {

    private static final long serialVersionUID = 4518740731544897946L;

    private long numerator;

    private long denominator;

    /**
     * Constructs the Rational number specifying the numerator and denominator
     * 
     * @param numerator the numerator
     * @param denominator the denominator
     */
    public Rational(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public double doubleValue() {
        return ((double) this.numerator / (double) this.denominator);
    }

    @Override
    public float floatValue() {
        return ((float) this.numerator / (float) this.denominator);
    }

    @Override
    public int intValue() {
        return ((int) this.numerator / (int) this.denominator);
    }

    @Override
    public long longValue() {
        return this.numerator / this.denominator;
    }

    /**
     * Returns the value of this Rational number as a traditional fraction
     * String. eg 1/5
     * 
     * @return the value of this Rational number as a fractional string
     */
    public String toFractionString() {
        return this.numerator + "/" + this.denominator;
    }

    @Override
    public String toString() {
        if (this.denominator == 0) {
            return this.toFractionString();
        } else if (this.numerator % this.denominator == 0) {
            return Integer.toString(this.intValue());
        } else {
            return Float.toString(this.floatValue());
        }
    }
}
