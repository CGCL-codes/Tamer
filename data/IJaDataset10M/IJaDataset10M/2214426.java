package org.myjerry.maer.page1;

import org.myjerry.maer.util.MathUtil;

/**
 * Problem 21 on Project Euler, http://projecteuler.net/index.php?section=problems&id=21
 * 
 * @author Sandeep Gupta
 * @since Jan 6, 2011
 */
public class Problem21 {

    private static final int eulerLimit = 10000;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        boolean[] amicable = new boolean[eulerLimit];
        long next;
        long sum = 0;
        for (int i = 1; i < eulerLimit; i++) {
            if (!amicable[i]) {
                next = findNext(i);
                if (next > 0) {
                    if (next < eulerLimit) {
                        int nextIndex = (int) next;
                        amicable[nextIndex] = true;
                    }
                    sum += i + next;
                }
            }
        }
        System.out.println("Sum = " + sum);
    }

    /**
	 * Find the next amicable number.
	 * 
	 * @param number
	 * @return
	 */
    private static long findNext(int number) {
        long divSum = MathUtil.sumOfDivisors(number);
        long otherSum = MathUtil.sumOfDivisors(divSum);
        if (otherSum == number && number != divSum) {
            return divSum;
        }
        return 0;
    }
}
