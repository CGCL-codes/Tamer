package org.openxava.test.ejb;

/**
 * Home interface for Customer.
 */
public interface CustomerHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/Customer";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.ejb/Customer";

    public org.openxava.test.ejb.Customer create(java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Customer create(org.openxava.test.ejb.CustomerData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Customer create(org.openxava.test.ejb.CustomerValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public java.util.Collection findBySeller(int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findByAlternateSeller(int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Customer findByNumber(int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findByNameLike(java.lang.String name) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findByNameLikeAndrelationWithSeller(java.lang.String name, java.lang.String relationWithSeller) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findNormalOnes() throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findSteadyOnes() throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findAll() throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.ejb.Customer findByPrimaryKey(org.openxava.test.ejb.CustomerKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
