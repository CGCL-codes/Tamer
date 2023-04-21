package com.liferay.portal.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="PortletPreferencesLocalManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public interface PortletPreferencesLocalManagerHome extends EJBLocalHome {

    public PortletPreferencesLocalManagerEJB create() throws CreateException;
}
