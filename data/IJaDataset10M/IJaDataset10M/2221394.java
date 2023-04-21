package net.sf.collections15;

import java.util.Iterator;

/**
 * Defines an iterator that can be reset back to an initial state.
 * <p/>
 * This interface allows an iterator to be repeatedly reused.
 *
 * @author Stephen Colebourne
 * @version $Revision: 1.2 $ $Date: 2004/10/17 01:02:42 $
 * @since Commons Collections 3.0
 */
public interface ResettableIterator<E> extends Iterator<E> {

    /**
     * Resets the iterator back to the position at which the iterator was
     * created.
     */
    public void reset();
}
