package com.noahsloan.nutils.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Queue of a fixed size that drops the head element when a new element is
 * inserted and the queue is already full.
 * 
 * @author noah
 * 
 * @param <E>
 */
public class OverflowQueue<E> extends AbstractQueue<E> implements Serializable {

    private static final long serialVersionUID = 5629771219687332825L;

    private LinkedList<E> queue;

    private int capacity;

    public OverflowQueue(int capacity) {
        super();
        this.queue = new LinkedList<E>();
        this.capacity = capacity;
    }

    /**
	 * Creates an overflow queue and pushes all the elements of c onto it.
	 * Equivalent to calling addAll on a new queue of the same capacity.
	 * 
	 * @param c
	 * @param capacity
	 */
    public OverflowQueue(Collection<E> c, int capacity) {
        this(capacity);
        queue.addAll(c);
        fit();
    }

    @Override
    public java.util.Iterator<E> iterator() {
        return new ReverseIterator<E>(queue);
    }

    ;

    /**
	 * Returns an iterator pointed at the oldest element in the queue, the head,
	 * which advances towards the tail of the queue.
	 */
    public Iterator<E> standardIterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    public boolean offer(E o) {
        boolean result = queue.add(o);
        fit();
        return result;
    }

    /**
	 * Forces this queue to ensure that it is within its capacity.
	 */
    public void fit() {
        while (queue.size() > capacity) {
            queue.poll();
        }
    }

    public E peek() {
        return queue.peek();
    }

    public E poll() {
        return queue.poll();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(capacity);
        out.writeInt(queue.size());
        for (E e : queue) {
            out.writeObject(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        capacity = in.readInt();
        this.queue = new LinkedList<E>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            queue.add((E) in.readObject());
        }
    }
}
