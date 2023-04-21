package com.nipun.facet.neural;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FacetNetworkRunner extends Remote {

    public boolean run(double[][] image) throws RemoteException;

    public void backPropagate(boolean value) throws RemoteException;

    public int getWidth() throws RemoteException;

    public int getHeight() throws RemoteException;
}
