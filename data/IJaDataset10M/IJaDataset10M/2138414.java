package uk.ac.lkl.common.util.value;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser of values such as 0.4 and 0.[3] (1/3).
 * 
 * The square-brackets syntax is used to specify recurring decimals. So 0.[3]
 * above is 0.333... (1/3) and similarly 0.[142857] is 0.142857142857... (1/7).
 * Using 0.[3] as an example, the algorithm for parsing these works as follows.
 * 
 * A decimal such as 0.333... can be seen as the sum of a geometric progression:
 * 
 * <pre>
 *        3      3        3
 * S  =  --  +  ---  +  ----  +  ...
 *       10     100     1000
 * </pre>
 * 
 * with starting term, a = 3/10 and ratio, r = 1/10.
 * 
 * The formula for the sum to infinity of a geometric series is:
 * 
 * <pre>
 *             a
 * S_inf  =  -----
 *           1 - n
 * </pre>
 * 
 * which, for the example above is:
 * 
 * <pre>
 *              3         3
 *             --        --
 *             10        10       3      1
 * S_inf  =  ------  =  ----  =  --  =  --
 *                1       9       9      3
 *           1 - --      --
 *               10      10
 * </pre>
 * 
 * More generally, for values such as <code>0.[d]</code> where <code>d</code>
 * is a single digit, the formula is:
 * 
 * <pre>
 *           d
 * S_inf  =  -
 *           9
 * </pre>
 * 
 * When there are n digits recurring <code>0.[r1r2...rn]</code>, the formula
 * is:
 * 
 * <pre>
 * 
 *           r1r2...rn  
 *           ---------   
 *             10&circ;n             r1r2...rn          
 * S_inf = ----------------  =  ---------
 *                   1           10&circ;n - 1
 *             1 - ----
 *                 10&circ;n
 * </pre>
 * 
 * <p>
 * The algorithm below parses strings with format
 * <code>i.d1d2...dm[r1r2...rn]</code> where i is an integer,
 * <code>d1d2...dm</code> are non-recurring digits preceding recurring digits
 * <code>r1r2...rn</code>. The value is therefore derived as an addition of
 * the three fractions:
 * </p>
 * 
 * <pre>
 *                d1d2...dm   r1r2...rn
 * fraction = i + --------- + ---------
 *                  10&circ;m       10&circ;n - 1
 * </pre>
 * 
 * @author $Author: darren.pearce $
 * @version $Revision: 6866 $
 * @version $Date: 2010-07-27 17:46:12 -0400 (Tue, 27 Jul 2010) $
 * 
 */
public class DecimalValueStringParser extends ValueStringParser {

    private static DecimalValueStringParser INSTANCE;

    private DecimalValueStringParser() {
    }

    public static DecimalValueStringParser getInstance() {
        if (INSTANCE == null) INSTANCE = new DecimalValueStringParser();
        return INSTANCE;
    }

    public static RationalValue parseValue(String valueString) {
        return INSTANCE.parseValueString(valueString);
    }

    public RationalValue parseValueString(String valueString) {
        Pattern pattern = Pattern.compile(" *([+-])? *([0-9]+)(?:\\.([0-9]*)(?:\\[([0-9]+)\\])?)? *");
        Matcher matcher = pattern.matcher(valueString);
        if (!matcher.matches()) throw new NumberFormatException();
        String signString = matcher.group(1);
        String integerString = matcher.group(2);
        String decimalString = matcher.group(3);
        String recurrenceString = matcher.group(4);
        if ("-".equals(signString)) {
            integerString = "-" + integerString;
        }
        BigInteger integer = new BigInteger(integerString);
        if (decimalString == null) {
            return new RationalValue(integer);
        } else {
            int decimalLength = decimalString.length();
            BigInteger decimalDenominator = BigInteger.TEN.pow(decimalLength);
            BigInteger decimal;
            if (decimalString.length() == 0) decimal = BigInteger.ZERO; else decimal = new BigInteger(decimalString);
            RationalValue value = new RationalValue(integer, decimal, decimalDenominator);
            if (recurrenceString == null) {
                return value;
            } else {
                int recurrenceLength = recurrenceString.length();
                BigInteger denominator9 = decimalDenominator.multiply(BigInteger.TEN.pow(recurrenceLength).subtract(BigInteger.ONE));
                BigInteger recurrence = new BigInteger(recurrenceString);
                RationalValue recurrenceValue = new RationalValue(recurrence, denominator9);
                return value.add(recurrenceValue);
            }
        }
    }
}
