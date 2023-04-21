package com.liferay.portal.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="ImageManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.10 $
 *
 */
public interface ImageManagerHome extends EJBHome {

    public ImageManagerEJB create() throws CreateException, RemoteException;
}
