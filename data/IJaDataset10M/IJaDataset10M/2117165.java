package ch.mypics.wtt.ejb.services;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import javax.ejb.EJBObject;
import ch.mypics.wtt.beans.Bean;

/**
 * 
 * @author <a href="mailto:mhaessig@okta.ch">Mathias H&auml;ssig</a>
 */
public interface DataService extends EJBObject {

    public Collection getBeans(String service, String dataSource) throws RemoteException;

    public Collection getBeans(String service, String dataSource, Map search) throws RemoteException;

    public void save(Bean bean) throws RemoteException;

    public void create(Bean bean) throws RemoteException;

    public void delete(Bean bean) throws RemoteException;
}
