package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteServer;

public abstract class Activatable extends RemoteServer {

    static final long serialVersionUID = -3120617863591563455L;

    protected Activatable(String location, MarshalledObject data, boolean restart, int port) throws ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    protected Activatable(String location, MarshalledObject data, boolean restart, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    protected Activatable(ActivationID id, int port) throws RemoteException {
        throw new Error("Not implemented");
    }

    protected Activatable(ActivationID id, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        throw new Error("Not implemented");
    }

    protected ActivationID getID() {
        throw new Error("Not implemented");
    }

    public static Remote register(ActivationDesc desc) throws UnknownGroupException, ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    public static boolean inactive(ActivationID id) throws UnknownObjectException, ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    public static void unregister(ActivationID id) throws UnknownObjectException, ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    public static ActivationID exportObject(Remote obj, String location, MarshalledObject data, boolean restart, int port) throws ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    public static ActivationID exportObject(Remote obj, String location, MarshalledObject data, boolean restart, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws ActivationException, RemoteException {
        throw new Error("Not implemented");
    }

    public static Remote exportObject(Remote obj, ActivationID id, int port) throws RemoteException {
        throw new Error("Not implemented");
    }

    public static Remote exportObject(Remote obj, ActivationID id, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        throw new Error("Not implemented");
    }

    public static boolean unexportObject(Remote obj, boolean force) throws NoSuchObjectException {
        throw new Error("Not implemented");
    }
}
