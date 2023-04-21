package geovista.collaboration;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteSelectionListener extends Remote {

    public void selectionChanged(String source, int[] selection) throws RemoteException;
}
