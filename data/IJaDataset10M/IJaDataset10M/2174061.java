package com.liferay.portal.service.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="ListTypeServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface ListTypeServiceHome extends EJBHome {

    public ListTypeServiceEJB create() throws CreateException, RemoteException;
}
