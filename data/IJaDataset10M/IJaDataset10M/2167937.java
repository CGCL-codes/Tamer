package net.jadoth.objectregistry;

import static java.lang.System.identityHashCode;
import static net.jadoth.util.chars.VarChar.MediumVarChar;
import net.jadoth.collections.BulkList;
import net.jadoth.collections.interfaces.OptimizableCollection;
import net.jadoth.collections.interfaces.Sized;
import net.jadoth.collections.types.XList;
import net.jadoth.lang.functional.Procedure;
import net.jadoth.lang.functional._longProcedure;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarChar;

/**
 * Primitive (read: fast) synchronized pseudo map implementation that maps long id values to weakly referenced objects.
 *
 * @author Thomas M�nz
 *
 */
public final class HashMap_longObject<T> implements Sized, OptimizableCollection {

    private static final int DEFAULT_SLOT_SIZE = 16;

    private static final int MAXIMUM_SLOT_SIZE = 1 << 30;

    private static int calcCapacity(final int n) {
        if (n >= MAXIMUM_SLOT_SIZE) {
            return MAXIMUM_SLOT_SIZE;
        }
        int slotCount = DEFAULT_SLOT_SIZE;
        while (slotCount < n) {
            slotCount <<= 1;
        }
        return slotCount;
    }

    private Entry<T>[] slots;

    private int modulo;

    private int size = 0;

    @SuppressWarnings("unchecked")
    public HashMap_longObject() {
        super();
        this.slots = new Entry[DEFAULT_SLOT_SIZE];
        this.modulo = DEFAULT_SLOT_SIZE - 1;
    }

    @SuppressWarnings("unchecked")
    public HashMap_longObject(int slotSize) {
        super();
        this.slots = new Entry[slotSize = calcCapacity(slotSize)];
        this.modulo = slotSize - 1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @SuppressWarnings("unchecked")
    private void rebuild(final int newCapacity) {
        final Entry<T>[] newSlots = new Entry[newCapacity];
        final int newSlotCountMinusOne = newCapacity - 1;
        int index;
        for (Entry<T> entry : this.slots) {
            for (Entry<T> next; entry != null; entry = next) {
                next = entry.next;
                entry.next = newSlots[index = identityHashCode(entry.ref) & newSlotCountMinusOne];
                newSlots[index] = entry;
            }
        }
        this.slots = newSlots;
        this.modulo = newSlotCountMinusOne;
    }

    @SuppressWarnings("unchecked")
    private void rebuild() {
        final int newModulo;
        final Entry<T>[] newSlots = new Entry[(newModulo = (this.modulo + 1 << 1) - 1) + 1];
        for (Entry<T> entry : this.slots) {
            for (Entry<T> next; entry != null; entry = next) {
                next = entry.next;
                entry.next = newSlots[(int) (entry.id & newModulo)];
                newSlots[(int) (entry.id & newModulo)] = entry;
            }
        }
        this.slots = newSlots;
        this.modulo = newModulo;
    }

    private void putEntry(final int index, final Entry<T> entry) {
        this.slots[index] = entry;
        if (++this.size >= this.modulo) {
            this.rebuild();
        }
    }

    public boolean add(final long id, final T object) {
        final int index;
        Entry<T> entry;
        if ((entry = this.slots[index = (int) (id & this.slots.length - 1)]) == null) {
            this.putEntry(index, new Entry<T>(id, object));
            return true;
        }
        do {
            if (entry.id == id) {
                return false;
            }
        } while ((entry = entry.next) != null);
        this.putEntry(index, new Entry<T>(id, object, this.slots[index]));
        return true;
    }

    public boolean put(final long id, final T object) {
        final int index;
        Entry<T> entry;
        if ((entry = this.slots[index = (int) (id & this.slots.length - 1)]) == null) {
            this.putEntry(index, new Entry<T>(id, object));
            return true;
        }
        do {
            if (entry.id == id) {
                entry.ref = object;
                return false;
            }
        } while ((entry = entry.next) != null);
        this.putEntry(index, new Entry<T>(id, object, this.slots[index]));
        return true;
    }

    public T putGet(final long id, final T object) {
        final int index;
        final Entry<T> head = this.slots[index = (int) (id & this.modulo)];
        if (head == null) {
            this.slots[index] = new Entry<T>(id, object);
            if (this.size++ >= this.modulo) {
                this.rebuild(this.modulo + 1 << 1);
            }
            return null;
        }
        if (head.id == id) {
            final T oldRef = head.ref;
            head.ref = object;
            return oldRef;
        }
        for (Entry<T> entry = head.next; entry != null; entry = entry.next) {
            if (entry.id == id) {
                final T oldRef = entry.ref;
                entry.ref = object;
                return oldRef;
            }
        }
        this.slots[index] = new Entry<T>(id, object, head);
        if (this.size++ >= this.modulo) {
            this.rebuild(this.modulo + 1 << 1);
        }
        return null;
    }

    public T get(final long id) {
        for (Entry<T> entry = this.slots[(int) (id & this.modulo)]; entry != null; entry = entry.next) {
            if (entry.id == id) {
                return entry.ref;
            }
        }
        return null;
    }

    public XList<T> getObjects() {
        final BulkList<T> list = new BulkList<T>(this.size);
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                list.add(entry.ref);
            }
        }
        return list;
    }

    public XList<Long> getIds() {
        final BulkList<Long> list = new BulkList<Long>(this.size);
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                list.add(entry.id);
            }
        }
        return list;
    }

    public int iterateObjects(final Procedure<? super T> procedure) {
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                procedure.apply(entry.ref);
            }
        }
        return this.size;
    }

    @Override
    public String toString() {
        final VarChar vc = MediumVarChar().append('{');
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                vc.append(entry.id).append(" -> ").append(entry.ref).append(", ");
            }
        }
        return vc.deleteLastChar().setLastChar('}').toString();
    }

    /**
	 * Optimizes the internal storage and returns the remaining amount of entries.
	 * @return the amount of entries after the optimization is been completed.
	 */
    @Override
    public int optimize() {
        final int newCapacity;
        if ((newCapacity = calcCapacity(this.size)) != this.slots.length) {
            this.rebuild(newCapacity);
        }
        return this.size;
    }

    public int iterateIds(final Procedure<? super Long> procedure) {
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                procedure.apply(entry.id);
            }
        }
        return this.size;
    }

    public int iterateIds(final _longProcedure procedure) {
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                procedure.apply(entry.id);
            }
        }
        return this.size;
    }

    public int iterate(final Procedure<? super KeyValue<Long, T>> procedure) {
        for (Entry<T> entry : this.slots) {
            for (; entry != null; entry = entry.next) {
                procedure.apply(entry);
            }
        }
        return this.size;
    }

    public void clear() {
        final Entry<T>[] slots = this.slots;
        for (int i = 0, len = slots.length; i < len; i++) {
            slots[i] = null;
        }
        this.size = 0;
    }

    static final class Entry<T> implements KeyValue<Long, T> {

        final long id;

        T ref;

        Entry<T> next;

        Entry(final long id, final T object, final Entry<T> next) {
            super();
            this.id = id;
            this.ref = object;
            this.next = next;
        }

        Entry(final long id, final T object) {
            super();
            this.id = id;
            this.ref = object;
            this.next = null;
        }

        @Override
        public Long key() {
            return this.id;
        }

        @Override
        public T value() {
            return this.ref;
        }

        @Override
        public String toString() {
            return '[' + String.valueOf(this.id) + " -> " + String.valueOf(this.ref) + ']';
        }
    }
}
