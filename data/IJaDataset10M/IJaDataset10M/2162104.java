package MyRemoteSorter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import MyRemoteSorter.*;

public interface ISortFactory extends Remote {

    public ISorter createSorter() throws RemoteException;
}
