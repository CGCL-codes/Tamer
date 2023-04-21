package jaxlib.attribute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.io.WriteAbortedException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import jaxlib.lang.Ints;
import jaxlib.thread.lock.SimpleReadWriteLock;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AttributedObject.java 2730 2009-04-21 01:12:29Z joerg_wassmer $
 */
public class AttributedObject extends Object implements Attributable, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    static final int PUT = 1;

    static final int PUT_IF_ABSENT = 2;

    static final int PUT_ID = -PUT;

    static final int PUT_ID_IF_ABSENT = -PUT_IF_ABSENT;

    static final Object IDENTITY = null;

    static final Object INSECURE = Boolean.FALSE;

    static final Object SECURE = Boolean.TRUE;

    static final Class CLASS = Class.class;

    static final Class OBJECT = Object.class;

    static final Class STRING = String.class;

    static final Class UUID = UUID.class;

    static final AttributedObject NO_DEFAULTS = new AttributedObject();

    private static final AtomicReferenceFieldUpdater<AttributedObject, AttributedObject.Impl> atomicImpl = AtomicReferenceFieldUpdater.newUpdater(AttributedObject.class, AttributedObject.Impl.class, "impl");

    static final Object keyType(final Class c) {
        return ((c == STRING) || (c == UUID)) ? SECURE : ((c == OBJECT) || (c == CLASS)) ? IDENTITY : INSECURE;
    }

    private transient Attributed defaultAttributes;

    private transient volatile AttributedObject.Impl impl;

    public AttributedObject() {
        super();
    }

    public AttributedObject(Attributed defaultAttributes) {
        super();
        this.defaultAttributes = defaultAttributes;
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        try {
            this.defaultAttributes = (Attributed) in.readObject();
        } catch (final ObjectStreamException ex) {
        }
        final AttributeMap map = (AttributeMap) in.readObject();
        if (map != null) this.impl = map.impl();
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (shouldSerializeDefaultAttributes()) {
            Attributed defaultAttributes = this.defaultAttributes;
            if (defaultAttributes instanceof Serializable) {
                try {
                    out.writeObject(defaultAttributes);
                } catch (final ObjectStreamException ex) {
                }
            } else {
                out.writeObject(null);
            }
        } else {
            out.writeObject(null);
        }
        if (shouldSerializeAttributes()) {
            final Impl impl = impl();
            if (impl == null) out.writeObject(null); else out.writeObject(impl.attributeMap());
        } else {
            out.writeObject(null);
        }
    }

    final Impl getOrCreateImpl() {
        Impl impl = impl();
        if (impl == null) {
            impl = new Impl();
            if (!AttributedObject.atomicImpl.compareAndSet(this, null, impl)) impl = this.impl;
        }
        return impl;
    }

    final Impl impl() {
        return this.impl;
    }

    protected SimpleReadWriteLock attributeLock() {
        return getOrCreateImpl();
    }

    protected AttributeMap attributeMap() {
        return getOrCreateImpl().attributeMap();
    }

    /**
   * Overridden to copy the underlying datastructure.
   * The default attributes are not copied.
   *
   * @see #getDefaultAttributes()
   */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        final AttributedObject clone = (AttributedObject) super.clone();
        final Impl impl = clone.impl();
        if (impl != null) clone.impl = impl.clone();
        return clone;
    }

    protected Attributed getDefaultAttributes() {
        return this.defaultAttributes;
    }

    protected void setDefaultAttributes(final Attributed defaults) {
        if (defaults == this) throw new IllegalArgumentException("defaults == this");
        if (defaults != null) {
            final Impl impl = impl();
            if ((impl != null) && (defaults == impl.map)) throw new IllegalArgumentException("defaults == this.attributeMap()");
        }
        this.defaultAttributes = defaults;
    }

    protected boolean shouldSerializeAttributes() {
        return true;
    }

    protected boolean shouldSerializeDefaultAttributes() {
        return shouldSerializeAttributes();
    }

    @Override
    public <T> T getAttribute(final Object key) {
        if (key == null) return null;
        final Impl impl = impl();
        if (impl != null) {
            final Class c = key.getClass();
            final Object type = keyType(c);
            final Object v = (type == IDENTITY) ? impl.getId(key, key.hashCode()) : (type == SECURE) ? impl.getSecure(key, key.hashCode()) : impl.getInsecure(key, c, key.hashCode());
            if (v != null) return (T) v;
        }
        final Attributed defaults = this.defaultAttributes;
        return (defaults == null) ? null : (T) defaults.getAttribute(key);
    }

    @Override
    public Object setAttribute(final Object key, final Object value) {
        if (value == null) {
            final Impl impl = impl();
            if (impl == null) return null;
            final Class c = key.getClass();
            final Object type = keyType(c);
            return (type == IDENTITY) ? impl.remove(key, key.hashCode(), true) : (type == SECURE) ? impl.remove(key, key.hashCode(), false) : impl.removeInsecure(key, c, key.hashCode());
        } else {
            final Class c = key.getClass();
            final Object type = keyType(c);
            return (type == IDENTITY) ? getOrCreateImpl().put(key, key.hashCode(), value, PUT_ID) : (type == SECURE) ? getOrCreateImpl().put(key, key.hashCode(), value, PUT) : getOrCreateImpl().putInsecure(key, c, key.hashCode(), value, false);
        }
    }

    @Override
    public Object setAttributeIfAbsent(final Object key, final Object value) {
        if (value == null) {
            final Impl impl = impl();
            if (impl == null) return null;
            final Class c = key.getClass();
            final Object type = keyType(c);
            return (type == IDENTITY) ? impl.getId(key, key.hashCode()) : (type == SECURE) ? impl.getSecure(key, key.hashCode()) : impl.getInsecure(key, c, key.hashCode());
        } else {
            final Class c = key.getClass();
            final Object type = keyType(c);
            return (type == IDENTITY) ? getOrCreateImpl().put(key, key.hashCode(), value, PUT_ID) : (type == SECURE) ? getOrCreateImpl().put(key, key.hashCode(), value, PUT) : getOrCreateImpl().putInsecure(key, c, key.hashCode(), value, true);
        }
    }

    @SuppressWarnings("serial")
    static final class Impl extends SimpleReadWriteLock {

        static final Object REMOVED = new Object();

        static final int INITIAL_TABLE_LENGTH = 4;

        private static final AtomicReferenceFieldUpdater<Impl, AttributeMap> atomicMap = AtomicReferenceFieldUpdater.newUpdater(Impl.class, AttributeMap.class, "map");

        static int cardinalIndex(final int h, final int length) {
            return (h & 0x7FFFFFFF) % length;
        }

        static int indexOfIdenticalKey(final Object k, final int h, final Object[] table) {
            final int length = table.length >> 1;
            int i = cardinalIndex(h, length);
            final int end = ((i == 0) ? length : i) - 2;
            int insertionIndex = -1;
            while (true) {
                final Object e = table[i];
                if (e == null) {
                    return (insertionIndex != -1) ? ~insertionIndex : ~i;
                } else if (e == REMOVED) {
                    if (insertionIndex == -1) insertionIndex = i;
                    continue;
                } else if (k == e) {
                    return i;
                } else if (i != end) {
                    i += 2;
                    if (i == length) i = 0;
                    continue;
                } else {
                    return ~insertionIndex;
                }
            }
        }

        static int indexOfKeyInsecure(final Object k, final Class c, final int h, final Object[] table, final int[] hashCodes) {
            final int length = hashCodes.length;
            int i = cardinalIndex(h, length);
            final int end = ((i == 0) ? length : i) - 2;
            int insertionIndex = -1;
            while (true) {
                final Object e = table[i];
                if (e == null) {
                    return (insertionIndex != -1) ? ~insertionIndex : ~i;
                } else if (e == REMOVED) {
                    if (insertionIndex == -1) insertionIndex = i;
                    continue;
                } else if ((k == e) || ((hashCodes[i] == h) && (k.getClass() == c) && k.equals(e))) {
                    return i;
                } else if (i != end) {
                    i += 2;
                    if (i == length) i = 0;
                    continue;
                } else {
                    return ~insertionIndex;
                }
            }
        }

        static int indexOfKeySecure(final Object k, final int h, final Object[] table, final int[] hashCodes) {
            final int length = hashCodes.length;
            int i = cardinalIndex(h, length);
            final int end = ((i == 0) ? length : i) - 2;
            int insertionIndex = -1;
            while (true) {
                final Object e = table[i];
                if (e == null) {
                    return (insertionIndex != -1) ? ~insertionIndex : ~i;
                } else if (e == REMOVED) {
                    if (insertionIndex == -1) insertionIndex = i;
                    continue;
                } else if ((k == e) || ((hashCodes[i] == h) && k.equals(e))) {
                    return i;
                } else if (i != end) {
                    i += 2;
                    if (i == length) i = 0;
                    continue;
                } else {
                    return ~insertionIndex;
                }
            }
        }

        static void removeSlot(final int index, final Object[] table) {
            table[index] = null;
            table[index << 1] = null;
            final int length = table.length >> 1;
            int i = index;
            if (table[(i = Ints.rollUp(i, length))] == null) {
                table[index] = null;
                while (table[(i = Ints.rollDown(i, length))] == REMOVED) table[i] = null;
            } else {
                table[index] = REMOVED;
            }
        }

        static boolean shouldGrow(final int size, final int length) {
            return size >= ((length <= 16) ? (length - 1) : (length - (length >> 2)));
        }

        static boolean shouldShrink(final int size, final int length) {
            return (size == 0) || (size < ((length <= 16) ? (length - 4) : (length >> 1)));
        }

        transient int[] hashCodes;

        transient int size;

        transient Object[] table;

        transient volatile AttributeMap map;

        Impl() {
            super();
        }

        Impl(final Iterator<Map.Entry> src) {
            super();
            while (src.hasNext()) {
                final Map.Entry e = src.next();
                put0(e.getKey(), e.getValue());
            }
        }

        private Impl(final Impl src) {
            super();
            this.size = src.size;
            this.hashCodes = this.hashCodes.clone();
            this.table = this.table.clone();
        }

        final void read(final ObjectInputStream in, int remaining) throws IOException, ClassNotFoundException {
            int initialCapacity = 1;
            while (initialCapacity <= remaining) initialCapacity <<= 1;
            beginWriteNonReentrant();
            try {
                this.hashCodes = new int[initialCapacity];
                this.table = new Object[initialCapacity << 1];
                while (--remaining >= 0) {
                    Object k = null;
                    try {
                        k = in.readObject();
                    } catch (final WriteAbortedException ex) {
                        continue;
                    } catch (final ObjectStreamException ex) {
                    }
                    final Object v;
                    try {
                        v = in.readObject();
                    } catch (final ObjectStreamException ex) {
                        continue;
                    }
                    if ((k != null) && (v != null)) {
                        if (k instanceof String) {
                            String s = (String) k;
                            if (s.length() <= 80) {
                                k = null;
                                s = s.intern();
                                put0(s, s.hashCode(), v, PUT_ID);
                            } else {
                                put0(s, s.hashCode(), v, PUT);
                            }
                        } else {
                            put0(k, v);
                        }
                    }
                }
                if (shouldShrink(this.size, this.hashCodes.length)) shrink(this.table, this.hashCodes, this.size);
            } finally {
                endWrite();
            }
        }

        final void write(final ObjectOutputStream out) throws IOException {
            int remaining;
            Object[] table = this.table;
            beginRead();
            try {
                remaining = this.size;
                if (remaining > 0) {
                    table = this.table;
                    if (table != null) table = table.clone();
                }
            } finally {
                endRead();
            }
            if (remaining > 0) {
                for (int ki = table.length >> 1, vi = table.length - 1; --ki >= 0; vi--) {
                    Object e = table[ki];
                    if ((e != null) && (e != REMOVED)) {
                        if (e instanceof Serializable) {
                            e = table[vi];
                            if ((e == null) || (e instanceof Serializable)) continue;
                        }
                        table[ki] = null;
                        table[vi] = null;
                        remaining--;
                    }
                }
            }
            out.writeInt(remaining);
            if (remaining > 0) {
                for (int ki = table.length >> 1, vi = table.length - 1; --ki >= 0; vi--) {
                    Object e = table[ki];
                    if ((e != null) && (e != REMOVED)) {
                        table[ki] = null;
                        try {
                            out.writeObject(e);
                        } catch (final ObjectStreamException ex) {
                            table[vi] = null;
                            if (--remaining > 0) continue; else break;
                        }
                        e = table[vi];
                        table[vi] = null;
                        try {
                            out.writeObject(e);
                        } catch (final ObjectStreamException ex) {
                        }
                        if (--remaining == 0) break;
                    }
                }
            }
        }

        final AttributeMap attributeMap() {
            AttributeMap map = this.map;
            if (map == null) {
                this.map = map = new AttributeMap(this);
                if (!Impl.atomicMap.compareAndSet(this, null, map)) map = this.map;
            }
            return map;
        }

        @Override
        protected final Impl clone() {
            beginRead();
            try {
                return (this.size == 0) ? null : new Impl(this);
            } finally {
                endRead();
            }
        }

        final boolean containsKeyId(final Object key, final int h) {
            beginRead();
            try {
                return (this.table != null) && (indexOfIdenticalKey(key, h, this.table) >= 0);
            } finally {
                endRead();
            }
        }

        final boolean containsKeyInsecure(final Object key, final Class c, final int h) {
            beginRead();
            try {
                return (this.table != null) && (indexOfKeyInsecure(key, c, h, this.table, this.hashCodes) >= 0);
            } finally {
                endRead();
            }
        }

        final boolean containsKeySecure(final Object key, final int h) {
            beginRead();
            try {
                return (this.table != null) && (indexOfKeySecure(key, h, this.table, this.hashCodes) >= 0);
            } finally {
                endRead();
            }
        }

        final Object getId(final Object key, final int h) {
            Object v = null;
            beginRead();
            final Object[] table = this.table;
            if (table != null) {
                final int i = indexOfIdenticalKey(key, h, table);
                if (i >= 0) v = table[table.length - i];
            }
            endRead();
            return v;
        }

        final Object getInsecure(final Object key, final Class c, final int h) {
            Object v = null;
            beginRead();
            try {
                final Object[] table = this.table;
                if (table != null) {
                    final int i = indexOfKeyInsecure(key, c, h, table, this.hashCodes);
                    if (i >= 0) v = table[table.length - i];
                }
            } finally {
                endRead();
            }
            return v;
        }

        final Object getSecure(final Object key, final int h) {
            Object v = null;
            beginRead();
            try {
                final Object[] table = this.table;
                if (table != null) {
                    final int i = indexOfKeySecure(key, h, table, this.hashCodes);
                    if (i >= 0) v = table[table.length - i];
                }
            } finally {
                endRead();
            }
            return v;
        }

        final void grow(final Object[] oldTable, final int[] oldHashCodes, final int size) {
            final int newLength = oldHashCodes.length << 1;
            if (newLength < size) throw new IllegalStateException("maximum capacity exhausted");
            rehash(oldTable, oldHashCodes, size, newLength);
        }

        final Object put(final Object key, final int h, Object value, final int mode) {
            beginWriteNonReentrant();
            try {
                return put0(key, h, value, mode);
            } finally {
                endWrite();
            }
        }

        final Object put0(final Object key, final Object value) {
            if (value == null) throw new NullPointerException("value");
            final Class c = key.getClass();
            final Object type = keyType(c);
            return (type == IDENTITY) ? put0(key, key.hashCode(), value, PUT_ID) : (type == SECURE) ? put0(key, key.hashCode(), value, PUT) : putInsecure0(key, c, key.hashCode(), value, false);
        }

        final Object put0(final Object key, final int h, Object value, int mode) {
            final int size = this.size;
            int[] hashCodes = this.hashCodes;
            Object[] table = this.table;
            if (table == null) {
                this.hashCodes = hashCodes = new int[INITIAL_TABLE_LENGTH];
                this.table = table = new Object[INITIAL_TABLE_LENGTH << 1];
            }
            int i;
            if (mode < 0) {
                i = indexOfIdenticalKey(key, h, table);
                mode = -mode;
            } else {
                i = indexOfKeySecure(key, h, table, hashCodes);
            }
            if (i >= 0) {
                i = table.length - i;
                final Object oldValue = table[i];
                if (mode == PUT) table[i] = value;
                value = oldValue;
            } else {
                i = ~i;
                table[i] = key;
                table[table.length - i] = value;
                value = null;
                hashCodes[i] = h;
                this.size = size + 1;
                if (shouldGrow(size + 1, hashCodes.length)) grow(table, hashCodes, size + 1);
            }
            return value;
        }

        final Object putInsecure(final Object key, final Class c, final int h, final Object value, final boolean onlyIfAbsent) {
            beginWriteNonReentrant();
            try {
                return putInsecure0(key, c, h, value, onlyIfAbsent);
            } finally {
                endWrite();
            }
        }

        final Object putInsecure0(final Object key, final Class c, final int h, Object value, final boolean onlyIfAbsent) {
            final int size = this.size;
            int[] hashCodes = this.hashCodes;
            Object[] table = this.table;
            if (table == null) {
                this.hashCodes = hashCodes = new int[INITIAL_TABLE_LENGTH];
                this.table = table = new Object[INITIAL_TABLE_LENGTH << 1];
            }
            int i = indexOfKeyInsecure(key, c, h, table, hashCodes);
            if (i >= 0) {
                i = table.length - i;
                final Object oldValue = table[i];
                if (!onlyIfAbsent) table[i] = value;
                value = oldValue;
            } else {
                i = ~i;
                table[i] = key;
                table[table.length - i] = value;
                value = null;
                hashCodes[i] = h;
                this.size = size + 1;
                if (shouldGrow(size + 1, hashCodes.length)) grow(table, hashCodes, size + 1);
            }
            return value;
        }

        final void rehash(final Object[] oldTable, final int[] oldHashCodes, final int size, final int newLength) {
            final int oldLength = oldHashCodes.length;
            final Object[] newTable = new Object[newLength << 1];
            final int[] newHashCodes = new int[newLength];
            int remaining = size;
            if (remaining > 0) {
                for (int oldIndex = oldLength - 1; oldIndex >= 0; oldIndex -= 2) {
                    final Object k = oldTable[oldIndex];
                    if ((k != null) && (k != REMOVED)) {
                        final int h = oldHashCodes[oldIndex];
                        int newIndex = cardinalIndex(h, newLength);
                        while (newTable[newIndex] != null) newIndex = Ints.rollUp(newIndex, newLength);
                        newTable[newIndex] = k;
                        newTable[newTable.length - newIndex] = oldTable[oldTable.length - oldIndex];
                        newHashCodes[newIndex] = h;
                        if (--remaining > 0) continue; else break;
                    }
                }
            }
            assert (remaining == 0) : remaining;
            this.hashCodes = newHashCodes;
            this.table = newTable;
        }

        final Object remove(final Object key, final int h, final boolean id) {
            beginWriteNonReentrant();
            try {
                final int size = this.size;
                if (size == 0) return null;
                final Object[] table = this.table;
                final int i = id ? indexOfIdenticalKey(key, h, table) : indexOfKeySecure(key, h, table, this.hashCodes);
                if (i >= 0) {
                    final Object oldValue = table[table.length - i];
                    this.size = size - 1;
                    removeSlot(i, table);
                    if (shouldShrink(size - 1, table.length)) shrink(table, this.hashCodes, size - 1);
                    return oldValue;
                } else {
                    return null;
                }
            } finally {
                endWrite();
            }
        }

        final Object removeInsecure(final Object key, final Class c, final int h) {
            beginWriteNonReentrant();
            try {
                final int size = this.size;
                if (size == 0) return null;
                final Object[] table = this.table;
                final int i = indexOfKeyInsecure(key, c, h, table, this.hashCodes);
                if (i >= 0) {
                    final Object oldValue = table[table.length - i];
                    this.size = size - 1;
                    removeSlot(i, table);
                    if (shouldShrink(size - 1, table.length)) shrink(table, this.hashCodes, size - 1);
                    return oldValue;
                } else {
                    return null;
                }
            } finally {
                endWrite();
            }
        }

        final Object replace(final Object key, final int h, Object value, final boolean id) {
            beginWrite();
            try {
                final int size = this.size;
                if (size == 0) {
                    value = null;
                } else {
                    final int[] hashCodes = this.hashCodes;
                    final Object[] table = this.table;
                    int i = id ? indexOfIdenticalKey(key, h, table) : indexOfKeySecure(key, h, table, hashCodes);
                    if (i >= 0) {
                        i = table.length - i;
                        final Object oldValue = table[i];
                        table[i] = value;
                        value = oldValue;
                    } else {
                        value = null;
                    }
                }
            } finally {
                endWrite();
            }
            return value;
        }

        final Object replaceInsecure(final Object key, final Class c, final int h, Object value) {
            beginWrite();
            try {
                final int size = this.size;
                if (size == 0) {
                    value = null;
                } else {
                    final int[] hashCodes = this.hashCodes;
                    final Object[] table = this.table;
                    int i = indexOfKeyInsecure(key, c, h, table, hashCodes);
                    if (i >= 0) {
                        i = table.length - i;
                        final Object oldValue = table[i];
                        table[i] = value;
                        value = oldValue;
                    } else {
                        value = null;
                    }
                }
            } finally {
                endWrite();
            }
            return value;
        }

        final void shrink(final Object[] oldTable, final int[] oldHashCodes, final int size) {
            if (size == 0) {
                this.hashCodes = null;
                this.table = null;
            } else {
                rehash(oldTable, oldHashCodes, size, oldHashCodes.length - (oldHashCodes.length >> 2));
            }
        }
    }
}
