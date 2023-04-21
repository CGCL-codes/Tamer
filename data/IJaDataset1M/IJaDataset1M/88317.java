package com.centraview.administration.serversettings;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Called by the client to create an object of File. It requires a matching pair in
 * the bean class, i.e. ejbCreate().
 * @throws javax.ejb.CreateException
 */
public interface ServerSettingsHome extends EJBHome {

    public ServerSettings create() throws RemoteException, CreateException;
}
