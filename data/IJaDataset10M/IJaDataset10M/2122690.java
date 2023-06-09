package de.felixbruns.jotify.util;

import java.math.BigInteger;

/**
 * Class providing methods for converting between different radices.
 * 
 * @author Felix Bruns <felixbruns@web.de>
 */
public class BaseConvert {

    /**
	 * The minimum radix available for conversion to and from strings. The
	 * constant value of this field is the smallest value permitted for the
	 * radix argument in radix-conversion methods such as the digit method
	 * and the {@code forDigit} method.
	 */
    public static final int MIN_RADIX = 2;

    /**
	 * The maximum radix available for conversion to and from strings. The
	 * constant value of this field is the largest value permitted for the
	 * radix argument in radix-conversion methods such as the digit method
	 * and the {@code forDigit} method.  
	 */
    public static final int MAX_RADIX = 62;

    /**
	 * A string holding the available characters for conversion to and from
	 * strings.
	 */
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
	 * Returns the numeric value of the character ''c'' in the specified radix.
	 * <br><br>
	 * If the radix is not in the range {@code MIN_RADIX <= radix <= MAX_RADIX}
	 * or if the value of {@code ch} is not a valid digit in the specified
	 * radix, -1 is returned.
	 * 
	 * @param ch    the character to be converted.
	 * @param radix the radix.
	 * 
	 * @return the numeric value represented by the character in the specified radix.
	 */
    public static int digit(char ch, int radix) {
        if (radix <= 36) {
            ch = Character.toLowerCase(ch);
        }
        return CHARACTERS.substring(0, radix).indexOf(ch);
    }

    /**
	 * Determines the character representation for a specific digit in the
	 * specified radix. If the value of radix is not a valid radix, or the
	 * value of digit is not a valid digit in the specified radix, the null
	 * character (' ') is returned.
	 * <br><br>
	 * The radix argument is valid if it is greater than or equal to MIN_RADIX
	 * and less than or equal to MAX_RADIX. The digit argument is valid if
	 * {@code 0 <= digit < radix}.
	 * 
	 * @param digit the number to convert to a character.
	 * @param radix the radix.
	 * 
	 * @return the char representation of the specified digit in the specified radix.
	 */
    public static char forDigit(int digit, int radix) {
        return CHARACTERS.substring(0, radix).charAt(digit);
    }

    /**
	 * Converts a string from the source radix to a string in the target radix.
	 * 
	 * @param source      the source string to be converted.
	 * @param sourceRadix the source radix.
	 * @param targetRadix the target radix.
	 * 
	 * @return the result string in the target radix.
	 */
    public static String convert(String source, int sourceRadix, int targetRadix) {
        StringBuilder result = new StringBuilder();
        if (sourceRadix < MIN_RADIX || targetRadix < MIN_RADIX || sourceRadix > MAX_RADIX || targetRadix > MAX_RADIX) {
            throw new IllegalArgumentException("Source and target radix both need to be in a" + " range from " + MIN_RADIX + " to " + MAX_RADIX);
        }
        BigInteger radixFrom = BigInteger.valueOf(sourceRadix);
        BigInteger radixTo = BigInteger.valueOf(targetRadix);
        BigInteger value = BigInteger.ZERO;
        BigInteger multiplier = BigInteger.ONE;
        for (int i = source.length() - 1; i >= 0; i--) {
            int digit = digit(source.charAt(i), sourceRadix);
            if (digit == -1) {
                throw new IllegalArgumentException("The character '" + source.charAt(i) + "'" + " is not defined for the radix " + sourceRadix);
            }
            value = value.add(multiplier.multiply(BigInteger.valueOf(digit)));
            multiplier = multiplier.multiply(radixFrom);
        }
        while (BigInteger.ZERO.compareTo(value) < 0) {
            BigInteger[] quotientAndRemainder = value.divideAndRemainder(radixTo);
            char c = forDigit(quotientAndRemainder[1].intValue(), targetRadix);
            result.insert(0, c);
            value = quotientAndRemainder[0];
        }
        return result.toString();
    }
}
