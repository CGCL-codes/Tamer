package org.deri.iris.api.operations.relation;

import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * An interface for the union operation. The union of 
 * relations R and S, denoted as R U S, is the set of 
 * tuples that are in R or S or both. 
 * </p>
 * <p>
 * @see org.deri.iris.api.factory.IRelationOperationsFactory#
 * </p>
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:29:27
 */
public interface IUnion {

    /**
	 * Performs the union operation.
	 * 
	 * @return	The relation which is a result of the union operation.
	 */
    public IMixedDatatypeRelation union();
}
