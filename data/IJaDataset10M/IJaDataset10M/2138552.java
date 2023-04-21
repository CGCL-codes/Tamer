package whf.framework.util;

/**
 * @author wanghaifeng
 *
 */
public class LongList {

    long[] elements;

    int capacity;

    int size;

    public LongList() {
        this(50);
    }

    public LongList(int initialCapacity) {
        size = 0;
        capacity = initialCapacity;
        elements = new long[capacity];
    }

    public void add(long value) {
        elements[size] = value;
        size++;
        if (size == capacity) {
            capacity = capacity * 2;
            long[] newElements = new long[capacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    public void addAll(long[] values) {
        if (values == null || values.length == 0) return;
        for (long value : values) add(value);
    }

    public long get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " not valid.");
        }
        return elements[index];
    }

    public int size() {
        return size;
    }

    public long[] toArray() {
        int size = this.size;
        long[] newElements = new long[size];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        return newElements;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < this.size; i++) {
            buf.append(elements[i]).append(" ");
        }
        return buf.toString();
    }
}
