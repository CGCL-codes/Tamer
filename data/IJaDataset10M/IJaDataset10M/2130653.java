package TransmitterS;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Intelligence extends Remote, Serializable {

    long getTime() throws RemoteException;
}
