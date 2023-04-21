package net.community.chest.util.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @param <E> Type of element being iterated
 * @since Aug 2, 2011 1:12:46 PM
 */
public class EmptyIterator<E> implements Iterator<E> {

    public EmptyIterator() {
        super();
    }

    @Override
    public final boolean hasNext() {
        return false;
    }

    @Override
    public final E next() {
        throw new NoSuchElementException("No elements in empty iterator");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Empty iterator has no elements to remove");
    }
}
