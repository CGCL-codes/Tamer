package com.sun.j2ee.blueprints.processmanager.ejb;

import javax.ejb.CreateException;

/**
 * The home interface of the ProcessManager Entity EJB.
 */
public interface ProcessManagerLocalHome extends javax.ejb.EJBLocalHome {

    public ProcessManagerLocal create() throws CreateException;
}
