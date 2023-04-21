package org.apache.commons.collections;

/**
 * Defines an iterator that operates over an ordered <code>Map</code>.
 * <p>
 * This iterator allows both forward and reverse iteration through the map.
 *  
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public interface OrderedMapIterator extends MapIterator, OrderedIterator {

    /**
     * Checks to see if there is a previous entry that can be iterated to.
     *
     * @return <code>true</code> if the iterator has a previous element
     */
    boolean hasPrevious();

    /**
     * Gets the previous <em>key</em> from the <code>Map</code>.
     *
     * @return the previous key in the iteration
     * @throws java.util.NoSuchElementException if the iteration is finished
     */
    Object previous();
}
