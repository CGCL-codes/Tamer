package alice.cartago;

import java.util.*;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import alice.cartago.security.*;

/**
 * Class representing a CArtAgO infrastructure node.
 *  
 * @author aricci
 *
 */
public class CartagoNode implements ICartagoNode {

    public static final int DEFAULT_PORT = 20100;

    static final String CArtAgO_NODE_BIND_NAME = "__cartago_node__";

    private String localAddress;

    private HashMap<String, CartagoWorkspace> wsps;

    private int port = DEFAULT_PORT;

    private long wspCounter = 0;

    private boolean standalone;

    CartagoNode(boolean standalone) throws CartagoException {
        wsps = new HashMap<String, CartagoWorkspace>();
        this.standalone = standalone;
        if (!standalone) {
            try {
                localAddress = InetAddress.getLocalHost().getHostAddress() + ":" + port;
                LocateRegistry.createRegistry(port);
            } catch (Exception ex) {
                System.err.println("[CArtAgO] RMI layer already installed.");
            }
            try {
                ICartagoRemoteNode rnode = new CartagoRemoteNode(this);
                Naming.bind("rmi://" + localAddress + "/" + CArtAgO_NODE_BIND_NAME, rnode);
                System.out.println("[CArtAgO] Node successfully created - Address: " + localAddress);
            } catch (Exception ex) {
                System.err.println("[CArtAgO] Impossible to install the node on " + localAddress);
                throw new CartagoException("Node installation failed");
            }
        }
        createWorkspace("default");
    }

    CartagoNode(int port) throws CartagoException {
        wsps = new HashMap<String, CartagoWorkspace>();
        standalone = false;
        this.port = port;
        try {
            localAddress = InetAddress.getLocalHost().getHostAddress() + ":" + port;
            LocateRegistry.createRegistry(port);
        } catch (Exception ex) {
            System.err.println("[CArtAgO] RMI layer already installed.");
        }
        try {
            ICartagoRemoteNode rnode = new CartagoRemoteNode(this);
            Naming.bind("rmi://" + localAddress + "/" + CArtAgO_NODE_BIND_NAME, rnode);
            System.out.println("[CArtAgO] Node successfully created - Address: " + localAddress);
        } catch (Exception ex) {
            System.err.println("[CArtAgO] Impossible to install the node on " + localAddress);
            throw new CartagoException("Node installation failed");
        }
        createWorkspace("default");
    }

    public synchronized ICartagoWorkspace createWorkspace(String name) {
        CartagoWorkspace env = wsps.get(name);
        if (env == null) {
            WorkspaceId id = new WorkspaceId(name, wspCounter, localAddress);
            env = new CartagoWorkspace(id);
            wsps.put(name, env);
            if (!standalone) {
                try {
                    CartagoRemoteWorkspace wsp = new CartagoRemoteWorkspace(env);
                    Naming.bind("rmi://" + localAddress + "/" + name, wsp);
                    System.out.println("[CArtAgO] Workspace successfully created: " + name + "@" + localAddress);
                } catch (Exception ex) {
                    System.err.println("[CArtAgO] Failure: " + ex);
                }
            }
        }
        return env;
    }

    public synchronized ICartagoWorkspace getWorkspace(String wspName) throws CartagoException {
        CartagoWorkspace env = wsps.get(wspName);
        if (env == null) {
            throw new CartagoException("Workspace not found.");
        }
        return env;
    }

    /**
	 * Infrastructure service for dispatching inter-node artifact signals
	 * 
	 */
    void dispatchSignal(OpId opSourceId, ArtifactId sourceId, UserId bodyId, ArtifactId targetId, Op op, long timeout) throws InterruptedException, OpRequestTimeoutException, ArtifactNoMoreAvailableException, OperationUnavailableException, CartagoException {
        WorkspaceId targetWsp = targetId.getWorkspaceId();
        if (targetWsp.getAddress() == null || targetWsp.getAddress().equals(localAddress)) {
            CartagoWorkspace env = wsps.get(targetWsp.getName());
            if (env == null) {
                throw new ArtifactNoMoreAvailableException();
            } else {
                env.dispatchSignal(opSourceId, sourceId, bodyId, targetId, op, timeout);
            }
        } else {
            try {
                ICartagoRemoteWorkspace env = (ICartagoRemoteWorkspace) Naming.lookup("rmi://" + targetWsp.getAddress() + "/" + targetWsp.getName());
                env.dispatchSignal(opSourceId, sourceId, bodyId, targetId, op, timeout);
            } catch (java.net.MalformedURLException ex) {
                throw new CartagoException("Workspace not reachable..");
            } catch (RemoteException ex) {
                throw new CartagoException("Workspace not reachable..");
            } catch (NotBoundException ex) {
                throw new CartagoException("Workspace not reachable..");
            }
        }
    }

    /**
	 * Infrastructure service for dispatching inter-artifact RPC
	 * 
	 */
    Object execInterArtifactRPC(OpId opSourceId, ArtifactId sourceId, UserId bodyId, ArtifactId targetId, Op op, long timeout) throws InterruptedException, OpRequestTimeoutException, ArtifactNoMoreAvailableException, OperationUnavailableException, CartagoException {
        WorkspaceId targetWsp = targetId.getWorkspaceId();
        if (targetWsp.getAddress() == null || targetWsp.getAddress().equals(localAddress)) {
            CartagoWorkspace env = wsps.get(targetWsp.getName());
            if (env == null) {
                throw new ArtifactNoMoreAvailableException();
            } else {
                return env.execInterArtifactRPC(opSourceId, sourceId, bodyId, targetId, op, timeout);
            }
        } else {
            try {
                ICartagoRemoteWorkspace env = (ICartagoRemoteWorkspace) Naming.lookup("rmi://" + targetWsp.getAddress() + "/" + targetWsp.getName());
                return env.execInterArtifactRPC(opSourceId, sourceId, bodyId, targetId, op, timeout);
            } catch (java.net.MalformedURLException ex) {
                throw new CartagoException("Workspace not reachable..");
            } catch (RemoteException ex) {
                throw new CartagoException("Workspace not reachable..");
            } catch (NotBoundException ex) {
                throw new CartagoException("Workspace not reachable..");
            }
        }
    }

    public String getVersion() throws CartagoException {
        return CArtAgO_VERSION.getID();
    }
}
