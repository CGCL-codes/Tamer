package edu.mit.wi.omnigene.dbsieve.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DBDimensionFilterHome extends EJBHome {

    public DBDimensionFilter create() throws RemoteException, CreateException;
}
