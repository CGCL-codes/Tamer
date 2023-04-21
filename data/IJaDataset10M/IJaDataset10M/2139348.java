package org.nexopenframework.core.validation;

import java.util.List;

/**
 * <p>Base contract for providers for dealing with custom validation. Main issues to be fullfied by implementators
 * is to create a <code>Thread-Safe</code> class.</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2007-09-05 22:02:44 +0100 $
 * @since 0.4.0
 */
public interface ValidatorDelegate {

    /**
	 * <p>Perform validations related to the given entity class and returns a collection with such error validations. 
	 * If no violation constraints are found an <code>empty</code> collection is returned</p>
	 * 
	 * @throws IllegalArgumentException if entity is null
	 * @param entity the entity to be validated
	 * @return a collection of errors found or an empty collection
	 */
    List validate(Object entity);
}
