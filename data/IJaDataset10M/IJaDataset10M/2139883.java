package net.sf.saxon.sort;

/**
 * An iterator over a sequence of integers held in an array. The array
 * may either be exactly the right size, or may be terminated by an end-of-sequence value.
 *
 * <p>This data structure is generally used for a sequence of namespace codes.</p>
 */
public class TerminatedIntIterator implements IntIterator {

    int[] values;

    int index;

    int terminator = -1;

    /**
     * Construct an iterator over a sequence of integers held in an array, with
     * the value -1 acting as the terminator
     * @param values the sequence of integers
     */
    public TerminatedIntIterator(int[] values) {
        this.values = values;
        index = 0;
    }

    /**
     * Construct an iterator over a sequence of integers held in an array, with
     * a specified value acting as the terminator
     * @param values the sequence of integers
     * @param terminator the terminator value
     */
    public TerminatedIntIterator(int[] values, int terminator) {
        this.values = values;
        index = 0;
        this.terminator = terminator;
    }

    /**
     * Test whether there are any more integers in the sequence
     */
    public boolean hasNext() {
        return index < values.length - 1 && values[index] != terminator;
    }

    /**
     * Return the next integer in the sequence. The result is undefined unless hasNext() has been called
     * and has returned true.
     */
    public int next() {
        return values[index++];
    }
}
