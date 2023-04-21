package org.progeeks.util;

import java.util.*;

/**
 *  Combines several iterators into one iterator view.
 *  How the next iterator is retrieved is customizable
 *  by subclasses.  The default implementation assumes an
 *  iterator of iterators.
 *
 *  @version   $Revision: 1.4 $
 *  @author    Paul Speed
 */
public class CompositeIterator implements CloseableIterator {

    private Iterator mainIterator;

    private Iterator last;

    private Iterator current;

    private boolean removed = false;

    public CompositeIterator(Iterator mainIterator) {
        this.mainIterator = mainIterator;
    }

    /**
     *  If the current iterator is a CloseableIterator then it is
     *  closed.  It also closes the main iterator if it is closeable.
     */
    public void close() {
        setLastIterator(null);
        if (current instanceof CloseableIterator) {
            ((CloseableIterator) current).close();
        }
        if (mainIterator instanceof CloseableIterator) {
            ((CloseableIterator) mainIterator).close();
        }
    }

    protected Iterator getCurrentIterator() {
        return (current);
    }

    protected Iterator getLastIterator() {
        return (last);
    }

    protected Iterator setLastIterator(Iterator last) {
        Iterator oldLast = last;
        this.last = last;
        if (oldLast instanceof CloseableIterator) {
            ((CloseableIterator) oldLast).close();
        }
        return oldLast;
    }

    /**
     *  Returns true if an entry has been removed but next()
     *  hasn't been called yet.
     */
    protected boolean isRemoved() {
        return (removed);
    }

    /**
     *  Returns the iterator for the specified item as retrieved
     *  from the main iterator.  The default implementation casts
     *  it to Iterator and returns it.  A return of null will
     *  cause the composite iterator to try the next entry.
     */
    protected Iterator getIterator(Object item) {
        return ((Iterator) item);
    }

    /**
     *  Makes sure the current iterator is valid and retrives
     *  the next one as necessary.
     */
    protected void refresh() {
        if (current != null && current.hasNext()) {
            setLastIterator(null);
            return;
        }
        Iterator oldLast = setLastIterator(current);
        while (current == null || !current.hasNext()) {
            if (!mainIterator.hasNext()) break;
            if ((current instanceof CloseableIterator) && (current != oldLast)) {
                ((CloseableIterator) current).close();
            }
            current = getIterator(mainIterator.next());
        }
        if (current != null && !current.hasNext()) current = null;
    }

    public boolean hasNext() {
        if (current != null && current.hasNext()) return (true);
        refresh();
        if (current == null) return (false);
        return (current.hasNext());
    }

    public Object next() {
        removed = false;
        refresh();
        if (current == null) throw new NoSuchElementException("No more elements.");
        return (current.next());
    }

    public void remove() {
        if (isRemoved()) throw new IllegalStateException("Item has already been removed.");
        if (last != null) last.remove(); else if (current != null) current.remove();
        removed = true;
    }
}
