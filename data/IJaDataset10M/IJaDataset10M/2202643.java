package com.aliasi.spell;

import com.aliasi.util.Distance;
import com.aliasi.util.Proximity;

/**
 * The <code>EditDistance</code> class implements the standard notion
 * of edit distance, with or without transposition.  The distance
 * without transposition is known as Levenshtein distance, and
 * with transposition as Damerau-Levenstein distance (see below about
 * distance-like metric properties).
 * 
 * <P>The edit distance between two strings is defined to be the
 * minimum number of non-matching substring edits that is required to
 * turn one string into the other.  The available edits and their
 * corresponding input and output substrings are:
 *
 * <blockquote>
 * <table border='1' cellpadding='5'>
 * <tr><td><i>Operation</i></td>
 *     <td><i>Input</i></td>
 *     <td><i>Output</i></td>
 *     <td><i>Notation</i></td>
 * <tr><td>Match</td><td>"a"</td><td>"a"</td><td>m(a)</td></tr>
 * <tr><td>Insert</td><td>""</td><td>"a"</td><td>i(a)</td></tr>
 * <tr><td>Delete</td><td>"a"</td><td>""</td><td>d(a)</td></tr>
 * <tr><td>Substitute</td><td>"a"</td><td>"b"</td><td>s(b,a)</td></tr>
 * <tr><td>Transpose</td><td>"ab"</td><td>"ba"</td><td>t(ab)</td></tr>
 * </table>
 * </blockquote>
 * 
 * Examples of minimal edit sequences are as follows, with distances
 * as indicated
 *
 * <blockquote><code>
 * <table border='1' cellpadding='5'>
 * <tr><td><i>Input</i></td>
 *     <td><i>Output</i></td>
 *     <td><i>Edit Sequence (w. Transp)</i></td>
 *     <td><i>Dist (w. Transp)</i></td></tr>
 * <tr><td>it</td><td>it</td><td>m(i) m(t)</td><td>0</td></tr>
 * <tr><td>gage</td><td>gauge</td><td>m(g) m(a) i(u) m(g) m(e)</td><td>1</td></tr>
 * <tr><td>thhe</td><td>the</td><td>m(t) d(h) m(h) m(e)</td><td>1</td></tr>
 * <tr><td>tenser</td><td>tensor</td><td>m(t) m(e) m(n) m(s) s(o,e) m(r)</td><td>1</td></tr>
 * <tr><td>hte</td><td>the</td><td>d(h) m(t) i(h) m(e) [t(ht) m(e)]</td><td>2 [1]</td></tr>
 * <tr><td>htne</td><td>then</td><td>d(h) m(t) s(h,n) m(e) i(n) [t(ht) t(ne)]</td><td>3 [2]</td></tr>
 * <tr><td>pwn4g</td><td>ownage</td><td>s(o,p) m(w) m(n) s(a,4) m(g) i(e)</td><td>3</td></tr>
 * </table>
 * </code></blockquote>
 * 
 * Note that, in general, there will be more than one way to edit a
 * string into another string.  For instance, a delete and insert may
 * replace a transposition, so that "hte" becomes "the" through edits
 * "d(h) m(t) i(h) m(e)" or "i(t) m(h) d(t) m(e)", as well as many
 * many more, such as "d(h) d(t) d(e) i(t) i(h) i(e)".  
 *
 * <h4>Distance as Metric</h4>
 * 
 * <P>Edit distance without transposition defines a proper metric.
 * Recall that a distance measure <code>d(x,y)</code> forms a metric
 * if for all <code>x, y, z</code>, we have (1) <code>d(x,y) &gt;=
 * 0</code>, (2) <code>d(x,y) = d(y,x)</code>, (3) <code>d(x,x) =
 * 0</code>, and (4) <code>d(x,y) + d(y,z) &gt;= d(x,z)</code>.  All
 * of these properties are easy to verify.  But with transposition, we
 * have strings such as <code>AB</code>, <code>BA</code> and
 * <code>ACB</code>.  With transposition, <code>d(AB,BA)=1</code>,
 * <code>d(BA,BCA)=1</code>, but <code>d(AB,BCA)= 3 &gt;= d(AB,BA) +
 * d(BA,BCA) = 1 + 1 = 2</code>.
 * 
 * <P><i>Implementation Note:</i> This class implements edit distance
 * using dynamic programming in time <code>O(n*m)</code> where
 * <code>n</code> and <code>m</code> are the length of the sequences
 * being compared.  Using a sliding window of three lattice slices rather
 * than allocating the entire lattice at once, the space required is
 * that for three arrays of integers as long as the shorter of the two
 * character sequences being compared.  For details, see section
 * 12.1.1 of:
 *
 * <UL>
 * <LI>Dan Gusfield (1997) <i>Algorithms on Strings, Trees, and Sequences</i>.
 * Cambridge University Press.
 * </UL>
 * 
 * @author  Bob Carpenter
 * @version 3.0
 * @since   LingPipe2.0
 */
public class EditDistance implements Distance<CharSequence>, Proximity<CharSequence> {

    private final boolean mAllowTransposition;

    /**
     * Construct an edit distance with or without transposition based
     * on the specified flag.
     *
     * @param allowTransposition Set to <code>true</code> to allow
     * transposition edits in the constructed distance.
     */
    public EditDistance(boolean allowTransposition) {
        mAllowTransposition = allowTransposition;
    }

