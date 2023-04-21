package jaxlib.col.queue;

import java.util.NoSuchElementException;
import jaxlib.col.AbstractXCollection;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractXQueue.java 2659 2008-10-13 12:15:27Z joerg_wassmer $
 */
public abstract class AbstractXQueue<E> extends AbstractXCollection<E> implements XQueue<E> {

    protected AbstractXQueue() {
        super();
    }

    public abstract boolean offer(E e);

    public abstract E peek();

    public abstract E poll();

    @Override
    public boolean add(E e) {
        if (offer(e)) return true; else throw new IllegalStateException("Queue full");
    }

    @Override
    public void clear() {
        while (poll() != null) ;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    public E element() {
        E e = peek();
        if (e != null) return e; else throw new NoSuchElementException();
    }

    @Override
    public int hashCode() {
        return identityHashCode();
    }

    public E remove() {
        E e = poll();
        if (e != null) return e; else throw new NoSuchElementException();
    }

    @Override
    public int replaceEach(Object oldElement, E newElement) {
        if (newElement == null) throw new NullPointerException("newElement");
        if ((oldElement == null) || (oldElement == newElement)) return 0;
        int count = 0;
        while (remove(oldElement)) count++;
        for (int i = count; --i >= 0; ) {
            if (!offer(newElement)) break;
        }
        return count;
    }

    @Override
    public int replaceEachIdentical(Object oldElement, E newElement) {
        if (newElement == null) throw new NullPointerException("newElement");
        if ((oldElement == null) || (oldElement == newElement)) return 0;
        int count = 0;
        while (removeId(oldElement)) count++;
        for (int i = count; --i >= 0; ) {
            if (!offer(newElement)) break;
        }
        return count;
    }
}
