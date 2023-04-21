package org.myjerry.maer.page1;

import org.myjerry.maer.util.PalindromeUtil;

/**
 * Problem 4 on Project Euler, http://projecteuler.net/index.php?section=problems&id=4
 * 
 * @author Sandeep Gupta
 * @since Jan 6, 2011
 */
public class Problem04 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        long maxProduct = 0;
        String result = null;
        for (int i = 999; i > 99; i--) {
            for (int j = 999; j > 99; j--) {
                long product = i * j;
                boolean isPalindrome = PalindromeUtil.checkPalindrome(product);
                if (isPalindrome) {
                    if (product > maxProduct) {
                        maxProduct = product;
                        result = "Numbers are: " + i + ", " + j + " with product: " + product;
                    }
                }
            }
        }
        System.out.println(result);
    }
}
