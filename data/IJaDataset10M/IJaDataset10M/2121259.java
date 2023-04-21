package com.liferay.portal.service.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="LayoutLocalServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface LayoutLocalServiceHome extends EJBLocalHome {

    public LayoutLocalServiceEJB create() throws CreateException;
}
