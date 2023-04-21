package org.openxava.test.model;

/**
 * Home interface for Service.
 */
public interface ServiceHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Service";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.model/Service";

    public org.openxava.test.model.ServiceRemote create(java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.ServiceRemote create(org.openxava.test.model.ServiceData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.ServiceRemote create(org.openxava.test.model.ServiceValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.ServiceRemote findByNumber(int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.model.ServiceRemote findByPrimaryKey(org.openxava.test.model.ServiceKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
