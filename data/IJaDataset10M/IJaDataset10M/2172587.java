package jopt.csp.spi.solver;

import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * A long array that stores data for an object that needs to record changes in a
 * {@link ChoicePointStack} so the changes can be pushed onto the stack and
 * later rolled back when the stack is popped.
 * <p>
 * This class is not thread safe.
 *  
 * @author Nick Coleman
 * @version $Revision: 1.2 $ 
 */
public class ChoicePointLongArray implements ChoicePointNumArraySet.StackList {

    private ArrayIntList basePointerStack;

    private ArrayIntList sizeStack;

    private long dataStack[];

    private int basePointer;

    private int size;

    private int depth;

    private int capacity;

    /**
     * Creates a new <code>ChoicePointLongArray</code>
     */
    public ChoicePointLongArray() {
        this.capacity = 10;
        this.basePointerStack = new ArrayIntList();
        this.sizeStack = new ArrayIntList();
        this.dataStack = new long[capacity];
        this.basePointer = 0;
        this.size = 0;
        this.depth = 0;
    }

    /**
     * Performs actions necessary to push data
     */
    public void push() {
        basePointerStack.add(basePointer);
        sizeStack.add(size);
        basePointer += size;
        size = 0;
        depth++;
    }

    public void pushDelta(Object delta) {
        push();
        if (delta != null) {
            long data[] = (long[]) delta;
            ensureCapacity(basePointer + data.length);
            System.arraycopy(data, 0, dataStack, basePointer, data.length);
            size = data.length;
        }
    }

    /**
     * Performs actions necessary to pop data
     */
    public void pop() {
        if (depth > 0) {
            depth--;
            basePointer = basePointerStack.removeElementAt(depth);
            size = sizeStack.removeElementAt(depth);
        } else {
            depth = 0;
            basePointer = 0;
            size = 0;
        }
    }

    public Object popDelta() {
        long data[] = null;
        if (size > 0) {
            data = new long[size];
            System.arraycopy(dataStack, basePointer, data, 0, size);
        }
        pop();
        return data;
    }

    /**
     * Returns current size of list
     */
    public int size() {
        return size;
    }

    /**
     * Ensures capacity exists in all internal value arrays
     * 
     * @param offset offset that needs to be available in new list
     */
    private void ensureCapacity(int offset) {
        if (offset >= capacity) {
            int oldCapacity = capacity;
            capacity = ((int) (((double) offset) * 1.25f)) + 1;
            long[] newStack = new long[capacity];
            System.arraycopy(dataStack, 0, newStack, 0, oldCapacity);
            dataStack = newStack;
        }
    }

    /**
     * Adds a value to the end of the list
     * 
     * @param val
     */
    public void add(long val) {
        set(size, val);
    }

    /**
     * Stores a value in list
     * 
     * @param offset    Offset of value in list
     * @param val       Value to append to the list
     */
    public void set(int offset, long val) {
        if (offset < 0) throw new IndexOutOfBoundsException("invalid value offset");
        int actualOffset = basePointer + offset;
        if (offset >= size) {
            ensureCapacity(actualOffset);
            int lastOffset = basePointer + size;
            for (int i = lastOffset; i <= actualOffset; i++) {
                dataStack[i] = 0;
            }
            size = offset + 1;
        }
        dataStack[actualOffset] = val;
    }

    /**
     * Retrieves a value in list
     * 
     * @param offset    Offset of value in list
     */
    public long get(int offset) {
        if (offset < 0) throw new IndexOutOfBoundsException("invalid value offset");
        if (offset >= size) return 0;
        int actualOffset = basePointer + offset;
        return dataStack[actualOffset];
    }

    /**
     * Retrieves a value from list
     * 
     * @param offset    Offset of value in list
     */
    public long remove(int offset) {
        if (offset < 0) throw new IndexOutOfBoundsException("invalid value offset");
        if (offset >= size) return 0;
        int actualOffset = basePointer + offset;
        long val = dataStack[actualOffset];
        if (offset < size - 1) System.arraycopy(dataStack, actualOffset, dataStack, actualOffset - 1, size - offset - 1);
        size--;
        return val;
    }
}
