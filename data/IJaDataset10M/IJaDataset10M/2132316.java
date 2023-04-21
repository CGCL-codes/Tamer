package cspfj.constraint;

import java.util.Arrays;
import java.util.Iterator;

public class TupleListDynamic implements Matrix, Cloneable, Iterable<int[]> {

    private int first;

    private int[][] tuples;

    private int[] next;

    private int[] removed;

    private int[] removedLast;

    private int size;

    public TupleListDynamic(final int arity, final int nbTuples) {
        size = 0;
        first = -1;
        tuples = new int[nbTuples][arity];
        next = new int[nbTuples];
        Arrays.fill(next, -1);
        removed = new int[arity];
        Arrays.fill(removed, -1);
        removedLast = removed.clone();
    }

    private void expandRemoved(final int newLength) {
        final int[] newRemoved = new int[newLength];
        Arrays.fill(newRemoved, -1);
        System.arraycopy(removed, 0, newRemoved, 0, removed.length);
        final int[] newRemovedLast = newRemoved.clone();
        System.arraycopy(removedLast, 0, newRemovedLast, 0, removedLast.length);
        removed = newRemoved;
        removedLast = newRemovedLast;
    }

    @Override
    public boolean check(final int[] tuple) {
        if (contains(tuple)) {
            return true;
        }
        return false;
    }

    public void restore(final int level) {
        if (level < removed.length && removed[level] >= 0) {
            addAll(removed[level], removedLast[level]);
            removedLast[level] = removed[level] = -1;
        }
    }

    @Override
    public void set(final int[] tuple, final boolean status) {
        if (status) {
            add(tuple.clone());
        } else {
            remove(tuple);
        }
    }

    public void add(final int[] tuple) {
        final int index = size++;
        System.arraycopy(tuple, 0, tuples[index], 0, tuple.length);
        addCell(index);
    }

    public void addAll(final int index, final int last) {
        next[last] = first;
        first = index;
    }

    public void addCell(final int index) {
        next[index] = first;
        first = index;
    }

    public boolean contains(final int[] tuple) {
        for (int i = size; --i >= 0; ) {
            if (Arrays.equals(tuple, tuples[i])) {
                return true;
            }
        }
        return false;
    }

    public void remove(final int[] tuple) {
        final Iterator<int[]> itr = this.iterator();
        while (itr.hasNext()) {
            if (Arrays.equals(itr.next(), tuple)) {
                itr.remove();
                break;
            }
        }
    }

    public TupleListDynamic clone() throws CloneNotSupportedException {
        final TupleListDynamic list = (TupleListDynamic) super.clone();
        list.tuples = new int[tuples.length][];
        for (int i = tuples.length; --i >= 0; ) {
            list.tuples[i] = tuples[i].clone();
        }
        list.next = next.clone();
        list.removed = removed.clone();
        list.removedLast = removedLast.clone();
        return list;
    }

    @Override
    public LLIterator iterator() {
        return new LLIterator();
    }

    public String toString() {
        final StringBuilder stb = new StringBuilder();
        for (int[] tuple : this) {
            stb.append(Arrays.toString(tuple)).append(",");
        }
        return stb.toString();
    }

    public int count() {
        int count = 0;
        final LLIterator itr = iterator();
        while (itr.hasNext()) {
            count++;
            itr.next();
        }
        return count;
    }

    public class LLIterator implements Iterator<int[]> {

        private int current;

        private int prev;

        public LLIterator() {
            current = -1;
            prev = -1;
        }

        @Override
        public boolean hasNext() {
            return (current < 0) ? (first != -1) : (next[current] != -1);
        }

        @Override
        public int[] next() {
            if (current < 0) {
                current = first;
            } else {
                prev = current;
                current = next[current];
            }
            return tuples[current];
        }

        @Override
        public void remove() {
            remove(0);
        }

        public void remove(final int level) {
            if (prev < 0) {
                first = next[current];
            } else {
                next[prev] = next[current];
            }
            if (level >= removed.length) {
                expandRemoved(level + 1);
            }
            final int oldFirstRemoved = removed[level];
            next[current] = oldFirstRemoved;
            removed[level] = current;
            if (oldFirstRemoved < 0) {
                removedLast[level] = current;
            }
            current = prev;
        }
    }
}
