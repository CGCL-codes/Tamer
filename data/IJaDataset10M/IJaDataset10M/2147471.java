package com.liferay.portlet.imagegallery.service.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="IGFolderServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface IGFolderServiceHome extends EJBHome {

    public IGFolderServiceEJB create() throws CreateException, RemoteException;
}
