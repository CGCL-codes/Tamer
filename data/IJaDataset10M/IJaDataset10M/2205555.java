package client.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Title:        ClusterServer
 * Description:  A clustered server to replicate state across separate JVMs.
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public interface RMISubscriber extends Remote {

    public void notify(Object data) throws RemoteException;
}
