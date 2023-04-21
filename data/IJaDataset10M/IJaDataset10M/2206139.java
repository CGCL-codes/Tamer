package com.liferay.portal.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="PortletPreferencesManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public interface PortletPreferencesManagerHome extends EJBHome {

    public PortletPreferencesManagerEJB create() throws CreateException, RemoteException;
}
