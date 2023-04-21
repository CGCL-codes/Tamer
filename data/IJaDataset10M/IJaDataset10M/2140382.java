package jaxlib.tcol.tint;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import jaxlib.col.AbstractXCollection;
import jaxlib.col.AbstractXSet;
import jaxlib.util.AccessType;
import jaxlib.util.AccessTypeSet;
import jaxlib.col.XCollection;
import jaxlib.col.XSet;
import jaxlib.tcol.AbstractTMap;

/**
 * Abstract implementation of the <tt>IntMap</tt> interface.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractIntMap.java 1044 2004-04-06 16:37:29Z joerg_wassmer $.00
 */
public abstract class AbstractIntMap<V> extends AbstractTMap<Integer, V> implements IntMap<V> {

    protected IntSet keys;

    protected AbstractIntMap() {
        super();
    }

    protected AbstractIntMap<V> clone() throws CloneNotSupportedException {
        AbstractIntMap<V> clone = (AbstractIntMap<V>) super.clone();
        clone.keys = null;
        return clone;
    }

    public boolean containsKey(Object key) {
        return (key instanceof Integer) && containsKey(((Integer) key).intValue());
    }

    public V get(Object key) {
        if (key instanceof Integer) return get(((Integer) key).intValue()); else return null;
    }

    public IntSet keys() {
        IntSet keys = this.keys;
        if (keys == null) this.keys = keys = new AbstractIntMap.DefaultKeySet();
        return keys;
    }

    public XSet<Integer> keySet() {
        if (super.keySet == null) super.keySet = new IntBoxSet(keys());
        return super.keySet;
    }

    public V put(int key, V value) {
        throw new UnsupportedOperationException();
    }

    public V put(Integer key, V value) {
        return put(key.intValue(), value);
    }

    public void putAll(Map<? extends Integer, ? extends V> source) {
        if (source == this) return;
        int remaining = source.size();
        if (remaining == 0) return;
        if (source instanceof IntMap) {
            XSet<IntMap.Entry<V>> entries = ((IntMap) source).entrySet();
            boolean askNext = !entries.isSizeStable();
            Iterator<IntMap.Entry<V>> it = entries.iterator();
            while (askNext ? it.hasNext() : (remaining > 0)) {
                IntMap.Entry<V> e = it.next();
                put(e.key(), e.getValue());
                remaining--;
            }
        } else {
            Set<Map.Entry<? extends Integer, ? extends V>> entries = (Set) source.entrySet();
            Iterator<Map.Entry<? extends Integer, ? extends V>> it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry<? extends Integer, ? extends V> e = it.next();
                put(e.getKey(), e.getValue());
            }
        }
    }

    public V remove(Object key) {
        return (key instanceof Integer) ? remove(((Integer) key).intValue()) : null;
    }

    private abstract class AbstractIteratorImpl extends Object {

        final Iterator<IntMap.Entry<V>> entryIterator = AbstractIntMap.this.entries().iterator();

        AbstractIteratorImpl() {
            super();
        }

        public final boolean hasNext() {
            return this.entryIterator.hasNext();
        }

        public final void remove() {
            this.entryIterator.remove();
        }
    }

    protected abstract class AbstractEntrySet<V, E extends IntMap.Entry<V>> extends AbstractTMap.AbstractEntrySet<Integer, V, E> {

        AbstractEntrySet() {
            super();
        }
    }

    protected class DefaultKeySet extends AbstractIntSet {

        protected DefaultKeySet() {
            super();
        }

        @Overrides
        public AccessTypeSet accessTypes() {
            return AbstractIntMap.this.accessTypes().except(AccessType.ADD).except(AccessType.SET);
        }

        @Overrides
        public boolean add(int key) {
            throw new UnsupportedOperationException();
        }

        @Overrides
        public int capacity() {
            return AbstractIntMap.this.capacity();
        }

        @Overrides
        public void clear() {
            AbstractIntMap.this.clear();
        }

        @Overrides
        public boolean contains(int e) {
            return AbstractIntMap.this.containsKey(e);
        }

        @Overrides
        public int freeCapacity() {
            return AbstractIntMap.this.freeCapacity();
        }

        @Overrides
        public IntIterator iterator() {
            return new DefaultKeySet.IteratorImpl();
        }

        @Overrides
        public boolean remove(int e) {
            int size = AbstractIntMap.this.size();
            AbstractIntMap.this.remove(e);
            return size > AbstractIntMap.this.size();
        }

        @Overrides
        public int size() {
            return AbstractIntMap.this.size();
        }

        @Overrides
        public int trimCapacity(int newCapacity) {
            return (newCapacity < this.capacity()) ? AbstractIntMap.this.trimCapacity(newCapacity) : this.capacity();
        }

        private final class IteratorImpl extends AbstractIteratorImpl implements IntIterator {

            IteratorImpl() {
                super();
            }

            public int next() {
                return this.entryIterator.next().key();
            }

            public double nextDouble() {
                return next();
            }
        }
    }
}
