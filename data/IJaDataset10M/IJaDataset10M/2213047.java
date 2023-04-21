package org.perfectjpattern.core.structural.adapter;

import java.util.*;
import org.perfectjpattern.core.api.structural.adapter.*;

/**
 * Adapter that provides a {@link Queue} based on a {@link List} implementation.
 * That is, it adapts the {@link List} to behave and look like a {@link Queue}. 
 * Please note that this is not an exhaustive implementation of {@link Queue} 
 * nor it replaces the proper JDK {@link Queue} implementations. The purpose 
 * of this Adapter is only to demonstrate:
 * <ul>
 * <li>How to use PerfectJPattern's {@link Adapter} implementation to adapt an 
 * implementation to a given target interface</li>
 * <li>How the componentized Adapter facilitates adapting types by means of:
 * <ul>
 * <li>Automatically delegating to the adapter implementation calls made to 
 * the target interface in this case e.g. {@link #remove()}, {@link #poll()} 
 * {@link #element()} {@link #peek()}</li>
 * <li>Otherwise automatically delegating to the Adaptee implementation using 
 * a specified {@link IAdaptingStrategy} in this case 
 * {@link NameMatchAdaptingStrategy} where it maps e.g. {@link Queue#
 * offer(Object)} to {@link ArrayList#add(Object)}</li>
 * </ul>
 * </li>
 * </ul>
 * Noteworthy is how this implementation did NOT have to provide a full blown
 * implementation of all of the Target {@link Queue} interface methods e.g. 
 * {@link Collection#addAll(Collection)} or {@link Collection#toArray()} as 
 * the call forwarding is automatically handled by PerfectJPattern's 
 * {@link Adapter} 
 * <br/><br/>
 * 
 * @param <E> Type of the {@link Queue} elements
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Feb 5, 2009 8:55:42 PM $
 */
public class List2QueueAdapter<E> extends Adapter<Queue<E>, List<E>> {

    /**
     * Constructs a {@link List2QueueAdapter} from the underlying Adaptee 
     * {@link List} implementation.
     * 
     * @param anAdaptee The Adaptee {@link List} implementation to adapt
     * @throws IllegalArgumentException 'anAdaptee' must not be null
     */
    @SuppressWarnings("unchecked")
    public List2QueueAdapter(List<E> anAdaptee) throws IllegalArgumentException {
        super((Class<Queue<E>>) ((Class<?>) Queue.class), anAdaptee, new NameMatchAdaptingStrategy(METHODS_MAPPING));
    }

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E remove() throws NoSuchElementException {
        List<E> myList = getUnderlying();
        if (myList.isEmpty()) {
            throw new NoSuchElementException("The Queue is empty");
        }
        E myElement = myList.get(0);
        myList.remove(0);
        return myElement;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    public E poll() {
        E myElement = null;
        try {
            myElement = remove();
        } catch (NoSuchElementException anException) {
        }
        return myElement;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from {@link #peek peek} only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E element() {
        List<E> myList = getUnderlying();
        if (myList.isEmpty()) {
            throw new NoSuchElementException("The Queue is empty");
        }
        E myElement = myList.get(0);
        return myElement;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns <tt>null</tt> if this queue is empty.
     *
     * @return the head of this queue, or <tt>null</tt> if this queue is empty
     */
    public E peek() {
        E myElement = null;
        try {
            myElement = element();
        } catch (NoSuchElementException anException) {
        }
        return myElement;
    }

    private static final Map<String, String> METHODS_MAPPING = new HashMap<String, String>();

    static {
        METHODS_MAPPING.put("offer", "add");
    }
}
