package com.liferay.portlet.shopping.service.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <a href="ShoppingCategoryLocalServiceHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface ShoppingCategoryLocalServiceHome extends EJBLocalHome {

    public ShoppingCategoryLocalServiceEJB create() throws CreateException;
}
