package com.centraview.chart;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * This interface is the remote home interface
 * for the Chart EJB. It provides the
 * method for creating an instance of the 
 * remote interface.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public interface ChartHome extends EJBHome {

    /**
   * This method returns a instance of the remote interface
   * of the Chart EJB.
   * 
   * @return An instance of the ChartEJB.
   * 
   * @throws CreateException The instance could not be
   * created.
   * @throws RemoteException There was an error in
   * talking to the Remote Interface.
   */
    public Chart create() throws CreateException, RemoteException;
}
