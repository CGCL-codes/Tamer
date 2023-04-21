package at.momberban.game.me;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * A synchronized minimal queue implementation.
 * @author nfk
 */
public class SynchronizedQueue {

    public final Vector v;

    /**
     * Constructs a new instance of {@link SynchronizedQueue}.
     */
    public SynchronizedQueue() {
        this.v = new Vector();
    }

    /**
     * Clears all elements from the queue.
     */
    public synchronized void clear() {
        v.removeAllElements();
    }

    /**
     * Adds the specified object to the queue.
     * @param obj the object to add
     * @throws NullPointerException if obj is null
     */
    public synchronized void add(final Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj must not be null.");
        }
        v.addElement(obj);
    }

    /**
     * Returns the next element in the queue without actually removing it.
     * @return the next element
     * @throws NoSuchElementException if queue is empty
     */
    public synchronized Object element() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return v.firstElement();
    }

    /**
     * Returns the next element in the queue and removes it.
     * @return the next element
     * @throws NoSuchElementException if queue is empty
     */
    public synchronized Object remove() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        final Object result = v.firstElement();
        v.removeElementAt(0);
        return result;
    }

    /**
     * Returns the number of elements in the queue.
     * @return number of elements in the queue
     */
    public synchronized int size() {
        return v.size();
    }

    /**
     * Returns if the queue is empty.
     * @return 0 if the queue is empty, > 0 if not.
     */
    public synchronized boolean isEmpty() {
        return (v.size() == 0);
    }
}
