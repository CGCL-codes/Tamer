package org.shiftone.bigcalc.math.function;

import org.shiftone.bigcalc.Context;
import org.shiftone.bigcalc.Function;
import org.shiftone.bigcalc.core.function.AbstractOneArgumentFunction;
import org.shiftone.bigcalc.math.MathConstants;
import org.shiftone.bigcalc.util.Log;
import org.shiftone.bigcalc.util.TypeUtil;
import java.math.BigDecimal;

public class NaturalExponent extends AbstractOneArgumentFunction implements Function, MathConstants {

    private static final Log LOG = new Log(NaturalExponent.class);

    public Object evaluate(Context stdContext, Object argument) throws Exception {
        return exp(TypeUtil.toBigDecimal(argument), stdContext.getScale());
    }

    /**
     * exp(x) = e^x .
     * exp(x) = 1 + x/1! + x^2/2! + x^3/3! + x^4/4! + ...
     */
    public static BigDecimal exp(BigDecimal x, int scale) {
        BigDecimal value = ONE;
        BigDecimal numerator = ONE;
        BigDecimal denominator = ONE;
        long i = 1;
        BigDecimal lastValue;
        do {
            lastValue = value;
            numerator = numerator.multiply(x);
            denominator = denominator.multiply(BigDecimal.valueOf(i));
            int roundingMode = (i % 2 == 0) ? BigDecimal.ROUND_FLOOR : BigDecimal.ROUND_CEILING;
            roundingMode = BigDecimal.ROUND_FLOOR;
            BigDecimal term = numerator.divide(denominator, scale + 1, roundingMode);
            value = value.add(term);
            i++;
        } while (value.compareTo(lastValue) > 0);
        LOG.debug("exp(" + x + "," + scale + ") => " + value + " done in " + i + " iterations");
        value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    public static void main(String[] args) {
        int scale = 100000;
        System.out.println(exp(new BigDecimal("1"), scale));
    }
}
