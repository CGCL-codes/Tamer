package com.liferay.portlet.blogs.service.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="BlogsCategoryServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface BlogsCategoryServiceHome extends EJBHome {

    public BlogsCategoryServiceEJB create() throws CreateException, RemoteException;
}
