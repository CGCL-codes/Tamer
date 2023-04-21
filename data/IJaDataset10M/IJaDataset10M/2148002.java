package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Predicate;

/**
 * Predicate implementation that returns the opposite of the decorated predicate.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public final class NotPredicate implements Predicate, PredicateDecorator, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -2654603322338049674L;

    /** The predicate to decorate */
    private final Predicate iPredicate;

    /**
     * Factory to create the not predicate.
     * 
     * @param predicate  the predicate to decorate, not null
     * @return the predicate
     * @throws IllegalArgumentException if the predicate is null
     */
    public static Predicate getInstance(Predicate predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate must not be null");
        }
        return new NotPredicate(predicate);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     * 
     * @param predicate  the predicate to call after the null check
     */
    public NotPredicate(Predicate predicate) {
        super();
        iPredicate = predicate;
    }

    /**
     * Evaluates the predicate returning the opposite to the stored predicate.
     * 
     * @param object  the input object
     * @return true if predicate returns false
     */
    public boolean evaluate(Object object) {
        return !(iPredicate.evaluate(object));
    }

    /**
     * Gets the predicate being decorated.
     * 
     * @return the predicate as the only element in an array
     * @since Commons Collections 3.1
     */
    public Predicate[] getPredicates() {
        return new Predicate[] { iPredicate };
    }
}
