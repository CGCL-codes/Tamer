package eu.pisolutions.collections.iterator;

import java.util.NoSuchElementException;
import eu.pisolutions.lang.Validations;

/**
 * {@link java.util.ListIterator} on an array.
 *
 * @author Laurent Pireyn
 */
public final class ArrayListIterator<E> extends AbstractListIterator<E> implements ResettableIterator<E> {

    private final E[] array;

    private int index;

    private boolean positioned;

    public ArrayListIterator(E[] array) {
        super();
        Validations.notNull(array, "array");
        this.array = array;
    }

    public boolean hasPrevious() {
        return this.index > 0;
    }

    public boolean hasNext() {
        return this.index < this.array.length;
    }

    public int previousIndex() {
        return this.index - 1;
    }

    public int nextIndex() {
        return this.index;
    }

    public E previous() {
        if (!this.hasPrevious()) {
            throw new NoSuchElementException();
        }
        this.positioned = true;
        return this.array[--this.index];
    }

    public E next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.positioned = true;
        return this.array[this.index++];
    }

    @Override
    public void set(E element) {
        if (!this.positioned) {
            throw new IllegalStateException();
        }
        this.array[this.index] = element;
    }

    public void reset() {
        this.index = 0;
        this.positioned = false;
    }
}
