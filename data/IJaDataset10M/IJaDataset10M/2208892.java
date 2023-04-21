package org.openxava.test.ejb;

/**
 * Home interface for Warehouse2.
 */
public interface Warehouse2Home extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Warehouse2";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.ejb/Warehouse2";

    public org.openxava.test.ejb.Warehouse2 create(java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Warehouse2 create(org.openxava.test.ejb.Warehouse2Data data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Warehouse2 create(org.openxava.test.ejb.Warehouse2Value value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Warehouse2 findByPrimaryKey(org.openxava.test.ejb.Warehouse2Key pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
