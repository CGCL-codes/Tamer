package com.spicesoft.clientobjects.core.collections;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.spicesoft.clientobjects.core.SimplePropertyChangeListenerSupport;
import com.spicesoft.clientobjects.core.collections.VariableLengthIndexedPropertyChangeEvent.EventType;

public class ObservableList<E> implements List<E>, SimplePropertyChangeListenerSupport {

    private static final String ELEMENT = "element";

    private List<E> original;

    private PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);

    public ObservableList(List<E> original) {
        this.original = original;
    }

    public boolean add(E o) {
        int index = this.original.size();
        boolean result = this.original.add(o);
        if (result) {
            firePropertyChange(null, o, index, index, VariableLengthIndexedPropertyChangeEvent.EventType.INSERT);
        }
        return result;
    }

    public void add(int index, E o) {
        this.original.add(index, o);
        firePropertyChange(null, o, index, index, VariableLengthIndexedPropertyChangeEvent.EventType.INSERT);
    }

    public boolean addAll(Collection<? extends E> c) {
        int startIndex = this.original.size();
        int endIndex = startIndex + c.size() - 1;
        boolean result = this.original.addAll(c);
        if (result) {
            firePropertyChange(null, null, startIndex, endIndex, VariableLengthIndexedPropertyChangeEvent.EventType.INSERT);
        }
        return result;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        int endIndex = index + c.size() - 1;
        boolean result = this.original.addAll(index, c);
        if (result) {
            firePropertyChange(null, null, index, endIndex, VariableLengthIndexedPropertyChangeEvent.EventType.INSERT);
        }
        return result;
    }

    public void clear() {
        int endIndex = this.original.size() - 1;
        this.original.clear();
        firePropertyChange(null, null, 0, endIndex, VariableLengthIndexedPropertyChangeEvent.EventType.DELETE);
    }

    public boolean contains(Object o) {
        return this.original.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.original.containsAll(c);
    }

    public E get(int index) {
        return this.original.get(index);
    }

    public int indexOf(Object o) {
        return this.original.indexOf(o);
    }

    public boolean isEmpty() {
        return this.original.isEmpty();
    }

    /**
   * TODO: Removes through iterator should also be tracked
   */
    public Iterator<E> iterator() {
        return this.original.iterator();
    }

    public int lastIndexOf(Object o) {
        return this.original.lastIndexOf(0);
    }

    /**
   * TODO: Removes through iterator should also be tracked
   */
    public ListIterator<E> listIterator() {
        return this.original.listIterator();
    }

    /**
   * TODO: Removes through iterator should also be tracked
   */
    public ListIterator<E> listIterator(int index) {
        return this.original.listIterator(index);
    }

    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        int index = this.original.indexOf(o);
        boolean result = this.original.remove(o);
        if (result) {
            firePropertyChange((E) o, null, index, index, EventType.DELETE);
        }
        return result;
    }

    public E remove(int index) {
        E result = this.original.remove(index);
        firePropertyChange(result, null, index, index, EventType.DELETE);
        return result;
    }

    public boolean removeAll(Collection<?> c) {
        boolean result = this.original.removeAll(c);
        if (result) {
            firePropertyChange(null, null, -1, -1, EventType.DELETE);
        }
        return result;
    }

    public boolean retainAll(Collection<?> c) {
        boolean result = this.original.retainAll(c);
        if (result) {
            firePropertyChange(null, null, -1, -1, EventType.DELETE);
        }
        return result;
    }

    public E set(int index, E o) {
        E result = this.original.set(index, o);
        firePropertyChange(result, o, index, index, EventType.MODIFY);
        return result;
    }

    public int size() {
        return this.original.size();
    }

    public List<E> subList(int index0, int index1) {
        return new ObservableList<E>(this.original.subList(index0, index1));
    }

    public Object[] toArray() {
        return this.original.toArray();
    }

    public <T> T[] toArray(T[] arr) {
        return this.original.toArray(arr);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcSupport.addPropertyChangeListener(ELEMENT, propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.pcSupport.removePropertyChangeListener(ELEMENT, propertyChangeListener);
    }

    private void firePropertyChange(E oldValue, E newValue, int startIndex, int endIndex, EventType type) {
        this.pcSupport.firePropertyChange(new VariableLengthIndexedPropertyChangeEvent(this, ELEMENT, oldValue, newValue, startIndex, endIndex, type));
    }
}