    /**
     * Returns the edit distance between the specified character
     * sequences.  Whether transposition is allowed or not is set at
     * construction time.  This method may be accessed concurrently
     * without synchronization.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return Edit distance between the character sequences.
     */
    public double distance(CharSequence cSeq1, CharSequence cSeq2) {
        return editDistance(cSeq1, cSeq2, mAllowTransposition);
    }

    /**
     * Returns the proximity between the character sequences.
     * Proximity is defined as the negation of the distance:
     *
     * <blockquote><pre>
     * proximity(cs1,cs2) = -distance(cs1,cs2)
     * </pre></blockquote>
     *
     * and thus proximities will all be negative or zero.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @return Proximity between the character sequences.
     */
    public double proximity(CharSequence cSeq1, CharSequence cSeq2) {
        return -distance(cSeq1, cSeq2);
    }

    /**
     * Returns a string representation of this edit distance.
     *
     * @return A string representation of this edit distance.
     */
    @Override
    public String toString() {
        return "EditDistance(" + mAllowTransposition + ")";
    }

    /**
     * Returns the edit distance between the character sequences with
     * or without transpositions as specified.  This distance is
     * symmetric.  This method is thread safe and may be accessed
     * concurrently.
     *
     * @param cSeq1 First character sequence.
     * @param cSeq2 Second character sequence.
     * @param allowTransposition Set to <code>true</code> to allow
     * transposition edits.
     * @return Edit distance between the character sequences.
     */
    public static int editDistance(CharSequence cSeq1, CharSequence cSeq2, boolean allowTransposition) {
        if (cSeq1.length() < cSeq2.length()) {
            CharSequence temp = cSeq1;
            cSeq1 = cSeq2;
            cSeq2 = temp;
        }
        if (cSeq2.length() == 0) return cSeq1.length();
        if (cSeq2.length() == 1) {
            char c = cSeq2.charAt(0);
            for (int i = 0; i < cSeq1.length(); ++i) if (cSeq1.charAt(i) == c) return cSeq1.length() - 1;
            return cSeq1.length();
        }
        if (allowTransposition) return editDistanceTranspose(cSeq1, cSeq2);
        return editDistanceNonTranspose(cSeq1, cSeq2);
    }

    private static int editDistanceNonTranspose(CharSequence cSeq1, CharSequence cSeq2) {
        int xsLength = cSeq1.length() + 1;
        int ysLength = cSeq2.length() + 1;
        int[] lastSlice = new int[ysLength];
        int[] currentSlice = new int[ysLength];
        for (int y = 0; y < ysLength; ++y) currentSlice[y] = y;
        for (int x = 1; x < xsLength; ++x) {
            char cX = cSeq1.charAt(x - 1);
            int[] lastSliceTmp = lastSlice;
            lastSlice = currentSlice;
            currentSlice = lastSliceTmp;
            currentSlice[0] = x;
            for (int y = 1; y < ysLength; ++y) {
                int yMinus1 = y - 1;
                currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1) ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
            }
        }
        return currentSlice[currentSlice.length - 1];
    }

    private static int editDistanceTranspose(CharSequence cSeq1, CharSequence cSeq2) {
        int xsLength = cSeq1.length() + 1;
        int ysLength = cSeq2.length() + 1;
        int[] twoLastSlice = new int[ysLength];
        int[] lastSlice = new int[ysLength];
        int[] currentSlice = new int[ysLength];
        for (int y = 0; y < ysLength; ++y) lastSlice[y] = y;
        currentSlice[0] = 1;
        char cX = cSeq1.charAt(0);
        for (int y = 1; y < ysLength; ++y) {
            int yMinus1 = y - 1;
            currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1) ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
        }
        char cYZero = cSeq2.charAt(0);
        for (int x = 2; x < xsLength; ++x) {
            char cXMinus1 = cX;
            cX = cSeq1.charAt(x - 1);
            int[] tmpSlice = twoLastSlice;
            twoLastSlice = lastSlice;
            lastSlice = currentSlice;
            currentSlice = tmpSlice;
            currentSlice[0] = x;
            currentSlice[1] = Math.min(cX == cYZero ? lastSlice[0] : 1 + lastSlice[0], 1 + Math.min(lastSlice[1], currentSlice[0]));
            char cY = cYZero;
            for (int y = 2; y < ysLength; ++y) {
                int yMinus1 = y - 1;
                char cYMinus1 = cY;
                cY = cSeq2.charAt(yMinus1);
                currentSlice[y] = Math.min(cX == cY ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
                if (cX == cYMinus1 && cY == cXMinus1) currentSlice[y] = Math.min(currentSlice[y], 1 + twoLastSlice[y - 2]);
            }
        }
        return currentSlice[currentSlice.length - 1];
    }

    /**
     * Edit distance allowing transposition.  The implementation is
     * thread safe and may be accessed concurrently.
     */
    public static final Distance<CharSequence> TRANSPOSING = new EditDistance(true);

    /**
     * Edit distance disallowing transposition.  The implementation is
     * thread safe and may be accessed concurrently.
     */
    public static final Distance<CharSequence> NON_TRANSPOSING = new EditDistance(false);
}
