package com.mycila.math;

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

    private Factorial() {
    }

    private static final long[] factorials = new long[] { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };

    /**
     * Compute the factorial of a number by using a lookup table
     *
     * @param number A positive number, between 0 and 20
     * @return The factorial
     * @throws ArithmeticException If the number is too big (> 20)
     */
    public static long lookup(int number) throws ArithmeticException {
        if (number >= factorials.length) throw new ArithmeticException("Number too big:" + number);
        return factorials[number];
    }

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">falling factorial</a>
     * <code>(a)n = a! / (a-n)!</code>
     *
     * @param a A positive number
     * @param n A positive number
     * @return The factorial
     */
    public static long falling(long a, long n) {
        long b = a - n;
        b++;
        long res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }
}
