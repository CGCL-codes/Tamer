package java.rmi.server;

import gnu.java.rmi.server.UnicastServerRef;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class UnicastRemoteObject extends RemoteServer {

    private static final long serialVersionUID = 4974527148936298033L;

    private int port = 0;

    private RMIClientSocketFactory csf = null;

    private RMIServerSocketFactory ssf = null;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int port) throws RemoteException {
        this(port, RMISocketFactory.getSocketFactory(), RMISocketFactory.getSocketFactory());
    }

    protected UnicastRemoteObject(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        this.port = port;
        this.ref = new UnicastServerRef(new ObjID(), port, ssf);
        exportObject(this);
    }

    protected UnicastRemoteObject(RemoteRef ref) throws RemoteException {
        super((UnicastServerRef) ref);
        exportObject(this);
    }

    public Object clone() throws CloneNotSupportedException {
        throw new Error("Not implemented");
    }

    public static RemoteStub exportObject(Remote obj) throws RemoteException {
        UnicastServerRef sref = (UnicastServerRef) ((RemoteObject) obj).getRef();
        return (sref.exportObject(obj));
    }

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj, port, null);
    }

    static Remote exportObject(Remote obj, int port, RMIServerSocketFactory ssf) throws RemoteException {
        UnicastServerRef sref = null;
        if (obj instanceof RemoteObject) sref = (UnicastServerRef) ((RemoteObject) obj).getRef();
        if (sref == null) {
            sref = new UnicastServerRef(new ObjID(), port, ssf);
        }
        Remote stub = sref.exportObject(obj);
        addStub(obj, stub);
        return stub;
    }

    /**
   * FIXME
   */
    public static Remote exportObject(Remote obj, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        return (exportObject(obj, port, ssf));
    }

    public static boolean unexportObject(Remote obj, boolean force) throws NoSuchObjectException {
        if (obj instanceof RemoteObject) {
            deleteStub(obj);
            UnicastServerRef sref = (UnicastServerRef) ((RemoteObject) obj).getRef();
            return sref.unexportObject(obj, force);
        } else {
            ;
        }
        return true;
    }
}
