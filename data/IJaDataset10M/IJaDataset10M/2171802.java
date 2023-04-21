package com.eaio.stringsearch;

/**
 * An implementation of Wu-Manbers k-differences algorithm. 
 * <p>
 * Note that with k-differences algorithms, you might not always get the 
 * results you'd expect. For example, searching "Johann" in "Hallo Johann"
 * with <code>k=1</code> would return the position of the blank and not
 * of the J since the blank can be eliminated with one operation.
 * <p>
 * This is an <strong>experimental</strong> algorithm that might not work
 * for longer texts.
 * 
 * @see <a href="http://johannburkard.de/software/stringsearch/">StringSearch
 * &#8211; high-performance pattern matching algorithms in Java</a>
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: WuManber.java,v 1.7 2008/06/29 11:39:46 grnull Exp $
 */
public class WuManber extends MismatchSearch {

    private static final int MSB = 0x80000000;

    private static final int MAX_PATTERN_LENGTH = 32;

    /**
     * Constructor for WuManber.
     */
    public WuManber() {
        super();
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#processBytes(byte[], int)
     */
    public Object processBytes(byte[] pattern, int k) {
        int max = Math.min(pattern.length, MAX_PATTERN_LENGTH);
        int[] T = new int[256];
        int mask = MSB;
        for (int j = 0; j < max; ++j) {
            T[index(pattern[j])] |= mask;
            mask >>= 1;
        }
        int[] s = new int[k + 1];
        int[] olds = new int[k + 1];
        for (int l = 1; l <= k; ++l) {
            olds[l] = (olds[l - 1] >> 1) | MSB;
        }
        return new Object[] { T, s, olds };
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#processChars(char[], int)
     */
    public Object processChars(char[] pattern, int k) {
        int max = Math.min(pattern.length, MAX_PATTERN_LENGTH);
        CharIntMap T = createCharIntMap(pattern, max, 0);
        int mask = MSB;
        for (int j = 0; j < max; ++j) {
            T.set(pattern[j], T.get(pattern[j]) | mask);
            mask >>= 1;
        }
        int[] s = new int[k + 1];
        int[] olds = new int[k + 1];
        for (int l = 1; l <= k; ++l) {
            olds[l] = (olds[l - 1] >> 1) | MSB;
        }
        return new Object[] { T, s, olds };
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#searchBytes(byte[], int,
     * int, byte[], java.lang.Object, int)
     */
    public int[] searchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed, int k) {
        int max = Math.min(pattern.length, MAX_PATTERN_LENGTH);
        Object[] o = (Object[]) processed;
        int[] T = (int[]) o[0];
        int[] s = (int[]) o[1];
        int[] olds = (int[]) o[2];
        for (int i = textStart; i < textEnd; ++i) {
            s[0] = ((s[0] >> 1) | MSB) & T[index(text[i])];
            for (int l = 1; l <= k; ++l) {
                s[l] = ((olds[l] >> 1) & T[index(text[i])]) | ((olds[l - 1] | s[l - 1]) >> 1) | olds[l - 1] | MSB;
            }
            if ((s[k] & (1 << 32 - max)) == (1 << 32 - max)) {
                return new int[] { (i - max + 1), 0 };
            }
            System.arraycopy(s, 0, olds, 0, olds.length);
        }
        return new int[] { -1, 0 };
    }

    /**
     * @see com.eaio.stringsearch.MismatchSearch#searchChars(char[], int,
     * int, char[], java.lang.Object, int)
     */
    public int[] searchChars(char[] text, int textStart, int textEnd, char[] pattern, Object processed, int k) {
        int max = Math.min(pattern.length, MAX_PATTERN_LENGTH);
        Object[] o = (Object[]) processed;
        CharIntMap T = (CharIntMap) o[0];
        int[] s = (int[]) o[1];
        int[] olds = (int[]) o[2];
        for (int i = textStart; i < textEnd; ++i) {
            s[0] = ((s[0] >> 1) | MSB) & T.get(text[i]);
            for (int l = 1; l <= k; ++l) {
                s[l] = ((olds[l] >> 1) & T.get(text[i])) | ((olds[l - 1] | s[l - 1]) >> 1) | olds[l - 1] | MSB;
            }
            if ((s[k] & (1 << 32 - max)) == (1 << 32 - max)) {
                return new int[] { (i - max + 1), 0 };
            }
            System.arraycopy(s, 0, olds, 0, olds.length);
        }
        return new int[] { -1, 0 };
    }

    /**
     * @see com.eaio.stringsearch.StringSearch#usesNative()
     */
    public boolean usesNative() {
        return false;
    }
}
