package hu.ihash.common.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A size-constrained ordered collection, for managing the <code>n</code>
 * smallest entities.
 * 
 * @author Gergely Kiss
 * 
 * @param <E>
 */
public class FixedOrderedList<T> {

    /** The ordered array of elements. */
    private final ArrayList<T> orderedList;

    /** The ordered array of values. */
    private final double[] values;

    /** Maximum size of the collection. */
    private final int maxSize;

    public FixedOrderedList(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Collection size must be positive.");
        this.maxSize = maxSize;
        this.values = new double[maxSize];
        this.orderedList = new ArrayList<T>(maxSize + 1);
        clear();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void clear() {
        Arrays.fill(values, Double.POSITIVE_INFINITY);
        orderedList.clear();
    }

    /**
	 * Adds a new element to the ordered list.
	 * 
	 * The value must not be infinite.
	 * 
	 * @param e
	 * @param value
	 */
    public void add(T e, double value) {
        int size = orderedList.size();
        if ((size == maxSize) && (values[0] >= value)) {
            return;
        }
        int pos = Arrays.binarySearch(values, value);
        int idx;
        if (pos >= 0) {
            idx = pos;
        } else {
            idx = -(pos + 1);
        }
        if (size < maxSize) {
            System.arraycopy(values, idx, values, idx + 1, maxSize - idx - 1);
            values[idx] = value;
            orderedList.add(idx, e);
            size++;
        } else {
            System.arraycopy(values, 1, values, 0, idx - 1);
            values[idx - 1] = value;
            orderedList.add(idx, e);
            orderedList.remove(0);
        }
    }

    public List<OrderableValue<T>> getAscendingList() {
        int size = orderedList.size();
        ArrayList<OrderableValue<T>> r = new ArrayList<OrderableValue<T>>(size);
        for (int i = 0; i < size; i++) {
            r.add(new OrderableValue<T>(orderedList.get(i), values[i]));
        }
        return r;
    }

    public List<OrderableValue<T>> getDescendingList() {
        int size = orderedList.size();
        ArrayList<OrderableValue<T>> r = new ArrayList<OrderableValue<T>>(size);
        for (int i = size - 1; i >= 0; i--) {
            r.add(new OrderableValue<T>(orderedList.get(i), values[i]));
        }
        return r;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int size = orderedList.size();
        for (int i = 0; i < size; i++) {
            sb.append(orderedList.get(i));
            sb.append(" (").append(values[i]).append(")");
            if (i < (size - 1)) sb.append(',');
        }
        sb.append("]");
        return sb.toString();
    }

    public int size() {
        return orderedList.size();
    }
}
