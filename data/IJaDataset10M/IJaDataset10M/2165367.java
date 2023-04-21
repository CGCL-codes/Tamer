package name.huzhenbo.java.algorithm.talent;

/**
 * Consider a function which, for a given whole number n, returns the number of ones required
 * when writing out all numbers between 0 and n.
 * For example, f(13)=6. Notice that f(1)=1. Find out the n that match f(n)=n?
 */
class TheOnes {

    /**
     *
     * Algorithm:
     *
     * e.g.
     *
     * f(876) = 100 + 8 * full(2) + f(76)      // when first digit > 1
     *
     * f(176) = 76 + 1 * full(2) + f(76)       // when first digit == 1
     *
     * @param n
     * @return
     */
    public int go(int n) {
        int pow = getPow(n);
        if (pow == 0) {
            return n >= 1 ? 1 : 0;
        }
        int base = intPow(pow);
        int firstDigit = n / base;
        int leftN = n % base;
        int current = firstDigit * full(pow);
        current += (firstDigit == 1 ? leftN + 1 : intPow(pow));
        return current + go(leftN);
    }

    /**
     * 0 - 99:  2 * 10
     * 0 - 999: 3 * 100
     * @param pow
     * @return
     */
    private int full(int pow) {
        return pow * intPow(pow - 1);
    }

    private int intPow(int pow) {
        return new Double(Math.pow(10, pow)).intValue();
    }

    private int getPow(int n) {
        int pow = 0;
        n /= 10;
        while (n > 0) {
            pow++;
            n /= 10;
        }
        return pow;
    }
}
