package org.jage.query;

/**
 * Describes a constraint which restricts a result of the query which contains
 * this constraint.
 * 
 * @author KrzS
 */
public interface IQueryConstraint {

    /**
	 * Checks if the given object is restricted by this constraint.
	 * 
	 * @param obj
	 *            the object to check
	 * @return <TT>true</TT> if this constraint does not restrict the given
	 *         object (value), so it can be included in the result of the query;
	 *         <TT>false</TT>- otherwise
	 */
    public boolean matches(Object obj);
}
