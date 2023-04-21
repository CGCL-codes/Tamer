package ijaux.iter;

import ijaux.Constants;

/**
 * @author Dimiter Prodanov
 * @param <E>
 */
public interface ForwardIterator<E> extends Constants, IndexedIterator<E> {

    E first();

    boolean hasNext();

    E next();

    void put(E value);
}
