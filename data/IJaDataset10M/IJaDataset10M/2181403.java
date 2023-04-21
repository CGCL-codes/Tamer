package com.liferay.portal.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="ReleaseRemoteManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public interface ReleaseRemoteManagerHome extends EJBHome {

    public ReleaseRemoteManagerEJB create() throws CreateException, RemoteException;
}
