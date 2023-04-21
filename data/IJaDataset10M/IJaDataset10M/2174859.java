package com.liferay.portal.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="CompanyLocalManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.10 $
 *
 */
public interface CompanyLocalManagerHome extends EJBLocalHome {

    public CompanyLocalManagerEJB create() throws CreateException;
}
