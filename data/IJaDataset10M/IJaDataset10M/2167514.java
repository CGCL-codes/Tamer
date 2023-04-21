package org.openxava.qamanager.model;

import java.math.*;
import java.rmi.RemoteException;

public interface IResourceAllocation1 extends org.openxava.model.IModel {

    java.util.Date getScheduledStart() throws RemoteException;

    void setScheduledStart(java.util.Date scheduledStart) throws RemoteException;

    java.util.Date getScheduledEnd() throws RemoteException;

    void setScheduledEnd(java.util.Date scheduledEnd) throws RemoteException;

    String getResourceComment() throws RemoteException;

    void setResourceComment(String resourceComment) throws RemoteException;

    java.util.Date getActualEnd() throws RemoteException;

    void setActualEnd(java.util.Date actualEnd) throws RemoteException;

    String getReasonDelay() throws RemoteException;

    void setReasonDelay(String reasonDelay) throws RemoteException;

    java.util.Date getActualStart() throws RemoteException;

    void setActualStart(java.util.Date actualStart) throws RemoteException;

    org.openxava.qamanager.model.IRoleDefinition getRole() throws RemoteException;

    void setRole(org.openxava.qamanager.model.IRoleDefinition newRole) throws RemoteException;

    org.openxava.qamanager.model.ITestCycle getTestCycle() throws RemoteException;

    void setTestCycle(org.openxava.qamanager.model.ITestCycle newTestCycle) throws RemoteException;

    org.openxava.qamanager.model.IResourceStatus getStatus() throws RemoteException;

    void setStatus(org.openxava.qamanager.model.IResourceStatus newStatus) throws RemoteException;

    org.openxava.qamanager.model.IResource getResource() throws RemoteException;

    void setResource(org.openxava.qamanager.model.IResource newResource) throws RemoteException;
}
