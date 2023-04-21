package org.openxava.qamanager.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IProjectContact extends org.openxava.model.IModel {

    String getCustomerChatID() throws RemoteException;

    void setCustomerChatID(String customerChatID) throws RemoteException;

    java.lang.String getOtherInfo() throws RemoteException;

    void setOtherInfo(java.lang.String otherInfo) throws RemoteException;

    String getTelephone() throws RemoteException;

    void setTelephone(String telephone) throws RemoteException;

    String getEmail() throws RemoteException;

    void setEmail(String email) throws RemoteException;

    String getName() throws RemoteException;

    void setName(String name) throws RemoteException;

    String getId() throws RemoteException;

    org.openxava.qamanager.model.IQaProject getQaProject() throws RemoteException;

    void setQaProject(org.openxava.qamanager.model.IQaProject newQaProject) throws RemoteException;

    org.openxava.qamanager.model.ICustomer getCustomer() throws RemoteException;

    void setCustomer(org.openxava.qamanager.model.ICustomer newCustomer) throws RemoteException;
}
