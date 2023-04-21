package org.openxava.qamanager.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IShipmentMethod extends org.openxava.model.IModel {

    String getDescription() throws RemoteException;

    void setDescription(String description) throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    String getId() throws RemoteException;
}
