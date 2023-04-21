package com.om.onec.adapter.cci;

import com.om.onec.adapter.spi.*;
import javax.resource.cci.*;
import javax.resource.*;

/**
 * @author Sergey Yu. Terentjev
 * @version 1.1
 * @description This implementation class represents an application level connection  handle that is used by a component
 * to access an EIS instance.
 * @difficulty medium
 * @type Product Requirement
 * @stereotype tested
 * @testcase com.om.onec.adapter.cci.Test.TestConnectionImpl
 */
public class ConnectionImpl implements Connection {

    /**
     * @stereotype constructor
     * @input ManagedConnectionImpl
     */
    public ConnectionImpl(ManagedConnectionImpl mc) {
        this.mc = mc;
    }

    /**
     * @description Returns Interaction
     * @exception javax.resource.ResourceException
     * @return javax.resource.cci.Interaction
     * @type Product Requirement
     */
    public Interaction createInteraction() throws ResourceException {
        throw new NotSupportedException("Not supported this time");
    }

    public void close() throws ResourceException {
        if (mc == null) {
            throw new ResourceException("Connection already closed");
        }
        mc.fireConnectionClosed(this);
        mc = null;
    }

    /**
     * @description Return local transaction
     * @exception javax.resource.ResourceException
     * @return javax.resource.cci.LocalTransaction
     * @type Product Requirement
     */
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("Not supported this time");
    }

    /**
     * @description Return connection metadata
     * @exception javax.resource.ResourceException
     * @return javax.resource.cci.LocalTransaction
     * @type Product Requirement
     */
    public ConnectionMetaData getMetaData() throws ResourceException {
        throw new NotSupportedException("Not supported this time");
    }

    /**
     * @description Return resultset info
     * @exception javax.resource.ResourceException
     * @return javax.resource.cci.ResultSetInfo
     * @type Product Requirement
     */
    public ResultSetInfo getResultSetInfo() throws ResourceException {
        throw new NotSupportedException("Not supported this time");
    }

    public void setAutoCommit(boolean par1) {
    }

    public boolean getAutoCommit() {
        return false;
    }

    /**
     * @description clear ManagedConnection
     * @type Product Requirement
     */
    public void invalidate() {
        mc = null;
    }

    /** @description ManagedconnectionFactoryImpl instance */
    private ManagedConnectionImpl mc;
}
