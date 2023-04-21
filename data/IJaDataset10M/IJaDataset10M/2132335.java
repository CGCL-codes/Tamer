package app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interfata extends Remote {

    double PI(int threads, int steps) throws RemoteException;
}
