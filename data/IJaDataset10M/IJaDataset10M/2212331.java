package com.liferay.portlet.messageboards.service.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="MBMessageServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface MBMessageServiceHome extends EJBHome {

    public MBMessageServiceEJB create() throws CreateException, RemoteException;
}
