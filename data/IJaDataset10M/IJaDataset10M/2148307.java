package com.eaio.stringsearch;

/**
 * An implementation of Horspool's improved version of the Boyer-Moore String
 * searching algorithm. See "Practical fast searching in strings" (appeared in
 * <em>Software - Practice & Experience, 10(6):501-506</em>). Unfortunately,
 * there seems to be no on-line version of his paper.
 * <br><br>
 * This is the second fastest algorithm in this library for the
 * <code>searchChars</code> and <code>searchString</code>. Except for very short
 * patterns (&lt; 5 characters), it is always faster than any other algorithm
 * except {@link com.eaio.stringsearch.BoyerMooreHorspoolRaita} and faster than
 * {@link java.lang.String#indexOf(java.lang.String)} by more than 5 times for
 * patterns longer than 24 characters. It's <code>searchBytes</code> methods are
 * slightly faster than in the
 * {@link com.eaio.stringsearch.BoyerMooreHorspoolRaita} algorithm.
 * <br><br>
 * This implementation is based on <a
 * href="http://www.dcc.uchile.cl/~rbaeza/handbook/algs/7/713b.srch.c">Ricardo
 * Baeza-Yates' implementation</a>.
 * <pre>
 * Preprocessing: O(m + &sum;) time
 * 
 * Processing   : O(mn) worst case
 * </pre>
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version 1.2
 */
public class BoyerMooreHorspool extends StringSearch {

    /**
  * Constructor for BoyerMooreHorspool. Note that it is not required to create
  * multiple instances.
  */
    public BoyerMooreHorspool() {
    }

    /**
  * Returns a <code>int</code> array.
  * 
  * @see com.eaio.stringsearch.StringSearch#processBytes(byte[])
  */
    public Object processBytes(byte[] pattern) {
        int[] skip = new int[256];
        for (int i = 0; i < skip.length; ++i) {
            skip[i] = pattern.length;
        }
        for (int i = 0; i < pattern.length - 1; ++i) {
            skip[index(pattern[i])] = pattern.length - i - 1;
        }
        return skip;
    }

    /**
  * Returns a {@link CharIntMap}.
  * 
  * @see com.eaio.stringsearch.StringSearch#processChars(char[])
  */
    public Object processChars(char[] pattern) {
        CharIntMap skip = createCharIntMap(pattern, pattern.length);
        for (int i = 0; i < pattern.length - 1; ++i) {
            skip.set(pattern[i], pattern.length - i - 1);
        }
        return skip;
    }

    /**
  * @see com.eaio.stringsearch.StringSearch#searchBytes(byte[], int, int,
  * byte[], java.lang.Object)
  */
    public int searchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed) {
        return useNative ? nativeSearchBytes(text, textStart, textEnd, pattern, processed) : javaSearchBytes(text, textStart, textEnd, pattern, processed);
    }

    private int javaSearchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed) {
        int[] skip = (int[]) processed;
        int i, j, k;
        for (k = pattern.length - 1; k < textEnd; k += skip[index(text[k])]) {
            for (j = pattern.length - 1, i = k; j >= 0 && text[i] == pattern[j] && i >= textStart; --j, --i) ;
            if (j == -1) return ++i;
        }
        return -1;
    }

    private native int nativeSearchBytes(byte[] text, int textStart, int textEnd, byte[] pattern, Object processed);

    /**
  * @see com.eaio.stringsearch.StringSearch#searchChars(char[], int, int,
  * char[], Object)
  */
    public int searchChars(char[] text, int textStart, int textEnd, char[] pattern, Object processed) {
        CharIntMap skip = (CharIntMap) processed;
        int i, j, k;
        for (k = pattern.length - 1; k < textEnd; k += skip.get(text[k])) {
            for (j = pattern.length - 1, i = k; j >= 0 && text[i] == pattern[j] && i >= textStart; --j, --i) ;
            if (j == -1) return ++i;
        }
        return -1;
    }
}
