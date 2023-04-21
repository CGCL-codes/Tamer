package org.shiftone.bigcalc.math.function;

import org.shiftone.bigcalc.Context;
import org.shiftone.bigcalc.Function;
import org.shiftone.bigcalc.core.function.AbstractOneArgumentFunction;
import org.shiftone.bigcalc.math.MathConstants;
import org.shiftone.bigcalc.util.Log;
import org.shiftone.bigcalc.util.TypeUtil;
import java.math.BigDecimal;

public class SquareRoot extends AbstractOneArgumentFunction implements Function, MathConstants {

    private static final Log LOG = new Log(SquareRoot.class);

    public Object evaluate(Context context, Object argument) throws Exception {
        return sqrt(TypeUtil.toBigDecimal(argument), context.getScale());
    }

    public static BigDecimal sqrt(BigDecimal input, int scale) {
        LOG.debug("sqrt(" + input + ")");
        if (input.signum() == 0) {
            return ZERO;
        } else if (input.signum() == -1) {
            throw new RuntimeException("negative values not supported");
        }
        BigDecimal guess = ONE;
        int i = 0;
        BigDecimal lastGuess;
        do {
            lastGuess = guess;
            guess = lastGuess.add(input.divide(lastGuess, scale, BigDecimal.ROUND_HALF_UP));
            guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
            i++;
        } while (lastGuess.compareTo(guess) != 0);
        System.out.println("i = " + i);
        return guess.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(sqrt(new BigDecimal(9), 100));
        System.out.println(sqrt(new BigDecimal(100), 100));
        System.out.println(sqrt(new BigDecimal(144), 100));
        System.out.println(sqrt(new BigDecimal(99), 100));
    }
}
