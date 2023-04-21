package de.molimo.session;

import java.rmi.RemoteException;

public interface IBrowserTree {

    IBrowserAttribute[] getChildren() throws RemoteException;

    void moreAttributes() throws RemoteException;

    void lessAttributes() throws RemoteException;

    boolean isLessable() throws RemoteException;

    boolean isMoreable() throws RemoteException;

    boolean hasBrothers() throws RemoteException;

    boolean areChildrenVisible() throws RemoteException;

    boolean hasChildren() throws RemoteException;

    void toggleChildren() throws RemoteException;
}
