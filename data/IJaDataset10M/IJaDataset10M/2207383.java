package tudresden.ocl20.pivot.essentialocl.standardlibrary;

/**
 * 
 *
 * @author Matthias Braeuer
 * @version 1.0 30.03.2007
 */
public interface OclOrderedSet<T extends OclAny> extends OclSortedCollection<T> {

    /**
	 * 
	 * @param lower
	 * @param upper
	 * @return the sub-set of <code>this</code> starting at number
	 *         <code>lower</code>, up to and including element number
	 *         <code>upper</code>.
	 */
    OclOrderedSet<T> subOrderedSet(OclInteger lower, OclInteger upper);

    /**
	 * 
	 * @param o
	 * 
	 * @return The orderedset of elements, consisting of all elements of
	 *         <code>this</code>, followed by <code>o</code>.
	 */
    OclOrderedSet<T> append(T o);

    /**
	 * 
	 * @param o
	 * 
	 * @return The orderedset consisting of <code>o</code>, followed by all
	 *         elements in <code>this</code>.
	 */
    OclOrderedSet<T> prepend(T o);

    /**
	 * 
	 * @param index
	 * @param o
	 * 
	 * @return The orderedset consisting of <code>this</code> with
	 *         <code>o</code> inserted at position <code>index</code>.
	 */
    OclOrderedSet<T> insertAt(OclInteger index, T o);

    /**
	 * 
	 * @param o
	 * 
	 * @return the ordered set containing all elements of <code>this</code>
	 *         plus <code>o</code>.
	 */
    OclOrderedSet<T> including(T o);

    /**
	 * 
	 * @param o
	 * 
	 * @return the ordered set containing all elements of <code>this</code>
	 *         apart from <code>o</code>.
	 */
    OclOrderedSet<T> excluding(T o);

    <T2 extends OclAny> OclOrderedSet<T2> flatten();
}
