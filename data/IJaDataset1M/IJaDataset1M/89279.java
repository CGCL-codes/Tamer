package ch.mypics.wtt.ejb.services;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * 
 * @author <a href="mailto:mhaessig@okta.ch">Mathias H&auml;ssig</a>
 */
public interface DataServiceHome extends EJBHome {

    public DataService create() throws CreateException, RemoteException;
}
