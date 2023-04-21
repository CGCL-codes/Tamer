package org.plazmaforge.framework.client.swt.data;

/**
 * Represents a fraction of two integers.
 * 
 */
public final class IntFraction {

    public final int numerator;

    public final int denominator;

    public IntFraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public String toString() {
        if (denominator == 100) return numerator + "%"; else return numerator + "/" + denominator;
    }
}
