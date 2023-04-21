package com.liferay.portal.service.ejb;

import com.liferay.portal.service.impl.PrincipalSessionBean;
import com.liferay.portal.service.spring.UserGroupService;
import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="UserGroupServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class UserGroupServiceEJBImpl implements UserGroupService, SessionBean {

    public static final String CLASS_NAME = UserGroupService.class.getName() + ".transaction";

    public static UserGroupService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        return (UserGroupService) ctx.getBean(CLASS_NAME);
    }

    public void addGroupUserGroups(java.lang.String groupId, java.lang.String[] userGroupIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        getService().addGroupUserGroups(groupId, userGroupIds);
    }

    public com.liferay.portal.model.UserGroup addUserGroup(java.lang.String name, java.lang.String description) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().addUserGroup(name, description);
    }

    public void deleteUserGroup(java.lang.String userGroupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        getService().deleteUserGroup(userGroupId);
    }

    public com.liferay.portal.model.UserGroup getUserGroup(java.lang.String userGroupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().getUserGroup(userGroupId);
    }

    public java.util.List getUserUserGroups(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().getUserUserGroups(userId);
    }

    public void unsetGroupUserGroups(java.lang.String groupId, java.lang.String[] userGroupIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        getService().unsetGroupUserGroups(groupId, userGroupIds);
    }

    public com.liferay.portal.model.UserGroup updateUserGroup(java.lang.String userGroupId, java.lang.String name, java.lang.String description) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return getService().updateUserGroup(userGroupId, name, description);
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
