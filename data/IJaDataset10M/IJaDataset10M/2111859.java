package org.openxava.test.model;

/**
 * Remote interface for AverageSpeed.
 */
public interface AverageSpeedRemote extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IAverageSpeed {

    public int getSpeed() throws java.rmi.RemoteException;

    public void setSpeed(int newSpeed) throws java.rmi.RemoteException;

    public java.lang.String getOid() throws java.rmi.RemoteException;

    public org.openxava.test.model.IVehicle getVehicle() throws java.rmi.RemoteException;

    public org.openxava.test.model.VehicleRemote getVehicleRemote() throws java.rmi.RemoteException;

    public void setVehicle(org.openxava.test.model.IVehicle newVehicle) throws java.rmi.RemoteException;

    public org.openxava.test.model.VehicleKey getVehicleKey() throws java.rmi.RemoteException;

    public void setVehicleKey(org.openxava.test.model.VehicleKey key) throws java.rmi.RemoteException;

    public java.lang.String getVehicle_oid() throws java.rmi.RemoteException;

    public org.openxava.test.model.IDriver getDriver() throws java.rmi.RemoteException;

    public org.openxava.test.model.DriverRemote getDriverRemote() throws java.rmi.RemoteException;

    public void setDriver(org.openxava.test.model.IDriver newDriver) throws java.rmi.RemoteException;

    public org.openxava.test.model.DriverKey getDriverKey() throws java.rmi.RemoteException;

    public void setDriverKey(org.openxava.test.model.DriverKey key) throws java.rmi.RemoteException;

    public java.lang.Integer getDriver_number() throws java.rmi.RemoteException;

    public org.openxava.test.model.AverageSpeedData getData() throws java.rmi.RemoteException;

    public void setData(org.openxava.test.model.AverageSpeedData data) throws java.rmi.RemoteException;

    public org.openxava.test.model.AverageSpeedValue getAverageSpeedValue() throws java.rmi.RemoteException;

    public void setAverageSpeedValue(org.openxava.test.model.AverageSpeedValue value) throws java.rmi.RemoteException;
}
