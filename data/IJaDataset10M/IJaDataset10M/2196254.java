package com.liferay.portlet.softwarecatalog.service.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="SCLicenseServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface SCLicenseServiceHome extends EJBHome {

    public SCLicenseServiceEJB create() throws CreateException, RemoteException;
}
