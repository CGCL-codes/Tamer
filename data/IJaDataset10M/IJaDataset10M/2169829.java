package pl.org.minions.utils.collections;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Iterator that iterates backwards over a list.
 * @param <Value>
 *            element type of the iterated list
 */
public class ReverseIterator<Value> implements Iterator<Value> {

    private ListIterator<? extends Value> wrappedIterator;

    /**
     * Creates an iterator that iterates backwards over
     * selected list, starting from the last element.
     * @param list
     *            the list to iterate over
     */
    public ReverseIterator(List<? extends Value> list) {
        wrappedIterator = list.listIterator(list.size());
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return wrappedIterator.hasPrevious();
    }

    /** {@inheritDoc} */
    @Override
    public Value next() {
        return wrappedIterator.previous();
    }

    /** {@inheritDoc} */
    @Override
    public void remove() {
        wrappedIterator.remove();
    }
}
