package xbird.util.collections.ints;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import xbird.util.math.Primes;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 * @link http://www.eece.unm.edu/faculty/heileman/hash/hash.html
 */
public class Int2LongOpenHash implements Externalizable {

    private static final long serialVersionUID = -8162355845665353513L;

    protected static final byte FREE = 0;

    protected static final byte FULL = 1;

    protected static final byte REMOVED = 2;

    private static final float DEFAULT_LOAD_FACTOR = 0.7f;

    private static final float DEFAULT_GROW_FACTOR = 2.0f;

    protected final transient float _loadFactor;

    protected final transient float _growFactor;

    protected int _used = 0;

    protected int _threshold;

    protected int[] _keys;

    protected long[] _values;

    protected byte[] _states;

    protected Int2LongOpenHash(int size, float loadFactor, float growFactor, boolean forcePrime) {
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        this._loadFactor = loadFactor;
        this._growFactor = growFactor;
        int actualSize = forcePrime ? Primes.findLeastPrimeNumber(size) : size;
        this._keys = new int[actualSize];
        this._values = new long[actualSize];
        this._states = new byte[actualSize];
        this._threshold = (int) (actualSize * _loadFactor);
    }

    public Int2LongOpenHash(int size, float loadFactor, float growFactor) {
        this(size, loadFactor, growFactor, true);
    }

    public Int2LongOpenHash(int size) {
        this(size, DEFAULT_LOAD_FACTOR, DEFAULT_GROW_FACTOR, true);
    }

    public Int2LongOpenHash() {
        this._loadFactor = DEFAULT_LOAD_FACTOR;
        this._growFactor = DEFAULT_GROW_FACTOR;
    }

    public boolean containsKey(int key) {
        return findKey(key) >= 0;
    }

    /**
     * @return -1L if not found
     */
    public long get(int key) {
        int i = findKey(key);
        if (i < 0) {
            return -1L;
        }
        recordAccess(i);
        return _values[i];
    }

    public long put(int key, long value) {
        int hash = keyHash(key);
        int keyLength = _keys.length;
        int keyIdx = hash % keyLength;
        boolean expanded = preAddEntry(keyIdx);
        if (expanded) {
            keyLength = _keys.length;
            keyIdx = hash % keyLength;
        }
        int[] keys = _keys;
        long[] values = _values;
        byte[] states = _states;
        if (states[keyIdx] == FULL) {
            if (keys[keyIdx] == key) {
                long old = values[keyIdx];
                values[keyIdx] = value;
                recordAccess(keyIdx);
                return old;
            }
            int decr = 1 + (hash % (keyLength - 2));
            for (; ; ) {
                keyIdx -= decr;
                if (keyIdx < 0) {
                    keyIdx += keyLength;
                }
                if (isFree(keyIdx, key)) {
                    break;
                }
                if (states[keyIdx] == FULL && keys[keyIdx] == key) {
                    long old = values[keyIdx];
                    values[keyIdx] = value;
                    recordAccess(keyIdx);
                    return old;
                }
            }
        }
        keys[keyIdx] = key;
        values[keyIdx] = value;
        states[keyIdx] = FULL;
        postAddEntry(keyIdx);
        return -1L;
    }

    /** Return weather the required slot is free for new entry */
    protected boolean isFree(int index, int key) {
        byte stat = _states[index];
        if (stat == FREE) {
            return true;
        }
        if (stat == REMOVED && _keys[index] == key) {
            return true;
        }
        return false;
    }

    /** @return expanded or not */
    protected boolean preAddEntry(int index) {
        if (++_used >= _threshold) {
            int newCapacity = Math.round(_keys.length * _growFactor);
            ensureCapacity(newCapacity);
            return true;
        }
        return false;
    }

    protected void postAddEntry(int index) {
    }

    protected int findKey(int key) {
        int[] keys = _keys;
        byte[] states = _states;
        int keyLength = keys.length;
        int hash = keyHash(key);
        int keyIdx = hash % keyLength;
        if (states[keyIdx] != FREE) {
            if (states[keyIdx] == FULL && keys[keyIdx] == key) {
                return keyIdx;
            }
            int decr = 1 + (hash % (keyLength - 2));
            for (; ; ) {
                keyIdx -= decr;
                if (keyIdx < 0) {
                    keyIdx += keyLength;
                }
                if (isFree(keyIdx, key)) {
                    return -1;
                }
                if (states[keyIdx] == FULL && keys[keyIdx] == key) {
                    return keyIdx;
                }
            }
        }
        return -1;
    }

