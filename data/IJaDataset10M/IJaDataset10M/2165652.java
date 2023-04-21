package com.choicemaker.matching.gen;

/**
 * Computes the modified edit distance between 2 strings.
 * As compared to regular edit distance, "swaps" or "twiddles" of two 
 * letters count as a single edit rather than two.
 * 
 * @author Adam Winkel
 * @version   $Revision: 1.2 $ $Date: 2010/03/27 22:24:25 $
 * @see EditDistance
 */
public class EditDistance2 {

    /**
	 * Computes the modified edit distance between two strings.
	 *
	 * If the result would be greater than <code>maxDistance</code>, then
	 * <code>maxDistance + 1</code> is returned. The rationale for
	 * the the <code>maxDistance</code> parameter is to improve performance
	 * by stopping the computation if the result would be bigger than
	 * interested.
	 *
	 * @param  s           The first string.
	 * @param  t           The second string.
	 * @param  maxDistance The maximum distance. Must be <code>&gt; 0</code> and
	 *           <code>&lt; Integer.MAX_VALUE</code>.
	 * @return the minimum edit distance between <code>s</code> and <code>t</code>
	 * @throws IllegalArgumentException if one of the Strings is <code>null</code>
	 * or the maxDistance is not positive. 
	 */
    public static int editDistance2(String s, String t, int maxDistance) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("null string");
        }
        if (maxDistance < 1) {
            throw new IllegalArgumentException("non-positive distance");
        }
        int m = s.length();
        int n = t.length();
        if (m == 0) {
            return Math.min(maxDistance + 1, n);
        }
        if (n == 0) {
            return Math.min(maxDistance + 1, m);
        }
        int[][] d = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        int cost;
        for (int i = 1; i <= m; i++) {
            char s_i = s.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                if (s_i == t.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                d[i][j] = Math.min(d[i - 1][j] + 1, Math.min(d[i][j - 1] + 1, d[i - 1][j - 1] + cost));
                if (i > 1 && j > 1 && s.charAt(i - 2) == t.charAt(j - 1) && s.charAt(i - 1) == t.charAt(j - 2)) {
                    d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + 1);
                }
            }
        }
        return Math.min(maxDistance + 1, d[m][n]);
    }

    /**
	 * Computes the modified edit distance between two strings.
	 *
	 * This is equivalent to <code>editDistance(s, t, Integer.MAX_VALUE - 1)</code>.
	 *
	 * @param  s           The first string.
	 * @param  t           The second string.
	 * @return the minimum edit distance between <code>a</code> and <code>b</code> 
	 */
    public static int editDistance2(String s, String t) {
        return editDistance2(s, t, Integer.MAX_VALUE - 1);
    }
}
