package org.deri.iris.api.evaluation.algebra;

import org.deri.iris.api.basics.IPredicate;

/**
 * <p>
 * Represents a component for describing a relation. This description
 * is used during the evaluation of a relational algebra expression.
 * </p>
 * 
* @author Darko Anicic, DERI Innsbruck
* @date 13.12.2006 17:27:34
*/
public interface IRelationDescriptor extends IComponent {

    /**
	 * Returns the predicate of an atom which is bound to this relation.
	 * 
	 * @return	The predicate.
	 */
    public IPredicate getPredicate();
}
