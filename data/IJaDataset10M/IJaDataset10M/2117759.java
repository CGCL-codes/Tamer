package edu.vub.util.regexp;

import java.io.Serializable;

/**
 * An instance of this class represents a match
 * completed by a edu.vub.util.regexp matching function. It can be used
 * to obtain relevant information about the location of a match
 * or submatch.
 *
 * @author <A HREF="mailto:wes@cacas.org">Wes Biggs</A>
 */
public final class REMatch implements Serializable, Cloneable {

    private String matchedText;

    int eflags;

    int offset;

    int anchor;

    int index;

    int[] start;

    int[] end;

    REMatch next;

    boolean empty;

    int matchFlags;

    static final int MF_FIND_ALL = 0x01;

    public Object clone() {
        try {
            REMatch copy = (REMatch) super.clone();
            copy.next = null;
            copy.start = (int[]) start.clone();
            copy.end = (int[]) end.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
    }

    void assignFrom(REMatch other) {
        start = other.start;
        end = other.end;
        index = other.index;
        next = other.next;
    }

    REMatch(int subs, int anchor, int eflags) {
        start = new int[subs + 1];
        end = new int[subs + 1];
        this.anchor = anchor;
        this.eflags = eflags;
        clear(anchor);
    }

    void finish(CharIndexed text) {
        start[0] = 0;
        StringBuffer sb = new StringBuffer();
        int i;
        for (i = 0; i < end[0]; i++) sb.append(text.charAt(i));
        matchedText = sb.toString();
        for (i = 0; i < start.length; i++) {
            if ((start[i] == -1) ^ (end[i] == -1)) {
                start[i] = -1;
                end[i] = -1;
            }
        }
        next = null;
    }

    /** Clears the current match and moves the offset to the new index. */
    void clear(int index) {
        offset = index;
        this.index = 0;
        for (int i = 0; i < start.length; i++) {
            start[i] = end[i] = -1;
        }
        next = null;
    }

    /**
     * Returns the string matching the pattern.  This makes it convenient
     * to write code like the following:
     * <P>
     * <code> 
     * REMatch myMatch = myExpression.getMatch(myString);<br>
     * if (myMatch != null) System.out.println("Regexp found: "+myMatch);
     * </code>
     */
    public String toString() {
        return matchedText;
    }

    /**
     * Returns the index within the input text where the match in its entirety
     * began.
     */
    public int getStartIndex() {
        return offset + start[0];
    }

    /**
     * Returns the index within the input string where the match in
     * its entirety ends.  The return value is the next position after
     * the end of the string; therefore, a match created by the
     * following call:
     *
     * <P>
     * <code>REMatch myMatch = myExpression.getMatch(myString);</code>
     * <P>
     * can be viewed (given that myMatch is not null) by creating
     * <P>
     * <code>String theMatch = myString.substring(myMatch.getStartIndex(),
     * myMatch.getEndIndex());</code>
     * <P>
     * But you can save yourself that work, since the <code>toString()</code>
     * method (above) does exactly that for you.  
     */
    public int getEndIndex() {
        return offset + end[0];
    }

    /**
     * Returns the string matching the given subexpression.  The subexpressions
     * are indexed starting with one, not zero.  That is, the subexpression
     * identified by the first set of parentheses in a regular expression
     * could be retrieved from an REMatch by calling match.toString(1).
     *
     * @param sub Index of the subexpression.
     */
    public String toString(int sub) {
        if ((sub >= start.length) || sub < 0) throw new IndexOutOfBoundsException("No group " + sub);
        if (start[sub] == -1) return null;
        return (matchedText.substring(start[sub], end[sub]));
    }

    /** 
     * Returns the index within the input string used to generate this match
     * where subexpression number <i>sub</i> begins, or <code>-1</code> if
     * the subexpression does not exist.  The initial position is zero.
     *
     * @param sub Subexpression index
     * @deprecated Use getStartIndex(int) instead.
     */
    public int getSubStartIndex(int sub) {
        if (sub >= start.length) return -1;
        int x = start[sub];
        return (x == -1) ? x : offset + x;
    }

    /** 
     * Returns the index within the input string used to generate this match
     * where subexpression number <i>sub</i> begins, or <code>-1</code> if
     * the subexpression does not exist.  The initial position is zero.
     *
     * @param sub Subexpression index
     * @since edu.vub.util.regexp 1.1.0
     */
    public int getStartIndex(int sub) {
        if (sub >= start.length) return -1;
        int x = start[sub];
        return (x == -1) ? x : offset + x;
    }

    /** 
     * Returns the index within the input string used to generate this match
     * where subexpression number <i>sub</i> ends, or <code>-1</code> if
     * the subexpression does not exist.  The initial position is zero.
     *
     * @param sub Subexpression index
     * @deprecated Use getEndIndex(int) instead
     */
    public int getSubEndIndex(int sub) {
        if (sub >= start.length) return -1;
        int x = end[sub];
        return (x == -1) ? x : offset + x;
    }

    /** 
     * Returns the index within the input string used to generate this match
     * where subexpression number <i>sub</i> ends, or <code>-1</code> if
     * the subexpression does not exist.  The initial position is zero.
     *
     * @param sub Subexpression index
     */
    public int getEndIndex(int sub) {
        if (sub >= start.length) return -1;
        int x = end[sub];
        return (x == -1) ? x : offset + x;
    }

    /**
     * Substitute the results of this match to create a new string.
     * This is patterned after PERL, so the tokens to watch out for are
     * <code>$0</code> through <code>$9</code>.  <code>$0</code> matches
     * the full substring matched; <code>$<i>n</i></code> matches
     * subexpression number <i>n</i>.
     * <code>$10, $11, ...</code> may match the 10th, 11th, ... subexpressions
     * if such subexpressions exist.
     *
     * @param input A string consisting of literals and <code>$<i>n</i></code> tokens.
     */
    public String substituteInto(String input) {
        StringBuffer output = new StringBuffer();
        int pos;
        for (pos = 0; pos < input.length() - 1; pos++) {
            if ((input.charAt(pos) == '$') && (Character.isDigit(input.charAt(pos + 1)))) {
                int val = Character.digit(input.charAt(++pos), 10);
                int pos1 = pos + 1;
                while (pos1 < input.length() && Character.isDigit(input.charAt(pos1))) {
                    int val1 = val * 10 + Character.digit(input.charAt(pos1), 10);
                    if (val1 >= start.length) break;
                    pos1++;
                    val = val1;
                }
                pos = pos1 - 1;
                if (val < start.length) {
                    output.append(toString(val));
                }
            } else output.append(input.charAt(pos));
        }
        if (pos < input.length()) output.append(input.charAt(pos));
        return output.toString();
    }

    static class REMatchList {

        REMatch head;

        REMatch tail;

        REMatchList() {
            head = tail = null;
        }

        void addTail(REMatch newone) {
            if (head == null) {
                head = newone;
                tail = newone;
            } else {
                tail.next = newone;
            }
            while (tail.next != null) {
                tail = tail.next;
            }
        }
    }
}
