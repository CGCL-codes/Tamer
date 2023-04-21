package org.gtdfree;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GTDFreeOperations extends Remote {

    public boolean isRunning() throws RemoteException;

    public void shutdown() throws RemoteException;

    public void pushAppVisible() throws RemoteException;

    public String getDataLocation() throws RemoteException;
}
