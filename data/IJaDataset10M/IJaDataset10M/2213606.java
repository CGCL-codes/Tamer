package org.redwood.business.usermanagement.websitegroup;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * The EJBHome interface.
 * This interface is for extending the EJBHome.
 *
 * @author  
 * @version 1.0
 */
public interface WebSiteGroupHome extends EJBHome {

    public static final String COMP_NAME = "WebSiteGroup";

    public static final String JNDI_NAME = "WebSiteGroup";

    /**
   * Creates the WebSiteGroup.
   *
   * @return    the EJBObject.
   * @exception RemoteException, CreateException.
   */
    public WebSiteGroupObject create(String id) throws RemoteException, CreateException;

    /**
   * Creates the WebSiteGroup.
   *
   * @return    the EJBObject.
   * @exception RemoteException, CreateException.
   */
    public WebSiteGroupObject create(String id, String name, String personID) throws RemoteException, CreateException;

    /**
   * Finds the WebSiteGroup by its primary key.
   *
   * @return    the EJBObject.
   * @exception RemoteException, FinderException.
   */
    public WebSiteGroupObject findByPrimaryKey(WebSiteGroupPK pk) throws RemoteException, FinderException;

    /**
   * Finds all the WebSiteGroup of a specified Person.
   *
   * @param personID The specified PersonID
   * @return    the EJBObject.
   * @exception RemoteException, FinderException.
   */
    public Collection findByRw_personID(String personID) throws RemoteException, FinderException;

    /**
   * Finds all WebSiteGroups
   */
    public Collection findAll() throws RemoteException, FinderException;
}
