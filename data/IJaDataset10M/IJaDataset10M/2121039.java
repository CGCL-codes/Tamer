package jaxlib.col;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.Nonnull;
import jaxlib.lang.Objects;
import jaxlib.util.AccessTypeSet;

/**
 * Abstract implementation of the <tt>XMap</tt> interface.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractXMap.java 3035 2012-01-04 06:49:57Z joerg_wassmer $
 */
@SuppressWarnings({ "unchecked", "element-type-mismatch" })
public abstract class AbstractXMap<K, V> extends Object implements XMap<K, V> {

    protected AbstractXMap() {
        super();
    }

    @Override
    public abstract XSet<Map.Entry<K, V>> entrySet();

    @Override
    public abstract int size();

    @Override
    public AccessTypeSet accessTypes() {
        return AccessTypeSet.ALL;
    }

    @Override
    public V apply(final K key) {
        return get(key);
    }

    @Override
    public int capacity() {
        return size();
    }

    @Override
    public void clear() {
        final Iterator it = entrySet().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Override
    public boolean contains(final Object key, final Object value) {
        if (value == null) return (get(key) == null) && containsKey(key); else {
            final Object v = get(key);
            return (v != null) && value.equals(v);
        }
    }

    @Override
    public boolean containsId(final Object key) {
        final Map.Entry<K, V> e = getEntry(key);
        return (e != null) && (e.getKey() == key);
    }

    @Override
    public boolean containsKey(final Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(final Object value) {
        for (final Map.Entry<K, V> e : entrySet()) {
            if (Objects.equals(value, e.getValue())) return true;
        }
        return false;
    }

    @Override
    public void ensureCapacity(final int minCapacity) {
        if (minCapacity > capacity()) trimCapacity(minCapacity);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        final Map<?, ?> b = (Map<?, ?>) o;
        final int size = size();
        if (size == 0) return b.isEmpty();
        if (b.size() != size) return false;
        if (this instanceof SortedMap) return equalsSorted(b);
        return equals0(b);
    }

    private boolean equals0(final Map<?, ?> b) {
        for (final Map.Entry<?, ?> e : b.entrySet()) {
            if (!contains(e.getKey(), e.getValue())) return false;
        }
        return true;
    }

    private boolean equalsSorted(final Map<?, ?> b) {
        for (final Map.Entry<?, ?> e : b.entrySet()) {
            final Object k = e.getKey();
            final Object v = e.getValue();
            try {
                if (!contains(k, v)) return false;
            } catch (final ClassCastException ex) {
                if (k == null) throw ex;
                return false;
            } catch (final NullPointerException ex) {
                if (k != null) throw ex;
                return false;
            }
        }
        return true;
    }

    @Override
    public int freeCapacity() {
        return Math.max(0, capacity() - size());
    }

    @Override
    public V get(final Object key) {
        final Map.Entry<K, V> entry = getEntry(key);
        return (entry == null) ? null : entry.getValue();
    }

    @Override
    public Map.Entry<K, V> getEntry(final Object key) {
        for (final Map.Entry<K, V> e : entrySet()) {
            if (Objects.equals(key, e.getKey())) return e;
        }
        return null;
    }

    @Override
    public V getId(final Object key) {
        final Map.Entry<K, V> e = getEntry(key);
        return ((e != null) && (e.getKey() == key)) ? e.getValue() : null;
    }

    @Override
    public int hashCode() {
        int h = 0;
        final Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        while (it.hasNext()) h += it.next().hashCode();
        return h;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isSizeStable() {
        return false;
    }

    @Override
    public V put(final K key, final V value) {
        throw unsupportedByClass();
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> src) {
        if (src != this) for (final Map.Entry<? extends K, ? extends V> e : src.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        final V oldValue = get(key);
        if (((oldValue == null) || !containsKey(key)) && (put(key, value) != null)) throw new ConcurrentModificationException();
        return oldValue;
    }

    @Override
    public V putIfAbsentAndGet(final K key, final V value) {
        final V old = putIfAbsent(key, value);
        if ((old != null) || (value == old)) return old;
        final V v = get(key);
        if ((v == null) && (old != null) && (value != null)) throw new ConcurrentModificationException();
        return v;
    }

    @Override
    public V remove(final Object key) {
        for (final Iterator<Map.Entry<K, V>> it = entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<K, V> e = it.next();
            if (Objects.equals(key, e.getKey())) {
                final V v = e.getValue();
                it.remove();
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        final V oldValue = get(key);
        if (((oldValue == null) && !containsKey(key)) || !Objects.equals(value, oldValue)) return false;
        if (!Objects.equals(oldValue, remove(key))) throw new ConcurrentModificationException();
        return true;
    }

    @Override
    public V removeId(final Object key) {
        Map.Entry<K, V> e = getEntry(key);
        if ((e == null) || (e.getKey() != key)) return null;
        final V v = e.getValue();
        e = null;
        final V removed = remove(key);
        if (!Objects.equals(v, removed)) throw new ConcurrentModificationException();
        return removed;
    }

    @Override
    public V replace(final K key, final V value) {
        final V oldValue = get(key);
        if ((oldValue == null) && !containsKey(key)) return null;
        if (!Objects.equals(oldValue, put(key, value))) throw new ConcurrentModificationException();
        return oldValue;
    }

    @Override
    public boolean replace(final K key, final V value, final V newValue) {
        final V oldValue = get(key);
        if (((oldValue == null) && !containsKey(key)) || !Objects.equals(value, oldValue)) return false;
        if (!Objects.equals(oldValue, put(key, newValue))) throw new ConcurrentModificationException();
        return true;
    }

    @Override
    public String toString() {
        return Maps.toString(this);
    }

    @Override
    public int trimCapacity(final int newCapacity) {
        return capacity();
    }

    @Override
    public void trimToSize() {
        trimCapacity(size());
    }

    @Nonnull
    final UnsupportedOperationException unsupportedByClass() {
        return new UnsupportedOperationException(getClass().toString().concat(" is not supporting the requested operation"));
    }
}
