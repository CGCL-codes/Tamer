package org.gbif.biogarage.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import javax.naming.OperationNotSupportedException;

public class LimitedList<E> extends ArrayList<E> {

    private final int listSize;

    public LimitedList(int listSize) {
        this.listSize = listSize;
    }

    @Override
    public boolean add(E o) {
        reduce();
        return super.add(o);
    }

    private void reduce() {
        if (this.size() == listSize) {
            this.remove(0);
        }
    }

    @Override
    public void add(int index, E element) {
        reduce();
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new IllegalArgumentException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new IllegalArgumentException();
    }
}
