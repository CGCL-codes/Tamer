package fate.server;

/**
 *
 * @author  Albion Zeglin
 */
public class FateServerEngine implements FateServer {

    /** Creates a new instance of FateServerEngine */
    public FateServerEngine() throws java.rmi.RemoteException {
    }

    public String getGameName() throws java.rmi.RemoteException {
        return "test";
    }

    public int getGameNumber() throws java.rmi.RemoteException {
        return 1;
    }

    public Object queneMessage(Object m) throws java.rmi.RemoteException {
        return this;
    }
}
