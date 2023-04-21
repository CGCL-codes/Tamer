package net.sourceforge.hlm.util.iterator;

import java.util.*;

public class DoubleIterator<T> implements Iterator<T> {

    public DoubleIterator(Iterator<? extends T> first, Iterator<? extends T> second) {
        this.first = first;
        this.second = second;
    }

    public boolean hasNext() {
        if (!this.inSecond && (this.first == null || !this.first.hasNext())) {
            this.inSecond = true;
        }
        if (this.inSecond) {
            return (this.second != null && this.second.hasNext());
        } else {
            return this.first.hasNext();
        }
    }

    public T next() {
        if (this.inSecond) {
            return this.second.next();
        } else {
            return this.first.next();
        }
    }

    public void remove() {
        if (this.inSecond) {
            this.second.remove();
        } else {
            this.first.remove();
        }
    }

    private Iterator<? extends T> first;

    private Iterator<? extends T> second;

    private boolean inSecond;
}
