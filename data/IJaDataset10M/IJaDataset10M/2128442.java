package com.liferay.portlet.shopping.service.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="ShoppingOrderItemLocalServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface ShoppingOrderItemLocalServiceHome extends EJBLocalHome {

    public ShoppingOrderItemLocalServiceEJB create() throws CreateException;
}
