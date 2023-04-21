package org.ikasan.connector.base.outbound.local;

import javax.resource.ResourceException;
import javax.resource.spi.LocalTransaction;
import org.ikasan.connector.base.outbound.EISManagedConnection;

/**
 * This is an abstract class representing the XAManagedConnection 
 * for the resource adapter.
 *  
 * This is derived form the EISManagedConnection, but requires the derived 
 * classes implement the XA methods.
 *  
 * @author Ikasan Development Team
 */
public abstract class EISLocalManagedConnection extends EISManagedConnection implements LocalTransaction {

    /**
     * When a connection is in an auto-commit mode, an operation on the 
     * connection automatically commits after it has been executed. 
     * The auto-commit mode must be off if multiple interactions have 
     * to be grouped in a single transaction, either local or XA, 
     * and committed or rolled back as a unit.
     * 
     * This is a Local Transaction and may have multiple operations within 
     * a single unit, so we must set auto-commit to false.
     * 
     * @return false
     */
    @Override
    public boolean getAutoCommit() {
        return false;
    }

    @Override
    public abstract void associateConnection(Object arg0) throws ResourceException;

    public abstract void begin() throws ResourceException;

    public abstract void commit() throws ResourceException;

    public abstract void rollback() throws ResourceException;
}
