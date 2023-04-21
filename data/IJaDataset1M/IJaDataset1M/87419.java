package org.databene.commons.iterator;

/**
 * Proxy for a {@link BidirectionalIterator}.<br/>
 * <br/>
 * Created: 12.05.2007 23:18:31
 * @author Volker Bergmann
 */
public abstract class BidirectionalIteratorProxy<E> implements BidirectionalIterator<E> {

    protected BidirectionalIterator<E> realIterator;

    public BidirectionalIteratorProxy(BidirectionalIterator<E> realIterator) {
        this.realIterator = realIterator;
    }

    public E first() {
        return realIterator.first();
    }

    public boolean hasPrevious() {
        return realIterator.hasPrevious();
    }

    public E previous() {
        return realIterator.previous();
    }

    public E last() {
        return realIterator.last();
    }

    public boolean hasNext() {
        return realIterator.hasNext();
    }

    public E next() {
        return realIterator.next();
    }

    public void remove() {
        realIterator.remove();
    }
}
