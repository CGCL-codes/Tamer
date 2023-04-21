package com.sun.jini.norm;

import java.io.Serializable;
import java.util.Map;
import net.jini.id.Uuid;

/**
 * Base class for the objects Norm logs as delta for each state changing
 * operation.
 * 
 * @author Sun Microsystems, Inc.
 */
abstract class LoggedOperation implements Serializable {

    private static final long serialVersionUID = 2;

    /**
	 * The <code>Uuid</code> of the set this operation was on
	 * 
	 * @serial
	 */
    protected Uuid setID;

    /**
	 * Simple constructor
	 * 
	 * @param setID
	 *            The <code>Uuid</code> for the set this operation is on
	 */
    protected LoggedOperation(Uuid setID) {
        this.setID = setID;
    }

    /**
	 * Update state of the passed <code>Map</code> of <code>LeaseSet</code>s to
	 * reflect the state of server after this operation was performed.
	 * 
	 * @throws StoreException
	 *             if there is a problem applying the update
	 */
    abstract void apply(Map setTable) throws StoreException;
}
