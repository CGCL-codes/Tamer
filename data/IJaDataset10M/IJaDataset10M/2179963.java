package com.liferay.portal.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="ReleaseLocalManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public interface ReleaseLocalManagerHome extends EJBLocalHome {

    public ReleaseLocalManagerEJB create() throws CreateException;
}
