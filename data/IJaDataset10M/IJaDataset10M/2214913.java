package net.jadoth.collections;

import static java.lang.System.identityHashCode;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.util.KeyValue;

/**
 * Straight forward minimal implementation of a strongly referencing identity hashing map.
 * <p>
 * This implementation is preferable to full scale implementations like {@link HashEnum} or {@link EqHashEnum} in cases
 * where only basic mapping functionality but best performance and low memory need is required, for example to associate
 * handler instances to class instances.
 *
 * @author Thomas M�nz
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public final class MiniMap<K, V> {

    private static final int DEFAULT_SLOT_LENGTH = 8;

    private static int calculateCapacity(final int minimalCapacity) {
        if (minimalCapacity > 1 << 30) {
            return 1 << 30;
        }
        int capacity = DEFAULT_SLOT_LENGTH;
        while (capacity < minimalCapacity) {
            capacity <<= 1;
        }
        return capacity;
    }

    @SafeVarargs
    public static final <K, V> MiniMap<K, V> miniMap(final KeyValue<? extends K, ? extends V>... data) {
        return new MiniMap<K, V>(data.length, data);
    }

    private Entry<K, V>[] slots;

    private int modulo;

    private int size;

    @SuppressWarnings("unchecked")
    public MiniMap() {
        super();
        this.size = 0;
        this.slots = new Entry[DEFAULT_SLOT_LENGTH];
        this.modulo = DEFAULT_SLOT_LENGTH - 1;
    }

    @SuppressWarnings("unchecked")
    public MiniMap(final int initialCapacity) {
        super();
        this.size = 0;
        this.modulo = (this.slots = new Entry[calculateCapacity(initialCapacity)]).length - 1;
    }

    @SuppressWarnings("unchecked")
    public MiniMap(final int initialCapacity, final KeyValue<? extends K, ? extends V>... data) {
        super();
        final Entry<K, V>[] slots;
        final int modulo;
        this.size = 0;
        this.modulo = modulo = (this.slots = slots = new Entry[calculateCapacity(initialCapacity)]).length - 1;
        for (int i = 0; i < data.length; i++) {
            final KeyValue<? extends K, ? extends V> entry;
            if ((entry = data[i]) == null) continue;
            final K key;
            if ((key = entry.key()) == null) continue;
            slots[identityHashCode(key) & modulo] = new Entry<K, V>(key, entry.value(), slots[identityHashCode(key) & modulo]);
        }
    }

    @SuppressWarnings("unchecked")
    MiniMap(final int size, final ConstMiniMap.Entry<K, V>[] source) {
        super();
        final Entry<K, V>[] slots;
        final int modulo;
        this.modulo = modulo = (this.slots = slots = new Entry[calculateCapacity(this.size = size)]).length - 1;
        for (int i = 0; i < source.length; i++) {
            for (ConstMiniMap.Entry<K, V> entry = source[i]; entry != null; entry = entry.link) {
                slots[identityHashCode(entry.key) & modulo] = new Entry<K, V>(entry.key, entry.value, slots[identityHashCode(entry.key) & modulo]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    MiniMap(final int size, final Entry<K, V>[] source) {
        super();
        final Entry<K, V>[] slots;
        final int modulo;
        this.modulo = modulo = (this.slots = slots = new Entry[calculateCapacity(this.size = size)]).length - 1;
        for (int i = 0; i < source.length; i++) {
            for (Entry<K, V> entry = source[i]; entry != null; entry = entry.link) {
                slots[identityHashCode(entry.key) & modulo] = new Entry<K, V>(entry.key, entry.value, slots[identityHashCode(entry.key) & modulo]);
            }
        }
    }

    public int size() {
        return this.size;
    }

    public MiniMap<K, V> copy() {
        return new MiniMap<K, V>(this.size, this.slots);
    }

    public ConstMiniMap<K, V> toConstMap() {
        return new ConstMiniMap<K, V>(this.size, this.slots);
    }

    public V get(final K key) {
        for (Entry<K, V> e = this.slots[identityHashCode(key) & this.modulo]; e != null; e = e.link) {
            if (e.key == key) {
                return e.value;
            }
        }
        if (key == null) throw new NullPointerException();
        return null;
    }

    public boolean containsKey(final K key) {
        for (Entry<K, V> e = this.slots[identityHashCode(key) & this.modulo]; e != null; e = e.link) {
            if (e.key == key) {
                return true;
            }
        }
        return false;
    }

    private void increaseStorage() {
        if (this.slots.length == 1 << 30) return;
        this.rebuildStorage(this.slots.length << 1);
    }

    @SuppressWarnings("unchecked")
    private void rebuildStorage(final int newSlotLength) {
        final Entry<K, V>[] slots = this.slots, newSlots = new Entry[newSlotLength];
        final int newModulo = newSlotLength - 1;
        for (int i = 0; i < slots.length; i++) {
            for (Entry<K, V> e = slots[i], link; e != null; e = link) {
                link = e.link;
                e.link = newSlots[System.identityHashCode(e.key) & newModulo];
                newSlots[System.identityHashCode(e.key) & newModulo] = e;
            }
        }
        this.slots = newSlots;
        this.modulo = newModulo;
    }

    public V put(final K key, final V value) {
        for (Entry<K, V> e = this.slots[identityHashCode(key) & this.modulo]; e != null; e = e.link) {
            if (e.key == key) {
                final V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        if (key == null) throw new NullPointerException();
        this.slots[identityHashCode(key) & this.modulo] = new Entry<K, V>(key, value, this.slots[identityHashCode(key) & this.modulo]);
        if (this.size++ >= this.modulo) {
            this.increaseStorage();
        }
        return null;
    }

    public MiniMap<K, V> putAll(@SuppressWarnings("unchecked") final KeyValue<K, V>... data) {
        Entry<K, V>[] slots = this.slots;
        int modulo = this.modulo;
        for (int i = 0; i < data.length; i++) {
            final K key;
            if ((key = data[i].key()) == null) {
                continue;
            }
            slots[identityHashCode(key) & modulo] = new Entry<K, V>(key, data[i].value(), slots[identityHashCode(key) & modulo]);
            if (this.size++ >= modulo) {
                this.increaseStorage();
                slots = this.slots;
                modulo = this.modulo;
            }
        }
        return this;
    }

    public V remove(final K key) {
        if (key == null) throw new NullPointerException();
        Entry<K, V> entry;
        if ((entry = this.slots[identityHashCode(key) & this.modulo]).key == key) {
            this.slots[identityHashCode(key) & this.modulo] = entry.link;
            this.size--;
            return entry.value;
        }
        Entry<K, V> last;
        while ((entry = (last = entry).link) != null) {
            if (entry.key == key) {
                last.link = entry.link;
                this.size--;
                return entry.value;
            }
        }
        return null;
    }

    public void optimize() {
        if (calculateCapacity(this.size) < this.modulo) {
            this.rebuildStorage(calculateCapacity(this.size));
        }
    }

    public void clear() {
        final Entry<K, V>[] slots = this.slots;
        for (int i = 0; i < slots.length; i++) {
            for (Entry<K, V> e = slots[i], link; e != null; e = link) {
                link = e.link;
                e.key = null;
                e.value = null;
                e.link = null;
            }
            slots[i] = null;
        }
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public KeyValue<K, V>[] toArray() {
        final Entry<K, V>[] slots = this.slots;
        final int slotsLength = slots.length;
        final KeyValue<K, V>[] array = new KeyValue[this.size];
        int a = 0;
        for (int i = 0; i < slotsLength; i++) {
            for (Entry<K, V> e = slots[i]; e != null; e = e.link) {
                array[a++] = new KeyValue.Implementation<K, V>(e.key, e.value);
            }
        }
        return array;
    }

    public int iterateValues(final Procedure<? super V> procedure) {
        for (Entry<K, V> entry : this.slots) {
            for (; entry != null; entry = entry.link) {
                procedure.apply(entry.value);
            }
        }
        return this.size;
    }

    static final class Entry<K, V> {

        K key;

        V value;

        Entry<K, V> link;

        Entry(final K key, final V value, final Entry<K, V> link) {
            super();
            this.key = key;
            this.value = value;
            this.link = link;
        }
    }
}
