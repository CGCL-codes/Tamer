package org.fgraph.tstore;

import java.util.*;

/**
 *
 *  @version   $Revision: 562 $
 *  @author    Paul Speed
 */
public abstract class AbstractTripleStore<U, V, W> implements TripleStore<U, V, W> {

    private String name;

    private Class<U> firstType;

    private Class<V> secondType;

    private Class<W> thirdType;

    protected AbstractTripleStore(String name, Class<U> u, Class<V> v, Class<W> w) {
        this.name = name;
        this.firstType = u;
        this.secondType = v;
        this.thirdType = w;
    }

    protected Class<U> getFirstType() {
        return firstType;
    }

    protected Class<V> getSecondType() {
        return secondType;
    }

    protected Class<W> getThirdType() {
        return thirdType;
    }

    public abstract void close();

    public abstract int size();

    public abstract Iterator<Triple<U, V, W>> iterator();

    public abstract boolean add(Triple<U, V, W> o);

    public abstract boolean remove(Object o);

    public abstract Triple<U, V, W> get(Object id);

    public abstract Triple<U, V, W> get(U first, V second, W third);

    public abstract TripleStore<U, V, W> find(U first, V second, W third);

    public abstract Triple<U, V, W> replace(Triple<U, V, W> oldValue, Triple<U, V, W> newValue);

    public abstract Triple<U, V, W> create(U first, V second, W third);

    public Triple<U, V, W> replace(Triple<U, V, W> oldValue, U first, V second, W third) {
        return replace(oldValue, new Triple<U, V, W>(first, second, third));
    }

    public Triple<U, V, W> add(U first, V second, W third) {
        Triple<U, V, W> t = new Triple<U, V, W>(first, second, third);
        if (add(t)) return t;
        return null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        throw new UnsupportedOperationException("This TripleStore does not support clear.");
    }

    /**
     *  Default implementation grabs the size before calling clear()
     *  and then returns that size.  Implementations that can provide
     *  more accurate clear information are encouraged to do so.
     */
    public long removeAll() {
        int size = size();
        clear();
        return size;
    }

    @SuppressWarnings("unchecked")
    protected final Triple<U, V, W> cast(Object o) {
        return (Triple<U, V, W>) o;
    }

    public boolean contains(Object o) {
        if (!(o instanceof Triple)) return false;
        Triple<U, V, W> t = cast(o);
        if (t.getId() != null) {
            Triple<U, V, W> loaded = get(t.getId());
            if (loaded == null) return false;
            return loaded.equalValues(t);
        } else {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends Triple<U, V, W>> c) {
        boolean changed = false;
        for (Triple<U, V, W> t : c) {
            if (add(t)) changed = true;
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This TripleStore does not support retainAll.");
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("Cannot convert this TripleStore to an array.");
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Cannot convert this TripleStore to an array.");
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName() + "[ name='" + name + "', ");
        if (firstType == null) sb.append("?, "); else sb.append(firstType.getName() + ", ");
        if (secondType == null) sb.append("?, "); else sb.append(secondType.getName() + ", ");
        if (thirdType == null) sb.append("?, "); else sb.append(thirdType.getName());
        sb.append("]");
        return sb.toString();
    }
}
