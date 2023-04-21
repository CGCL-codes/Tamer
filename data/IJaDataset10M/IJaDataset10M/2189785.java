package org.mandarax.util.resultsetfilters;

import org.mandarax.kernel.VariableTerm;

/**
 * Interface for ORDER BY conditions.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 3.0
 */
public interface OrderByCondition {

    /**
	 * Compare two objects.
	 * @param obj1 object 1
	 * @param obj2 object 2
	 * @return a number
	 * @see java.util.Comparator
	 */
    public int compareTo(Object obj1, Object obj2) throws OrderByException;

    /**
	 * Get the variable term.
	 * @return a variable term
	 */
    public VariableTerm getVariable();
}
