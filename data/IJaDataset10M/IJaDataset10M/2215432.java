package jflex;

/**
 * Simple pair of integers.
 *
 * Used in NFA to represent a partial NFA by its start and end state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.5, $Revision: 586 $, $Date: 2010-03-07 03:59:36 -0500 (Sun, 07 Mar 2010) $
 */
final class IntPair {

    int start;

    int end;

    IntPair(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int hashCode() {
        return end + (start << 8);
    }

    public boolean equals(Object o) {
        if (o instanceof IntPair) {
            IntPair p = (IntPair) o;
            return start == p.start && end == p.end;
        }
        return false;
    }

    public String toString() {
        return "(" + start + "," + end + ")";
    }
}
