package visad.cluster;

import java.rmi.*;
import java.io.Serializable;

/**
   RemoteClientAgent is the interface for agents on the client,
   which typically send NodeAgents to each node.<P>
*/
public interface RemoteClientAgent extends Remote {

    void sendToClient(Serializable message) throws RemoteException;
}
