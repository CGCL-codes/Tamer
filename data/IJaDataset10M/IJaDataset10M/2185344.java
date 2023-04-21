package de.jaret.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Peter Kliem
 * @version $id$
 */
public class WatchableArrayList implements List {

    private List<ListWatcher> _listeners;

    private List _content;

    /**
     * 
     */
    public WatchableArrayList() {
        _content = new ArrayList();
    }

    public void addListWatcher(ListWatcher watcher) {
        if (_listeners == null) {
            _listeners = new ArrayList<ListWatcher>();
        }
        _listeners.add(watcher);
    }

    public void removeListWatcher(ListWatcher watcher) {
        if (_listeners != null) {
            _listeners.remove(watcher);
        }
    }

    private void fireElementAdded(Object o) {
        if (_listeners != null) {
            for (ListWatcher watcher : _listeners) {
                watcher.elementAdded(o);
            }
        }
    }

    private void fireElementRemoved(Object o) {
        if (_listeners != null) {
            for (ListWatcher watcher : _listeners) {
                watcher.elementRemoved(o);
            }
        }
    }

    public int size() {
        return _content.size();
    }

    public boolean isEmpty() {
        return _content.isEmpty();
    }

    public boolean contains(Object o) {
        return _content.contains(o);
    }

    public Iterator iterator() {
        return _content.iterator();
    }

    public Object[] toArray() {
        return _content.toArray();
    }

    public Object[] toArray(Object[] a) {
        return _content.toArray(a);
    }

    public boolean add(Object o) {
        boolean result = _content.add(o);
        fireElementAdded(o);
        return result;
    }

    public boolean remove(Object o) {
        boolean wasInList = _content.contains(o);
        boolean result = _content.remove(o);
        if (wasInList) {
            fireElementRemoved(o);
        }
        return result;
    }

    public boolean containsAll(Collection c) {
        return _content.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return _content.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return _content.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return _content.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return _content.retainAll(c);
    }

    public void clear() {
        _content.clear();
    }

    public Object get(int index) {
        return _content.get(index);
    }

    public Object set(int index, Object element) {
        return _content.set(index, element);
    }

    public void add(int index, Object element) {
        _content.add(index, element);
        fireElementAdded(element);
    }

    public Object remove(int index) {
        Object o = _content.remove(index);
        fireElementRemoved(o);
        return o;
    }

    public int indexOf(Object o) {
        return _content.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return _content.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return _content.listIterator();
    }

    public ListIterator listIterator(int index) {
        return _content.listIterator(index);
    }

    public List subList(int fromIndex, int toIndex) {
        return _content.subList(fromIndex, toIndex);
    }
}
