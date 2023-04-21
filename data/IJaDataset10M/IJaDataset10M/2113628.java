package br.net.woodstock.rockframework.collection;

import java.util.Iterator;
import br.net.woodstock.rockframework.util.Assert;

class DelegateIterator<E> implements Iterator<E> {

    private Iterator<E> iterator;

    public DelegateIterator(final Iterator<E> iterator) {
        super();
        Assert.notNull(iterator, "iterator");
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public E next() {
        return this.iterator.next();
    }

    @Override
    public void remove() {
        this.iterator.remove();
    }

    @Override
    public String toString() {
        return this.iterator.toString();
    }
}
