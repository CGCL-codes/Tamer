package net.sf.smailstandalone.util;

/**
 *
 * @since 19.02.2011
 * @author S�bastien CHATEL
 */
public class ArrayFifo<T> implements Fifo<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private T[] elements;

    private int head = 0;

    private int footer = 0;

    private int size = 0;

    private int capacity;

    public int size() {
        return this.size;
    }

    public int capacity() {
        return capacity;
    }

    public ArrayFifo() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayFifo(int capacity) {
        this.capacity = capacity;
        this.elements = (T[]) new Object[capacity];
    }

    public boolean add(T value) {
        if (this.size == this.capacity) {
            grows();
        }
        this.elements[this.footer] = value;
        this.footer = (this.footer + 1) % this.capacity;
        this.size++;
        return true;
    }

    public T poll() {
        if (this.size == 0) {
            return null;
        }
        T value = this.elements[this.head];
        this.elements[this.head] = null;
        this.head = (this.head + 1) % this.capacity;
        this.size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    private void grows() {
        T[] temp = (T[]) new Object[this.capacity * 2];
        System.arraycopy(this.elements, this.head, temp, 0, this.capacity - this.head);
        System.arraycopy(this.elements, 0, temp, this.capacity - this.head, this.footer);
        this.elements = temp;
        this.head = 0;
        this.footer = this.capacity;
        this.capacity = this.elements.length;
    }
}
