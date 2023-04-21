package org.openxava.qamanager.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IEventHistory extends org.openxava.model.IModel {

    java.util.Date getModified() throws RemoteException;

    void setModified(java.util.Date modified) throws RemoteException;

    java.util.Date getCreated() throws RemoteException;

    void setCreated(java.util.Date created) throws RemoteException;

    String getEventID() throws RemoteException;

    String getTitle() throws RemoteException;

    void setTitle(String title) throws RemoteException;

    String getRemarks() throws RemoteException;

    void setRemarks(String remarks) throws RemoteException;

    java.util.Date getEventDate() throws RemoteException;

    org.openxava.qamanager.model.ITestCycle getTestCycle() throws RemoteException;

    void setTestCycle(org.openxava.qamanager.model.ITestCycle newTestCycle) throws RemoteException;
}
