package fmx.bo.util;

import java.util.ArrayList;
import java.util.Collection;
import fmx.bo.event.MatrixDataModelListener;
import fmx.gui.MatrixDataModel;

public class MatrixDataModelListenerList<E> extends ArrayList<E> {

    public static final int SIZE_CHANGED = 1;

    public MatrixDataModelListenerList(final MatrixDataModel model) {
        matrixDataModel = model;
    }

    protected void publishChanges(final int key) {
        for (MatrixDataModelListener l : matrixDataModel.getListeners()) {
            switch(key) {
                case SIZE_CHANGED:
                    l.nodeCountChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void add(final int index, final E element) {
        super.add(index, element);
        publishChanges(SIZE_CHANGED);
    }

    @Override
    public boolean add(final E e) {
        boolean result = super.add(e);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean result = super.addAll(c);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        boolean result = super.addAll(index, c);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        publishChanges(SIZE_CHANGED);
    }

    @Override
    public E remove(final int index) {
        E result = super.remove(index);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public boolean remove(final Object o) {
        boolean result = super.remove(o);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        boolean result = super.removeAll(c);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        boolean result = super.retainAll(c);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    @Override
    public E set(final int index, final E e) {
        E result = super.set(index, e);
        publishChanges(SIZE_CHANGED);
        return result;
    }

    private MatrixDataModel matrixDataModel;
}
