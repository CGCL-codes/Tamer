package de.peathal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class ListenerList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 9812345987234509L;

    private final AbstractProperties prop;

    public ListenerList(AbstractProperties abstractProperties, int init) {
        super(init);
        prop = abstractProperties;
    }

    @Override
    public synchronized boolean remove(Object o) {
        if (super.remove(o)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized E remove(int index) {
        this.prop.change();
        return super.remove(index);
    }

    @Override
    public synchronized boolean add(E o) {
        if (super.add(o)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized void add(int index, E element) {
        this.prop.change();
        super.add(index, element);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        if (super.addAll(index, c)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        if (super.addAll(c)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized void clear() {
        this.prop.change();
        super.clear();
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        if (super.retainAll(c)) {
            this.prop.change();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized E set(int index, E element) {
        this.prop.change();
        return super.set(index, element);
    }
}