    public long remove(int key) {
        int[] keys = _keys;
        long[] values = _values;
        byte[] states = _states;
        int keyLength = keys.length;
        int hash = keyHash(key);
        int keyIdx = hash % keyLength;
        if (states[keyIdx] != FREE) {
            if (states[keyIdx] == FULL && keys[keyIdx] == key) {
                long old = values[keyIdx];
                states[keyIdx] = REMOVED;
                --_used;
                recordRemoval(keyIdx);
                return old;
            }
            int decr = 1 + (hash % (keyLength - 2));
            for (; ; ) {
                keyIdx -= decr;
                if (keyIdx < 0) {
                    keyIdx += keyLength;
                }
                if (states[keyIdx] == FREE) {
                    return -1L;
                }
                if (states[keyIdx] == FULL && keys[keyIdx] == key) {
                    long old = values[keyIdx];
                    states[keyIdx] = REMOVED;
                    --_used;
                    recordRemoval(keyIdx);
                    return old;
                }
            }
        }
        return -1L;
    }

    public int size() {
        return _used;
    }

    public void clear() {
        Arrays.fill(_states, FREE);
        this._used = 0;
    }

    public IMapIterator entries() {
        return new MapIterator();
    }

    @Override
    public String toString() {
        int len = size() * 10 + 2;
        StringBuilder buf = new StringBuilder(len);
        buf.append('{');
        IMapIterator i = entries();
        while (i.next() != -1) {
            buf.append(i.getKey());
            buf.append('=');
            buf.append(i.getValue());
            if (i.hasNext()) {
                buf.append(',');
            }
        }
        buf.append('}');
        return buf.toString();
    }

    protected void ensureCapacity(int newCapacity) {
        if (_used < _threshold) {
            throw new IllegalStateException();
        }
        int prime = Primes.findLeastPrimeNumber(newCapacity);
        rehash(prime);
        this._threshold = Math.round(prime * _loadFactor);
    }

    private void rehash(int newCapacity) {
        int oldCapacity = _keys.length;
        if (newCapacity <= oldCapacity) {
            throw new IllegalArgumentException("new: " + newCapacity + ", old: " + oldCapacity);
        }
        int[] newkeys = new int[newCapacity];
        long[] newValues = new long[newCapacity];
        byte[] newStates = new byte[newCapacity];
        this._used = 0;
        for (int i = 0; i < oldCapacity; i++) {
            if (_states[i] == FULL) {
                _used++;
                int k = _keys[i];
                long v = _values[i];
                int hash = keyHash(k);
                int keyIdx = hash % newCapacity;
                if (newStates[keyIdx] == FULL) {
                    int decr = 1 + (hash % (newCapacity - 2));
                    while (newStates[keyIdx] != FREE) {
                        keyIdx -= decr;
                        if (keyIdx < 0) {
                            keyIdx += newCapacity;
                        }
                    }
                }
                newkeys[keyIdx] = k;
                newValues[keyIdx] = v;
                newStates[keyIdx] = FULL;
            }
        }
        this._keys = newkeys;
        this._values = newValues;
        this._states = newStates;
    }

    private static int keyHash(int key) {
        return key & 0x7fffffff;
    }

    protected void recordAccess(int idx) {
    }

    ;

    protected void recordRemoval(int idx) {
    }

    ;

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(_threshold);
        out.writeInt(_used);
        out.writeInt(_keys.length);
        IMapIterator i = entries();
        while (i.next() != -1) {
            out.writeInt(i.getKey());
            out.writeLong(i.getValue());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this._threshold = in.readInt();
        this._used = in.readInt();
        int keylen = in.readInt();
        int[] keys = new int[keylen];
        long[] values = new long[keylen];
        byte[] states = new byte[keylen];
        for (int i = 0; i < _used; i++) {
            int k = in.readInt();
            long v = in.readLong();
            int hash = keyHash(k);
            int keyIdx = hash % keylen;
            if (states[keyIdx] != FREE) {
                int decr = 1 + (hash % (keylen - 2));
                for (; ; ) {
                    keyIdx -= decr;
                    if (i < 0) {
                        keyIdx += keylen;
                    }
                    if (states[keyIdx] == FREE) {
                        break;
                    }
                }
            }
            states[keyIdx] = FULL;
            keys[keyIdx] = k;
            values[keyIdx] = v;
        }
        this._keys = keys;
        this._values = values;
        this._states = states;
    }

    public interface IMapIterator {

        public boolean hasNext();

        /**
         * @return -1 if not found
         */
        public int next();

        public int getKey();

        public long getValue();
    }

    private final class MapIterator implements IMapIterator {

        int nextEntry;

        int lastEntry = -1;

        MapIterator() {
            this.nextEntry = nextEntry(0);
        }

        /** find the index of next full entry */
        int nextEntry(int index) {
            while (index < _keys.length && _states[index] != FULL) {
                index++;
            }
            return index;
        }

        public boolean hasNext() {
            return nextEntry < _keys.length;
        }

        public int next() {
            if (!hasNext()) {
                return -1;
            }
            int curEntry = nextEntry;
            this.lastEntry = curEntry;
            this.nextEntry = nextEntry(curEntry + 1);
            return curEntry;
        }

