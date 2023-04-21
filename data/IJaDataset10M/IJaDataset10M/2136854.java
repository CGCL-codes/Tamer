package org.openxava.test.model;

/**
 * Remote interface for Product.
 */
public interface ProductRemote extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IProduct {

    public java.lang.String getPhotos() throws java.rmi.RemoteException;

    public void setPhotos(java.lang.String newPhotos) throws java.rmi.RemoteException;

    public int getSubfamilyNumber() throws java.rmi.RemoteException;

    public void setSubfamilyNumber(int newSubfamilyNumber) throws java.rmi.RemoteException;

    public org.openxava.test.model.Warehouse getWarehouseKey() throws java.rmi.RemoteException;

    public void setWarehouseKey(org.openxava.test.model.Warehouse warehouseKey) throws java.rmi.RemoteException;

    public java.lang.String getDescription() throws java.rmi.RemoteException;

    public void setDescription(java.lang.String newDescription) throws java.rmi.RemoteException;

    public java.math.BigDecimal getUnitPriceInPesetas() throws java.rmi.RemoteException;

    public java.math.BigDecimal getUnitPrice() throws java.rmi.RemoteException;

    public void setUnitPrice(java.math.BigDecimal newUnitPrice) throws java.rmi.RemoteException;

    public java.lang.String getRemarks() throws java.rmi.RemoteException;

    public void setRemarks(java.lang.String newRemarks) throws java.rmi.RemoteException;

    public long getNumber() throws java.rmi.RemoteException;

    public int getFamilyNumber() throws java.rmi.RemoteException;

    public void setFamilyNumber(int newFamilyNumber) throws java.rmi.RemoteException;

    public java.math.BigDecimal getPrice(java.lang.String country, java.math.BigDecimal tariff) throws org.openxava.test.model.ProductException, org.openxava.test.model.PriceException, java.rmi.RemoteException;

    public void increasePrice() throws java.rmi.RemoteException;

    public org.openxava.test.model.ProductData getData() throws java.rmi.RemoteException;

    public void setData(org.openxava.test.model.ProductData data) throws java.rmi.RemoteException;

    public org.openxava.test.model.ProductValue getProductValue() throws java.rmi.RemoteException;

    public void setProductValue(org.openxava.test.model.ProductValue value) throws java.rmi.RemoteException;
}
