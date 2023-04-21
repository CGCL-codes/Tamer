package java.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRef extends RemoteRef {

    long serialVersionUID = -4557750989390278438L;

    RemoteStub exportObject(Remote obj, Object data) throws RemoteException;

    String getClientHost() throws ServerNotActiveException;
}
