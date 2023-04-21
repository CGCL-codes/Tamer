package jaxlib.tcol.tdouble;

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
 * Abstract implementation of the <tt>DoubleMap</tt> interface.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractDoubleMap.java 1044 2004-04-06 16:37:29Z joerg_wassmer $.00
 */
public abstract class AbstractDoubleMap<V> extends AbstractTMap<Double, V> implements DoubleMap<V> {

    protected DoubleSet keys;

    protected AbstractDoubleMap() {
        super();
    }

    /**
   * TODO
   */
    public abstract boolean containsKey(double key);

    public abstract V get(double key);

    public abstract XSet<DoubleMap.Entry<V>> entries();

    protected AbstractDoubleMap<V> clone() throws CloneNotSupportedException {
        AbstractDoubleMap<V> clone = (AbstractDoubleMap) super.clone();
        clone.keys = null;
        return clone;
    }

    public boolean containsKey(Object key) {
        return (key instanceof Double) && containsKey(((Double) key).doubleValue());
    }

    public V get(Object key) {
        if (key instanceof Double) return get(((Double) key).doubleValue()); else return null;
    }

    public DoubleSet keys() {
        DoubleSet keys = this.keys;
        if (keys == null) this.keys = keys = new AbstractDoubleMap.DefaultKeySet();
        return keys;
    }

    public XSet<Double> keySet() {
        if (super.keySet == null) super.keySet = new DoubleBoxSet(keys());
        return super.keySet;
    }

    public V put(double key, V value) {
        throw new UnsupportedOperationException();
    }

    public V put(Double key, V value) {
        return put(key.doubleValue(), value);
    }

    public void putAll(Map<? extends Double, ? extends V> source) {
        if (source == this) return;
        int remaining = source.size();
        if (remaining == 0) return;
        if (source instanceof DoubleMap) {
            XSet<DoubleMap.Entry<V>> entries = ((DoubleMap) source).entrySet();
            boolean askNext = !entries.isSizeStable();
            Iterator<DoubleMap.Entry<V>> it = entries.iterator();
            while (askNext ? it.hasNext() : (remaining > 0)) {
                DoubleMap.Entry<V> e = it.next();
                put(e.key(), e.getValue());
                remaining--;
            }
        } else {
            Set<Map.Entry<? extends Double, ? extends V>> entries = (Set) source.entrySet();
            Iterator<Map.Entry<? extends Double, ? extends V>> it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry<? extends Double, ? extends V> e = it.next();
                put(e.getKey(), e.getValue());
            }
        }
    }

    public V remove(Object key) {
        return (key instanceof Double) ? remove(((Double) key).doubleValue()) : null;
    }

    private abstract class AbstractIteratorImpl extends Object {

        final Iterator<DoubleMap.Entry<V>> entryIterator = AbstractDoubleMap.this.entries().iterator();

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

    protected abstract class AbstractEntrySet<V, E extends DoubleMap.Entry<V>> extends AbstractTMap.AbstractEntrySet<Double, V, E> {

        AbstractEntrySet() {
            super();
        }
    }

    protected class DefaultKeySet extends AbstractDoubleSet {

        protected DefaultKeySet() {
            super();
        }

        @Overrides
        public AccessTypeSet accessTypes() {
            return AbstractDoubleMap.this.accessTypes().except(AccessType.ADD).except(AccessType.SET);
        }

        @Overrides
        public boolean add(double key) {
            throw new UnsupportedOperationException();
        }

        @Overrides
        public int capacity() {
            return AbstractDoubleMap.this.capacity();
        }

        @Overrides
        public void clear() {
            AbstractDoubleMap.this.clear();
        }

        @Overrides
        public boolean contains(double e) {
            return AbstractDoubleMap.this.containsKey(e);
        }

        @Overrides
        public int freeCapacity() {
            return AbstractDoubleMap.this.freeCapacity();
        }

        @Overrides
        public DoubleIterator iterator() {
            return new DefaultKeySet.IteratorImpl();
        }

        @Overrides
        public boolean remove(double e) {
            int size = AbstractDoubleMap.this.size();
            AbstractDoubleMap.this.remove(e);
            return size > AbstractDoubleMap.this.size();
        }

        @Overrides
        public int size() {
            return AbstractDoubleMap.this.size();
        }

        @Overrides
        public int trimCapacity(int newCapacity) {
            return (newCapacity < this.capacity()) ? AbstractDoubleMap.this.trimCapacity(newCapacity) : this.capacity();
        }

        private final class IteratorImpl extends AbstractIteratorImpl implements DoubleIterator {

            IteratorImpl() {
                super();
            }

            public double next() {
                return this.entryIterator.next().key();
            }

            public double nextDouble() {
                return next();
            }
        }
    }
}
