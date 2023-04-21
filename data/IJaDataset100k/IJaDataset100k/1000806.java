package eu.pisolutions.collections.iterator;

import java.util.Iterator;

/**
 * {@link java.util.Iterator} that can be <em>reset</em> to restart the iteration from the first element.
 *
 * @author Laurent Pireyn
 */
public interface ResettableIterator<E> extends Iterator<E> {

    void reset();
}
