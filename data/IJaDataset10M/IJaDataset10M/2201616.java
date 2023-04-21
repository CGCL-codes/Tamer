package org.openxava.test.ejb;

/**
 * Remote interface for SellerLevel.
 */
public interface SellerLevel extends org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.ISellerLevel {

    public java.lang.String getDescription() throws java.rmi.RemoteException;

    public void setDescription(java.lang.String newDescription) throws java.rmi.RemoteException;

    public java.lang.String getId() throws java.rmi.RemoteException;

    public org.openxava.test.ejb.SellerLevelData getData() throws java.rmi.RemoteException;

    public void setData(org.openxava.test.ejb.SellerLevelData data) throws java.rmi.RemoteException;

    public org.openxava.test.ejb.SellerLevelValue getSellerLevelValue() throws java.rmi.RemoteException;

    public void setSellerLevelValue(org.openxava.test.ejb.SellerLevelValue value) throws java.rmi.RemoteException;
}
