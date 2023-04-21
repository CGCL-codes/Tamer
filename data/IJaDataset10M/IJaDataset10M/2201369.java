package org.myjerry.maer.page1;

import java.math.BigInteger;
import org.myjerry.maer.util.MathUtil;

/**
 * Problem 26 on Project Euler,
 * http://projecteuler.net/index.php?section=problems&id=26
 * 
 * @author Sandeep Gupta
 * @since Jan 18, 2011
 */
public class Problem26 {

    private static final BigInteger one = BigInteger.valueOf(1);

    /**
	 * 1/d has a cycle of n digits if 10n-1 mod d = 0 for prime d. 
	 * It also follows that a prime number in the denominator, d, can yield up to d-1 repeating decimal digits.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        int n = 0;
        for (n = 997; n > 1; n -= 2) {
            if (MathUtil.isPrime(n)) {
                int c = 1;
                BigInteger num = BigInteger.valueOf(n);
                while (MathUtil.raiseToPower(10, c).subtract(one).mod(num).longValue() != 0) {
                    c += 1;
                    if (c > n) {
                        break;
                    }
                }
                if (n - c == 1) {
                    System.out.println("Number: " + n);
                    break;
                }
            }
        }
    }
}
