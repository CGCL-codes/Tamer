package visad.cluster;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

/**
   RemoteAgentContactImpl is the class on nodes for
   RemoteClientAgent to communicate to NodeAgent.<P> 
*/
public class RemoteAgentContactImpl extends UnicastRemoteObject implements RemoteAgentContact {

    NodeAgent agent;

    public RemoteAgentContactImpl(NodeAgent ag) throws RemoteException {
        agent = ag;
    }

    public void sendToNode(Serializable message) throws RemoteException {
        agent.sendToNode(message);
    }
}
