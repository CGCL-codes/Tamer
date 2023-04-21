package com.liferay.portlet.shopping.service.ejb;

import com.liferay.portal.service.impl.PrincipalSessionBean;
import com.liferay.portal.spring.util.SpringUtil;
import com.liferay.portlet.shopping.service.spring.ShoppingCategoryService;
import org.springframework.context.ApplicationContext;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="ShoppingCategoryServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingCategoryServiceEJBImpl implements ShoppingCategoryService, SessionBean {

    public static final String CLASS_NAME = ShoppingCategoryService.class.getName() + ".transaction";

    public static ShoppingCategoryService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        return (ShoppingCategoryService) ctx.getBean(CLASS_NAME);
    }

    public com.liferay.portlet.shopping.model.ShoppingCategory addCategory(java.lang.String plid, java.lang.String parentCategoryId, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().addCategory(plid, parentCategoryId, name, description, addCommunityPermissions, addGuestPermissions);
    }

    public void deleteCategory(java.lang.String categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        getService().deleteCategory(categoryId);
    }

    public com.liferay.portlet.shopping.model.ShoppingCategory getCategory(java.lang.String categoryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().getCategory(categoryId);
    }

    public com.liferay.portlet.shopping.model.ShoppingCategory updateCategory(java.lang.String categoryId, java.lang.String parentCategoryId, java.lang.String name, java.lang.String description) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().updateCategory(categoryId, parentCategoryId, name, description);
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public SessionContext getSessionContext() {
        return _sc;
    }

    public void setSessionContext(SessionContext sc) {
        _sc = sc;
    }

    private SessionContext _sc;
}
