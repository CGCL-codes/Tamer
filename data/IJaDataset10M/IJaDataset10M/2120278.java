package plasmid.topology;

import plasmid.IRemote;
import plasmid.RemoteObj;
import plasmid.SessionRunnable;
import java.rmi.RemoteException;

/**
 * This is the remote-server class for Topology information.
 * It currently provides only the RemoteObj.perform method, which allows
 * an arbitrary operation to be performed on the server's topology instance,
 * returning arbitrary (serializable) results.
 * @See plasmid.RemoteObj
 * @See plasmid.IRemote
 **/
public class RTopology extends RemoteObj implements IRTopology {

    /** Reference to the central topology information. **/
    protected Topology topology;

    public RTopology(Topology t) throws RemoteException {
        super((SessionRunnable) t);
        topology = t;
    }

    public String getServiceName() {
        return IRTopology.serviceName;
    }
}
