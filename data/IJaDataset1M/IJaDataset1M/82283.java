package be.docarch.odt2braille.utils;

/**
 *
 * @author Vincent Spiewak
 */
public class LetterNumbering {

    private static final String[] LETTER_NUMS = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    /**
     * Convert number to letter in base 26
     *
     * ex: A, B, ..., Z,
     *     AA, AB, ..., AZ,
     *     BA, BB, BC ... BZ,
     *     ...,
     *     ZA, ZB, ZC ... ZZ,
     *     AAA, AAB,..., AAZ,
     *     ABA, ABB, ABC, ... ABZ,
     *     ...
     *
     * @param number (MUST be > 1)
     * @return letter
     *
     */
    public static String toLetter(int n) {
        if (n < 1) return null; else return toLetterSub(n - 1);
    }

    private static String toLetterSub(int n) {
        int r = n % 26;
        String result = "";
        if (n - r == 0) {
            result = LETTER_NUMS[n];
        } else {
            result = toLetterSub(((n - r) - 1) / 26) + LETTER_NUMS[r];
        }
        return result;
    }
}
