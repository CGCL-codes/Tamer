package com.luzan.common.pool;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIPoolInterface extends Remote {

    final String bindName = "RMIPool";

    public void unBind() throws RemoteException, NotBoundException;

    public void registerListener(RMIListenerInterface listener, String pdsName) throws RemoteException;

    public void unregisterListener(String clientId, String pdsName) throws RemoteException;
}
