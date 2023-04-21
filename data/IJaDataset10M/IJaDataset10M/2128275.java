package org.jExigo;

import java.math.BigDecimal;

/**
 * @author Joe McVerry - American Coders, Ltd.
 *
 * object to do fixed decimal arithmetic
 *
 */
public class FixedDecimalArithmetic {

    /**
	 * add two FDN resulting in a FDN
	 * @param addto number to be added to
	 * @param addend number to be added
	 * @return result
	 */
    public static FixedDecimalNumber add(FixedDecimalNumber addto, FixedDecimalNumber addend) {
        if (addend.getDecimalPositions() == addto.getDecimalPositions()) {
            return new FixedDecimalNumber(addto.getLongValue() + addend.getLongValue(), addto.getDecimalPositions());
        }
        if (addend.getDecimalPositions() < addto.getDecimalPositions()) {
            long val = addend.getLongValue();
            for (int i = 0; i < (addto.getDecimalPositions() - addend.getDecimalPositions()); i++) val *= 10;
            return new FixedDecimalNumber(val + addto.getLongValue(), addto.getDecimalPositions());
        }
        if (addto.getDecimalPositions() < addend.getDecimalPositions()) {
            long val = addto.getLongValue();
            for (int i = 0; i < (addend.getDecimalPositions() - addto.getDecimalPositions()); i++) val *= 10;
            return new FixedDecimalNumber(val + addend.getLongValue(), addend.getDecimalPositions());
        }
        System.err.println("add method programming error, can't get here.");
        return null;
    }

    /**
	 * substract a FDN from another resulting in a new FDN.
	 * @param  minuend the quantity from which the subtrahend is deducted in subtraction
	 * @param  subtrahend the quantity that is deducted from the minuend
	 * @param result
	 */
    public static FixedDecimalNumber subtract(FixedDecimalNumber minuend, FixedDecimalNumber subtrahend) {
        if (subtrahend.getDecimalPositions() == minuend.getDecimalPositions()) {
            return new FixedDecimalNumber(minuend.getLongValue() - subtrahend.getLongValue(), minuend.getDecimalPositions());
        }
        if (subtrahend.getDecimalPositions() < minuend.getDecimalPositions()) {
            long val = subtrahend.getLongValue();
            for (int i = 0; i < (minuend.getDecimalPositions() - subtrahend.getDecimalPositions()); i++) val *= 10;
            return new FixedDecimalNumber(minuend.getLongValue() - val, minuend.getDecimalPositions());
        }
        if (minuend.getDecimalPositions() < subtrahend.getDecimalPositions()) {
            long val = minuend.getLongValue();
            for (int i = 0; i < (subtrahend.getDecimalPositions() - minuend.getDecimalPositions()); i++) val *= 10;
            return new FixedDecimalNumber(val - subtrahend.getLongValue(), subtrahend.getDecimalPositions());
        }
        System.err.println("add method programming error, can't get here.");
        return null;
    }

    /**
	 * multiply a FDN by another resulting in a new FDN.
	 * @param  multiplicand the quantity from which the subtrahend is deducted in subtraction
	 * @param  multiplier the quantity that is deducted from the minuend
	 * @param result
	 */
    public static FixedDecimalNumber multiply(FixedDecimalNumber multiplicand, FixedDecimalNumber multiplier) {
        BigDecimal bg = new BigDecimal(multiplicand.getLongValue());
        BigDecimal bg2 = new BigDecimal(multiplier.getLongValue());
        return new FixedDecimalNumber(bg.multiply(bg2).longValue(), multiplicand.getDecimalPositions() + multiplier.getDecimalPositions());
    }

