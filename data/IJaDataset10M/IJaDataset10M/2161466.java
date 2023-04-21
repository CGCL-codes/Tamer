package com.amazon.carbonado.cursor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import com.amazon.carbonado.Cursor;

/**
 * Special cursor implementation that is empty.
 *
 * @author Brian S O'Neill
 * @see SingletonCursor
 */
public class EmptyCursor<S> implements Cursor<S> {

    private static final Cursor EMPTY_CURSOR = new EmptyCursor();

    /**
     * Returns the empty cursor instance.
     */
    @SuppressWarnings("unchecked")
    public static <S> Cursor<S> the() {
        return EMPTY_CURSOR;
    }

    EmptyCursor() {
    }

    /**
     * Does nothing.
     */
    public void close() {
    }

    /**
     * Always returns false.
     */
    public boolean hasNext() {
        return false;
    }

    /**
     * Always throws NoSuchElementException.
     */
    public S next() {
        throw new NoSuchElementException();
    }

    /**
     * Always returns 0.
     */
    public int skipNext(int amount) {
        return 0;
    }

    /**
     * Performs no copy and always returns 0.
     */
    public int copyInto(Collection<? super S> c) {
        return 0;
    }

    /**
     * Performs no copy and always returns 0.
     */
    public int copyInto(Collection<? super S> c, int limit) {
        return 0;
    }

    /**
     * Always returns an empty list.
     */
    public List<S> toList() {
        return Collections.emptyList();
    }

    /**
     * Always returns an empty list.
     */
    public List<S> toList(int limit) {
        return Collections.emptyList();
    }
}
