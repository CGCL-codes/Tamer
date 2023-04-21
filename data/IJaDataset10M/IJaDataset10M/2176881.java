package org.apache.commons.collections.set;

import java.util.SortedSet;
import org.apache.commons.collections.functors.InstanceofPredicate;

/**
 * Decorates another <code>SortedSet</code> to validate that elements
 * added are of a specific type.
 * <p>
 * The validation of additions is performed via an instanceof test against 
 * a specified <code>Class</code>. If an object cannot be added to the
 * collection, an IllegalArgumentException is thrown.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Stephen Colebourne
 * @author Matthew Hawthorne
 */
public class TypedSortedSet {

    /**
     * Factory method to create a typed sorted set.
     * <p>
     * If there are any elements already in the set being decorated, they
     * are validated.
     * 
     * @param set  the set to decorate, must not be null
     * @param type  the type to allow into the collection, must not be null
     * @throws IllegalArgumentException if set or type is null
     * @throws IllegalArgumentException if the set contains invalid elements
     */
    public static SortedSet decorate(SortedSet set, Class type) {
        return new PredicatedSortedSet(set, InstanceofPredicate.getInstance(type));
    }

    /**
     * Restrictive constructor.
     */
    protected TypedSortedSet() {
    }
}
