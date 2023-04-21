package name.levering.ryan.sparql.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This is a set of variables bound to associated values. Visually, this is
 * often represented in table format, with the variables being column headers.
 * The set is immutable through the interface and so is thread-safe.
 * <p>
 * Besides the iterator method which allows traversal and the variable accessor,
 * the methods are all convenience methods that in some way modify the binding
 * set to produce a new bound set. This usage is somewhat like using that
 * java.lang.String.substring(), which maintains immutability.
 * 
 * @author Ryan Levering
 * @version 1.0
 */
public interface RdfBindingSet extends Collection {

    /**
	 * Gets an iterator over the collection of bound value rows in this set.
	 * 
	 * @return an iterator over the rows of type RdfBindingRow
	 */
    public Iterator iterator();

    /**
	 * Gets the list of variables that are the headers of the bound value set.
	 * Each row will have a value, possibly null, for each of the variables in
	 * this list.
	 * 
	 * @return a list of the bound variables in the set
	 */
    public List getVariables();

    /**
	 * Returns true if the value rows in the set are explicitly distinct.
	 * 
	 * @return true if the rows are distinct
	 */
    public boolean isDistinct();

    /**
	 * Returns true if the value rows in the set are explicitly ordered.
	 * 
	 * @return true if the rows are ordered
	 */
    public boolean isOrdered();

    /**
	 * Returns true if the set contains a particular binding row of data.
	 * 
	 * @param object the object, hopefully a RdfBindingRow
	 * @return true if the object is a row in this set
	 */
    public boolean contains(Object object);

    /**
	 * Returns the size of the binding set. Note that this will probably require
	 * having the entire binding set in memory.
	 * 
	 * @return the size of the set
	 */
    public int size();

    /**
	 * This is an alternative to toString() which currently returns the data
	 * representation of the binding set. This, on the other hand, returns a
	 * runtime description of the set without having to actually pull the data,
	 * which is useful for debugging/optimization.
	 * 
	 * @return a runtime description of the binding set structure
	 */
    public String describeSet();

    /**
	 * This is a useful visitor pattern method, especially used in conjunction
	 * with the stacked/streamed binding sets.
	 * 
	 * @param visitor the visitor to perform some function on the binding set
	 */
    public void accept(RdfBindingSetVisitor visitor);
}
