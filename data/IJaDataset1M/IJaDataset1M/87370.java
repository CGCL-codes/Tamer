package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;

/**
 * Basic implementation of a {@link HeaderIterator}.
 * 
 * @version $Revision: 744527 $
 *
 * @since 4.0
 */
public class BasicHeaderIterator implements HeaderIterator {

    /**
     * An array of headers to iterate over.
     * Not all elements of this array are necessarily part of the iteration.
     * This array will never be modified by the iterator.
     * Derived implementations are expected to adhere to this restriction.
     */
    protected final Header[] allHeaders;

    /**
     * The position of the next header in {@link #allHeaders allHeaders}.
     * Negative if the iteration is over.
     */
    protected int currentIndex;

    /**
     * The header name to filter by.
     * <code>null</code> to iterate over all headers in the array.
     */
    protected String headerName;

    /**
     * Creates a new header iterator.
     *
     * @param headers   an array of headers over which to iterate
     * @param name      the name of the headers over which to iterate, or
     *                  <code>null</code> for any
     */
    public BasicHeaderIterator(Header[] headers, String name) {
        if (headers == null) {
            throw new IllegalArgumentException("Header array must not be null.");
        }
        this.allHeaders = headers;
        this.headerName = name;
        this.currentIndex = findNext(-1);
    }

    /**
     * Determines the index of the next header.
     *
     * @param from      one less than the index to consider first,
     *                  -1 to search for the first header
     *
     * @return  the index of the next header that matches the filter name,
     *          or negative if there are no more headers
     */
    protected int findNext(int from) {
        if (from < -1) return -1;
        final int to = this.allHeaders.length - 1;
        boolean found = false;
        while (!found && (from < to)) {
            from++;
            found = filterHeader(from);
        }
        return found ? from : -1;
    }

    /**
     * Checks whether a header is part of the iteration.
     *
     * @param index     the index of the header to check
     *
     * @return  <code>true</code> if the header should be part of the
     *          iteration, <code>false</code> to skip
     */
    protected boolean filterHeader(int index) {
        return (this.headerName == null) || this.headerName.equalsIgnoreCase(this.allHeaders[index].getName());
    }

    public boolean hasNext() {
        return (this.currentIndex >= 0);
    }

    /**
     * Obtains the next header from this iteration.
     *
     * @return  the next header in this iteration
     *
     * @throws NoSuchElementException   if there are no more headers
     */
    public Header nextHeader() throws NoSuchElementException {
        final int current = this.currentIndex;
        if (current < 0) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.currentIndex = findNext(current);
        return this.allHeaders[current];
    }

    /**
     * Returns the next header.
     * Same as {@link #nextHeader nextHeader}, but not type-safe.
     *
     * @return  the next header in this iteration
     *
     * @throws NoSuchElementException   if there are no more headers
     */
    public final Object next() throws NoSuchElementException {
        return nextHeader();
    }

    /**
     * Removing headers is not supported.
     *
     * @throws UnsupportedOperationException    always
     */
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing headers is not supported.");
    }
}
