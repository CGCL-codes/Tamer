package net.sf.parser4j.collection;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class IntSet {

    private static final int INITIAL_SIZE = 32;

    private int[] valuesHolder = new int[INITIAL_SIZE];

    private int size;

    public IntSet(final IntSet intSet) {
        super();
        final int[] valuesHolderOfList = intSet.valuesHolder;
        valuesHolder = Arrays.copyOf(valuesHolderOfList, valuesHolderOfList.length);
        this.size = intSet.size;
    }

    public IntSet() {
        super();
    }

    public void clear() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int[] getValuesHolder() {
        return valuesHolder;
    }

    public void add(final int value) {
        boolean found = false;
        for (int index = 0; !found && index < size; index++) {
            found = valuesHolder[index] == value;
        }
        if (!found) {
            if (size == valuesHolder.length) {
                valuesHolder = Arrays.copyOf(valuesHolder, valuesHolder.length << 1);
            }
            valuesHolder[size++] = value;
        }
    }

    public void removeAll(final IntSet toRemove) {
        final int size = toRemove.size;
        final int[] valuesHolder = toRemove.valuesHolder;
        for (int index = 0; index < size; index++) {
            remove(valuesHolder[index]);
        }
    }

    public void remove(final int value) {
        boolean found = false;
        int index;
        for (index = 0; !found && index < size; index++) {
            found = valuesHolder[index] == value;
        }
        if (found) {
            if (index < size) {
                System.arraycopy(valuesHolder, index, valuesHolder, index - 1, size - index);
            }
            size--;
        }
    }

    public void addAll(final IntSet toAdd) {
        final int size = toAdd.size;
        final int[] valuesHolder = toAdd.valuesHolder;
        for (int index = 0; index < size; index++) {
            add(valuesHolder[index]);
        }
    }

    public boolean contains(final int value) {
        boolean found = false;
        for (int index = 0; !found && index < size; index++) {
            found = valuesHolder[index] == value;
        }
        return found;
    }

    public Set<Integer> toSet() {
        final Set<Integer> set = new TreeSet<Integer>();
        for (int index = 0; index < size; index++) {
            set.add(valuesHolder[index]);
        }
        return set;
    }

    @Override
    public String toString() {
        return "[" + Arrays.toString(Arrays.copyOf(valuesHolder, size)) + "]";
    }
}