    /**
	 * divide a FDN by another resulting in a new FDN.
	 * @param  dividend the quantity from which the subtrahend is deducted in subtraction
	 * @param  divisor the quantity that is deducted from the minuend
	 * @param result
	 */
    public static FixedDecimalNumber divide(FixedDecimalNumber dividend, FixedDecimalNumber divisor) {
        long dividendLong = dividend.getLongValue();
        long divisorLong = divisor.getLongValue();
        int dividendDecPos = dividend.getDecimalPositions();
        int divisorDecPos = divisor.getDecimalPositions();
        int diffDecPos = dividendDecPos - divisorDecPos;
        long result = dividendLong / divisorLong;
        diffDecPos = dividendDecPos - divisorDecPos;
        while (diffDecPos < dividendDecPos) {
            result *= 10;
            diffDecPos++;
        }
        return new FixedDecimalNumber(result, dividendDecPos);
    }

    /** 
	 * computes the power of the FDA to inInt
	 * @param  powerand the quantity from which the subtrahend is deducted in subtraction
	 * @param inPower - power value
	 * @throws Exception - illogical to get thrown but keep here for calling methods
	 */
    public static FixedDecimalNumber power(FixedDecimalNumber powerand, int inPower) throws Exception {
        return power(powerand, inPower, powerand.getDecimalPositions());
    }

    /** 
	 * computes the power of the FDA to inInt
	 * @param inInt - power value
	 * @param leastSignificantDigits - rounds down to least significan digits
	 * @throws Exception - most likely passed a negative number in leastSignificantDigits
	 */
    public static FixedDecimalNumber power(FixedDecimalNumber powerand, int inPower, int leastSignificantDigits) throws Exception {
        if (leastSignificantDigits < 0) throw new org.jExigo.Exception("not implemented to use negative");
        if (inPower < 0) throw new org.jExigo.Exception("not implemented to use negative");
        if (inPower == 0) {
            return new FixedDecimalNumber(1, 0);
        }
        double d = Double.parseDouble(powerand.get());
        d = Math.pow(d, Double.parseDouble(inPower + ""));
        return new FixedDecimalNumber(d, leastSignificantDigits);
    }

    /**
	 * static method to create a FDN object equal to PI - we have PI stored to 18 digits
	 * @param leastSignificantDigits
	 * @throws Exception - most likely passed a negative number
	 */
    public static FixedDecimalNumber makePI(int leastSignificantDigits) throws Exception {
        if (leastSignificantDigits < 0) throw new Exception("not implemented to use negative");
        if (leastSignificantDigits > 18) throw new Exception("we have PI stored to 18 digits");
        FixedDecimalNumber fdaPIski = new FixedDecimalNumber(FixedDecimalNumber.myPI);
        return truncate(fdaPIski, leastSignificantDigits);
    }

    /**
	 * rounds to the least number of significant digits
	 * @param roundee FDN to be rounded
	 * @param leastSignificantDigits
	 * @throws Exception - most likely passed a negative number
	 */
    public static FixedDecimalNumber round(FixedDecimalNumber roundee, int leastSignificantDigits) throws Exception {
        if (leastSignificantDigits < 0) throw new Exception("not implemented to use negative");
        long longValue = roundee.getLongValue();
        int originalDecimalPositions = roundee.getDecimalPositions();
        while (roundee.getDecimalPositions() > leastSignificantDigits) {
            if (longValue % 10 > 4) longValue += 10;
            longValue /= 10;
            originalDecimalPositions--;
        }
        return new FixedDecimalNumber(longValue, originalDecimalPositions);
    }

    /**
	 * truncates to the least number of significant digits
	 * @param truncatee FDN to be trucated
	 * @param leastSignificantDigits
	 * @throws Exception - most likely passed a negative number
	 */
    public static FixedDecimalNumber truncate(FixedDecimalNumber truncatee, int leastSignificantDigits) throws Exception {
        if (leastSignificantDigits < 0) throw new Exception("not implemented to use negative");
        int originalDecimalPositions = truncatee.getDecimalPositions();
        long longValue = truncatee.getLongValue();
        while (originalDecimalPositions > leastSignificantDigits) {
            longValue /= 10;
            originalDecimalPositions--;
        }
        return new FixedDecimalNumber(longValue, originalDecimalPositions);
    }
}
