package info.olteanu.utils;

import java.util.*;

public class RangeIterator implements Iterator<Integer> {

    /**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
    public boolean hasNext() {
        return cursor <= end;
    }

    /**
	 * Returns the next element in the iteration.  Calling this method
	 * repeatedly until the {@link #hasNext()} method returns false will
	 * return each element in the underlying collection exactly once.
	 * @return the next element in the iteration.
	 * @exception NoSuchElementException iteration has no more elements.
	 */
    public Integer next() {
        if (cursor > end) throw new NoSuchElementException("End of range");
        return cursor++;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private int cursor;

    private final int end;

    public RangeIterator(int start, int end) {
        this.cursor = start;
        this.end = end;
    }
}
