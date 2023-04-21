package uk.ac.rdg.resc.edal.util;

/**
 * <p>A resizeable array of signed long integers.  Data are stored in an array of primitive longs.</p>
 * <p>Instances of this class are not thread safe.</p>
 * @author Jon
 */
public final class RLongArray extends RArray {

    /** The maximum value that can be stored in this array */
    public static final long MAX_VALUE = Long.MAX_VALUE;

    /** The minimum value that can be stored in this array */
    public static final long MIN_VALUE = Long.MIN_VALUE;

    /**
     * Creates an array in which the initial capacity is set the same as the
     * chunk size.
     */
    public RLongArray(int chunkSize) {
        this(chunkSize, chunkSize);
    }

    /**
     * Creates an array with the given initial capacity and chunk size.
     * @param initialCapacity The number of elements in the storage array
     * @param chunkSize The number of storage elements that will be added each
     * time the storage array grows.
     */
    public RLongArray(int initialCapacity, int chunkSize) {
        super(initialCapacity, chunkSize);
    }

    @Override
    protected long[] makeStorage(int capacity) {
        return new long[capacity];
    }

    /**
     * Returns the <i>i</i>th element of the array.
     * @param i The index of the element to return.
     * @return the <i>i</i>th element of the array.
     * @throws ArrayIndexOutOfBoundsException if {@code i >= size()}
     */
    @Override
    public long getLong(int i) {
        return this.getStorage()[i];
    }

    @Override
    public int getInt(int i) {
        long val = getLong(i);
        if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
            throw new ArithmeticException(val + " cannot be represented as a 4-byte integer");
        }
        return (int) val;
    }

    private long[] getStorage() {
        return (long[]) this.storage;
    }

    @Override
    protected int getStorageLength() {
        return this.getStorage().length;
    }

    @Override
    protected void setElement(int index, long value) {
        this.getStorage()[index] = value;
    }

    @Override
    public void swapElements(int i1, int i2) {
        long[] arr = this.getStorage();
        long temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    @Override
    protected long getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected long getMaxValue() {
        return MAX_VALUE;
    }
}