        public int getKey() {
            if (lastEntry == -1) {
                throw new IllegalStateException();
            }
            return _keys[lastEntry];
        }

        public long getValue() {
            if (lastEntry == -1) {
                throw new IllegalStateException();
            }
            return _values[lastEntry];
        }
    }

    public static final class Int2LongOpenLRUMap extends Int2LongOpenHash {

        private final int maxCapacity;

        private final ChainEntry evictChainHeader;

        private final IntHash<ChainEntry> entries;

        private boolean reachedLimit = false;

        /**
         * allocates twice more open rooms for hash than limit, and 
         * the hash table is fixed.
         */
        public Int2LongOpenLRUMap(int limit) {
            super(limit * 2, 1.0f, limit, true);
            this.maxCapacity = limit;
            this.entries = new IntHash<ChainEntry>(limit);
            this.evictChainHeader = initEntryChain();
        }

        /**
         * growFactor is limit / init.
         * This semantics means expansion occourrs at most once
         * when more than 75% of rooms are filled.
         */
        public Int2LongOpenLRUMap(int init, int limit) {
            super(init, 0.75f, limit / init, true);
            if (limit < init) {
                throw new IllegalArgumentException("init '" + init + "'must be less than limit '" + limit + '\'');
            }
            this.maxCapacity = limit;
            this.entries = new IntHash<ChainEntry>(limit);
            this.evictChainHeader = initEntryChain();
        }

        private ChainEntry initEntryChain() {
            ChainEntry header = new ChainEntry(-1);
            header.prev = header.next = header;
            return header;
        }

        @Override
        protected boolean preAddEntry(int index) {
            ++_used;
            if (removeEldestEntry()) {
                this.reachedLimit = true;
                ChainEntry eldest = evictChainHeader.next;
                if (eldest == null) {
                    throw new IllegalStateException();
                }
                long removed = directRemove(eldest.entryIndex);
                if (removed == -1L) {
                    throw new IllegalStateException();
                }
            } else {
                if (_used > _threshold) {
                    throw new IllegalStateException("expansion is required for elements limited map");
                }
            }
            return false;
        }

        private long directRemove(int index) {
            long old = _values[index];
            _states[index] = REMOVED;
            --_used;
            recordRemoval(index);
            return old;
        }

        @Override
        protected void postAddEntry(int idx) {
            ChainEntry newEntry = new ChainEntry(idx);
            ChainEntry old = entries.put(idx, newEntry);
            if (old != null) {
                throw new IllegalStateException();
            }
            newEntry.addBefore(evictChainHeader);
        }

        /**
         * Move the entry to chain head.
         */
        @Override
        protected void recordAccess(int idx) {
            ChainEntry entry = entries.get(idx);
            if (entry == null) {
                throw new IllegalStateException();
            }
            entry.remove();
            entry.addBefore(evictChainHeader);
        }

        /**
         * Remove the entry from the chain.
         */
        @Override
        protected void recordRemoval(int idx) {
            ChainEntry entry = entries.remove(idx);
            if (entry == null) {
                throw new IllegalStateException();
            }
            entry.remove();
        }

        private boolean removeEldestEntry() {
            return _used > maxCapacity;
        }

        @Override
        protected boolean isFree(int index, int key) {
            byte stat = _states[index];
            return (stat == FREE) || (stat == REMOVED);
        }

        /** Do not trust me, consider this as rough estimated size. */
        @Override
        public int size() {
            if (reachedLimit) {
                return maxCapacity;
            } else {
                return _used;
            }
        }

        @Override
        public IMapIterator entries() {
            return new OrderedMapIterator(evictChainHeader);
        }

        static final class ChainEntry {

            final int entryIndex;

            ChainEntry prev = null, next = null;

            public ChainEntry(int index) {
                this.entryIndex = index;
            }

            /**
             * Removes this entry from the linked list.
             */
            private void remove() {
                prev.next = next;
                next.prev = prev;
                prev = null;
                next = null;
            }

            /**
             * Inserts this entry before the specified existing entry in the list.
             */
            private void addBefore(ChainEntry existingEntry) {
                next = existingEntry;
                prev = existingEntry.prev;
                prev.next = this;
                next.prev = this;
            }
        }

        final class OrderedMapIterator implements IMapIterator {

            private ChainEntry entry;

            OrderedMapIterator(ChainEntry e) {
                assert (e != null);
                this.entry = e;
            }

            public int getKey() {
                return _keys[entry.entryIndex];
            }

            public long getValue() {
                return _values[entry.entryIndex];
            }

            public boolean hasNext() {
                return entry.next != evictChainHeader;
            }

            public int next() {
                ChainEntry e = entry.next;
                if (e == evictChainHeader) {
                    return -1;
                }
                this.entry = e;
                return e.entryIndex;
            }
        }
    }
}
