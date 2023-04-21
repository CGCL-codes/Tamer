package infrastructureAPI.backendGate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import networkRmi.CommunicationServerRemoteAPI;
import messages.Message;

/**
 * Description: 
 * The GeneralRequestMessageDeliver defines and object 
 * able to deliver a Message through a remote connection
 * to the sender of the message a the specified service.
 * The GeneralRequestMessageDeliver's going to 
 * looking for the service which
 * need to be registered into rmiregistry.
 * If none rmiregistry is listening for replies then a 
 * RemoteException will be thrown.
 *
 */
public class GeneralRequestMessageDeliver extends UnicastRemoteObject implements RequestMessageDeliver {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    InetAddress myHost;

    /**
	 * 
	 */
    public GeneralRequestMessageDeliver() throws RemoteException {
        try {
            myHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            myHost = null;
        }
    }

    @Override
    public boolean deliver(Message msg, String service) throws RemoteException, NotBoundException, UnknownHostException {
        boolean ret = false;
        InetAddress host = InetAddress.getLocalHost();
        Registry registry = LocateRegistry.getRegistry(host.getHostAddress());
        CommunicationServerRemoteAPI hostServer = (CommunicationServerRemoteAPI) registry.lookup(service);
        ret = hostServer.receiveMessage(msg);
        return ret;
    }
}
