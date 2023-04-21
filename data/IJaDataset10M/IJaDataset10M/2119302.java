package org.openxava.test.model;

/**
 * Home interface for InvoiceDetail.
 */
public interface InvoiceDetailHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/InvoiceDetail";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.model/InvoiceDetail";

    public org.openxava.test.model.InvoiceDetailRemote create(org.openxava.test.model.InvoiceRemote container, int counter, java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.InvoiceDetailRemote create(org.openxava.test.model.InvoiceKey containerKey, int counter, java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.InvoiceDetailRemote create(org.openxava.test.model.InvoiceRemote container, int counter, org.openxava.test.model.InvoiceDetailData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.InvoiceDetailRemote create(org.openxava.test.model.InvoiceRemote container, int counter, org.openxava.test.model.InvoiceDetailValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.model.InvoiceDetailRemote create(org.openxava.test.model.InvoiceKey containerKey, int counter, org.openxava.test.model.InvoiceDetailValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public java.util.Collection findByProduct(long number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findByInvoice(int year, int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public java.util.Collection findBySoldBy(int number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.model.InvoiceDetailRemote findByPrimaryKey(org.openxava.test.model.InvoiceDetailKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
