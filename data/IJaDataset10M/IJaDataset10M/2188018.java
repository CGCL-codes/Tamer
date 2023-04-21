package rmiBankStandAlone;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    public void updateRekening(Account Rek) throws RemoteException;
}
