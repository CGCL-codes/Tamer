package org.openxava.test.ejb;

/**
 * Home interface for DeliveryDetail.
 */
public interface DeliveryDetailHome extends javax.ejb.EJBHome {

    public static final String COMP_NAME = "java:comp/env/ejb/DeliveryDetail";

    public static final String JNDI_NAME = "@subcontext@/ejb/org.openxava.test.ejb/DeliveryDetail";

    public org.openxava.test.ejb.DeliveryDetail create(org.openxava.test.ejb.Delivery container, int counter, java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.DeliveryDetail create(org.openxava.test.ejb.DeliveryKey containerKey, int counter, java.util.Map values) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.DeliveryDetail create(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailData data) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.DeliveryDetail create(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public org.openxava.test.ejb.DeliveryDetail create(org.openxava.test.ejb.DeliveryKey containerKey, int counter, org.openxava.test.ejb.DeliveryDetailValue value) throws javax.ejb.CreateException, org.openxava.validators.ValidationException, java.rmi.RemoteException;

    public java.util.Collection findByDelivery(int number, int type_number, int invoice_year, int invoice_number) throws javax.ejb.FinderException, java.rmi.RemoteException;

    public org.openxava.test.ejb.DeliveryDetail findByPrimaryKey(org.openxava.test.ejb.DeliveryDetailKey pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
