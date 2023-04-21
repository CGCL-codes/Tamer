package org.odmg;

/**
 * Interface for any collection that is queryable via an OQL WHERE clause.
 *
 * @version $Id: QueryableCollection.java,v 1.3 2005/08/12 02:56:46 dsyrstad Exp $
 * @author <a href="mailto:dsyrstad@ener-j.org">Dan Syrstad</a>
 */
public interface QueryableCollection<E> {

    /**
     * Selects the single element of the collection for which the provided OQL query
     * predicate is true.
     * @param	predicate	An OQL boolean query predicate.
     * @return The element that evaluates to true for the predicate. If no element
     * evaluates to true, null is returned.
     * @exception	QueryInvalidException	The query predicate is invalid.
     */
    public E selectElement(String predicate) throws QueryInvalidException;

    /**
     * Access all of the elements of the collection that evaluate to true for the
     * provided query predicate.
     * @param	predicate	An OQL boolean query predicate.
     * @return	An iterator used to iterate over the elements that evaluated true for the predicate.
     * @exception	QueryInvalidException	The query predicate is invalid.
     */
    public java.util.Iterator<E> select(String predicate) throws QueryInvalidException;

    /**
     * Evaluate the boolean query predicate for each element of the collection and
     * return a new collection that contains each element that evaluated to true.
     * @param	predicate	An OQL boolean query predicate.
     * @return	A new collection containing the elements that evaluated true for the predicate.
     * @exception	QueryInvalidException	The query predicate is invalid.
     */
    public DCollection<E> query(String predicate) throws QueryInvalidException;

    /**
     * Determines whether there is an element of the collection that evaluates to true
     * for the predicate.
     * @param	predicate	An OQL boolean query predicate.
     * @return	True if there is an element of the collection that evaluates to true
     * for the predicate, otherwise false.
     * @exception	QueryInvalidException	The query predicate is invalid.
     */
    public boolean existsElement(String predicate) throws QueryInvalidException;
}
