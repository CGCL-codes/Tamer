package edu.clemson.cs.nestbed.common.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObservable extends Remote {

    public void addRemoteObserver(RemoteObserver o) throws RemoteException;

    public void deleteRemoteObserver(RemoteObserver o) throws RemoteException;
}
