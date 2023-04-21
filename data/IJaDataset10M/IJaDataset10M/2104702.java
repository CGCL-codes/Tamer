package de.ifgi.simcat2.reasoner.term;

/**
 * An immutable interval with end and starting index included.
 * 
 * @author Christoph M�lligann
 */
public class Interval {

    public final int start;

    public final int end;

    public final int length;

    /**
	 * @param start the first index to be covered
	 * @param end the last index to be covered
	 */
    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
    }

    public String toString() {
        return "[" + start + " " + end + "]";
    }
}
