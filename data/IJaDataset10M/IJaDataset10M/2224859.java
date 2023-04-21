package net.sourceforge.freejava.collection.iterator;

import java.util.Iterator;

/**
 * Create the iterator until the first time be accessed.
 */
public abstract class LazyIterator<T> implements Iterator<T> {

    private Iterator<T> contentIter;

    @Override
    public boolean hasNext() {
        if (contentIter == null) contentIter = createIterator();
        return contentIter.hasNext();
    }

    @Override
    public T next() {
        if (contentIter == null) contentIter = createIterator();
        return contentIter.next();
    }

    @Override
    public void remove() {
        if (contentIter == null) contentIter = createIterator();
        contentIter.remove();
    }

    protected abstract Iterator<T> createIterator();
}
