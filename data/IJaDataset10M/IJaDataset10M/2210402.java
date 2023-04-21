package org.myjerry.maer.page1;

import org.myjerry.maer.util.MathUtil;

/**
 * Problem 3 on Project Euler, http://projecteuler.net/index.php?section=problems&id=3
 * 
 * @author Sandeep Gupta
 * @since Jan 6, 2011
 */
public class Problem03 {

    private static long num = 600851475143l;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int divisor = 3;
        do {
            long mod = num % divisor;
            if (mod == 0) {
                long quotient = num / divisor;
                if (MathUtil.isPrime(quotient)) {
                    System.out.println("Max prime: " + quotient);
                    break;
                }
            }
            divisor++;
        } while (true);
    }
}
