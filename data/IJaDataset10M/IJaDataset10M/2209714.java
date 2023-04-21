package pocu.data_structs;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Linked hash set. Duplicates are not saved and nulls are not allowed.
 * A doubly-linked list is maintained which makes iteration order consistent and
 * effiecient.
 */
public class LHSet extends LHashCore implements Set {

    public LHSet() {
        super();
    }

    public LHSet(int size) {
        super(size);
    }

    public LHSet(HashTypeEnum type) {
        super(type);
    }

    public LHSet(int size, HashTypeEnum type) {
        super(size, type);
    }

    public static LHSet getSynchronized() {
        return new LHSetSynchronized();
    }

    public static LHSet getSynchronized(int size) {
        return new LHSetSynchronized(size);
    }

    public static LHSet getSynchronized(HashTypeEnum type) {
        return new LHSetSynchronized(type);
    }

    public static LHSet getSynchronized(int size, HashTypeEnum type) {
        return new LHSetSynchronized(size, type);
    }

    public void add(Object key) {
        doPut(key);
    }

    public void addAll(Iterable i) {
        SetCommon.addAll(this, i);
    }

    public void addAll(Object[] arr) {
        SetCommon.addAll(this, arr);
    }

    public void remove(Object key) {
        doRemove(key);
    }

    public void removeAll(Iterable i) {
        SetCommon.removeAll(this, i);
    }

    public void removeAll(Object[] arr) {
        SetCommon.removeAll(this, arr);
    }

    public LHSet copy() {
        return (LHSet) getCopy(new LHSet(0));
    }

    public boolean equals(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.equals(this, set);
    }

    public boolean isSubset(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.isSubset(this, set);
    }

    public boolean isProperSubset(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.isProperSubset(this, set);
    }

    public Set union(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.union(this, set, getNewSet());
    }

    public Set intersection(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.intersection(this, set, getNewSet());
    }

    public Set complement(Set set) {
        if (set == null) throw new IllegalArgumentException("Argument 'set' cannot be null");
        return SetCommon.complement(this, set, getNewSet());
    }

    public String toString() {
        return "LHSet: size=" + size;
    }

    public String dump() {
        return Common.dump(this);
    }

    protected Bin getNewBin(Object key) {
        return new LBin(key);
    }

    protected Bin getCopyOfBin(Bin bin) {
        return new LBin(bin.key);
    }

    private Set getNewSet() {
        return new LHSet(new HashTypeEnum(loadFactor));
    }
}

class LHSetSynchronized extends LHSet {

    public LHSetSynchronized() {
        super();
    }

    public LHSetSynchronized(int size) {
        super(size);
    }

    public LHSetSynchronized(HashTypeEnum type) {
        super(type);
    }

    public LHSetSynchronized(int size, HashTypeEnum type) {
        super(size, type);
    }

    public void add(Object val) {
        synchronized (this) {
            super.add(val);
        }
    }

    public void addAll(Iterable i) {
        synchronized (this) {
            super.addAll(i);
        }
    }

    public void addAll(Object[] arr) {
        synchronized (this) {
            super.addAll(arr);
        }
    }

    public boolean contains(Object val) {
        synchronized (this) {
            return super.contains(val);
        }
    }

    public void remove(Object val) {
        synchronized (this) {
            super.remove(val);
        }
    }

    public void removeAll(Iterable i) {
        synchronized (this) {
            super.removeAll(i);
        }
    }

    public void removeAll(Object[] arr) {
        synchronized (this) {
            super.removeAll(arr);
        }
    }

    public LHSet copy() {
        synchronized (this) {
            return super.copy();
        }
    }

    public boolean isSubset(Set set) {
        synchronized (this) {
            return super.isSubset(set);
        }
    }

    public boolean isProperSubset(Set set) {
        synchronized (this) {
            return super.isProperSubset(set);
        }
    }

    public Set union(Set set) {
        synchronized (this) {
            return super.union(set);
        }
    }

    public Set intersection(Set set) {
        synchronized (this) {
            return super.intersection(set);
        }
    }

    public Set complement(Set set) {
        synchronized (this) {
            return super.complement(set);
        }
    }

    public void sort() {
        synchronized (this) {
            super.sort();
        }
    }

    public void sort(Comparator comparator) {
        synchronized (this) {
            super.sort(comparator);
        }
    }

    public void reverse() {
        synchronized (this) {
            super.reverse();
        }
    }

    public void clear() {
        synchronized (this) {
            super.clear();
        }
    }

    public int size() {
        synchronized (this) {
            return super.size();
        }
    }

    public SList keyList() {
        synchronized (this) {
            return super.keyList();
        }
    }

    public Iter iter() {
        return new SynchronizedIter(super.iter());
    }

    private class SynchronizedIter implements Iter {

        private Iter iter;

        public SynchronizedIter(Iter iter) {
            assert iter != null;
            this.iter = iter;
        }

        public boolean hasNext() {
            synchronized (LHSetSynchronized.this) {
                return iter.hasNext();
            }
        }

        public Object next() {
            synchronized (LHSetSynchronized.this) {
                return iter.next();
            }
        }

        public void set(Object val) {
            synchronized (LHSetSynchronized.this) {
                iter.set(val);
            }
        }

        public void remove() {
            synchronized (LHSetSynchronized.this) {
                iter.remove();
            }
        }

        public void insertBefore(Object val) {
            synchronized (LHSetSynchronized.this) {
                iter.insertBefore(val);
            }
        }

        public void insertAfter(Object val) {
            synchronized (LHSetSynchronized.this) {
                iter.insertAfter(val);
            }
        }
    }
}
