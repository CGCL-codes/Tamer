package joshua.util;

import java.util.AbstractList;

/**
 * @author lane
 *
 */
public class ArrayBackedList<E> extends AbstractList<E> {

    protected final E[] array;

    public ArrayBackedList(E... array) {
        this.array = array;
    }

    @Override
    public E get(int index) {
        return array[index];
    }

    @Override
    public int size() {
        return array.length;
    }
}
