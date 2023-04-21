package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;

public interface ICustomerState extends org.openxava.model.IModel {

    org.openxava.test.model.IState getState() throws RemoteException;

    void setState(org.openxava.test.model.IState newState) throws RemoteException;

    org.openxava.test.model.ICustomer getCustomer() throws RemoteException;

    void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;
}
