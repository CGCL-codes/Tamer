package com.tensegrity.palobrowser.table;

/**
 * <code>DomainObject</code>
 * <p>
 * A domain object is usually associated with a column or row model in a palo 
 * table model. This class introduces a layer of indirection for returning an 
 * object suitable for identifying a row or column model.
 * </p>
 *
 * @author Stepan Rutz
 * @version $Id$
 */
public interface DomainObject {

    /**
	 * Returns an object suitable for identifying 
	 * @return the identifier object
	 */
    Object getValueIdentifier();
}
