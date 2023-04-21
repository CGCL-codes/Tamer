package com.choicemaker.matching.gen;

/**
 * Computes the edit distance between 2 strings.
 * 
 * Edit distance computes the minimum number of edits that have to be made to 
 * one string to transform it into the other. Deletion, insertion, and 
 * replacement of a letter are the possible edit steps. Edit distance can be 
 * used to detect typos rather than phonetic misspellings. The function is 
 * equally well suited for most languages with a small alphabet. 
 * <br>
 * <b>E.g.</b>, the edit distance between <code>Test</code> and <code>Fest</code> is 1. The edit distance 
 * between <code>Test</code> and <code>Feste</code> is 2 because we have to perform one deletion and 
 * one replacement to transform <code>Feste</code> into <code>Test</code>.
 *
 * @author    Peining Tao
 * @author    Martin Buechi
 * @version   $Revision: 1.1.1.1 $ $Date: 2009/05/03 16:03:04 $
 */
public class EditDistance {

    /**
	 * Computes the edit distance between two strings.
	 *
	 * If the result would be greater than <code>maxDistance</code>, then
	 * <code>maxDistance + 1</code> is returned. The rationale for
	 * the the <code>maxDistance</code> parameter is to improve performance
	 * by stopping the computation if the result would be bigger than
	 * interested.
	 *
	 * The following examples illustrate edit distance:
	 * <table border="1">
	 *   <tr><th colspan="3">Inputs</th><th rowspan="2">Output</th><th rowspan="2">Notes</th></tr>
	 *   <tr><th>a</th><th>b</th><th>maxDistance</th></tr>
	 *   <tr><td>john</td><td>jon</td><td>10</td><td>1</td><td>&nbsp;</td></tr>
	 *   <tr><td>beautiful</td><td>beaut</td><td>10</td><td>4</td><td>&nbsp;</td></tr>
	 *   <tr><td>beautiful</td><td>beaut</td><td>2</td><td>3</td><td>Actual distance greater 2.</td></tr>
	 *   <tr><td><code>null</code></td><td>test</td><td>10</td><td><code>NullPointerException</code> thrown.</td><td>&nbsp;</td></tr>
	 * </table>
	 *
	 * @param  s           The first string.
	 * @param  t           The second string.
	 * @param  maxDistance The maximum distance. Must be <code>&gt; 0</code> and
	 *           <code>&lt; Integer.MAX_VALUE</code>.
	 * @return the minimum edit distance between <code>s</code> and <code>t</code>
	 * @throws NullPointerException if one of the Strings is <code>null</code>. 
	 */
    public static int editDistance(String s, String t, int maxDistance) {
        int cost;
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return Math.min(maxDistance + 1, m);
        }
        if (m == 0) {
            return Math.min(maxDistance + 1, n);
        }
        int m1 = m + 1;
        int[] d = new int[(n + 1) * (m + 1)];
        for (int i = 1; i <= n; i++) {
            d[i * m1] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[j] = j;
        }
        for (int i = 1; i <= n; i++) {
            char s_i = s.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                if (s_i == t.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                d[i * m1 + j] = Math.min(d[(i - 1) * m1 + j] + 1, Math.min(d[i * m1 + j - 1] + 1, d[(i - 1) * m1 + j - 1] + cost));
            }
        }
        return Math.min(maxDistance + 1, d[n * m1 + m]);
    }

    /**
	 * Computes the edit distance between two strings.
	 *
	 * This is equivalent to <code>editDistance(s, t, Integer.MAX_VALUE - 1)</code>.
	 *
	 * @param  s           The first string.
	 * @param  t           The second string.
	 * @return the minimum edit distance between <code>s</code> and <code>t</code>
	 */
    public static int editDistance(String s, String t) {
        return editDistance(s, t, Integer.MAX_VALUE - 1);
    }
}
