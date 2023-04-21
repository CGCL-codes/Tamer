package com.liferay.portal.ejb;

/**
 * <a href="UserRemoteManager.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.20 $
 *
 */
public interface UserRemoteManager {

    public boolean addGroup(java.lang.String userId, java.lang.String groupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean addGroup(java.lang.String userId, com.liferay.portal.model.Group group) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean addRole(java.lang.String userId, java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean addRole(java.lang.String userId, com.liferay.portal.model.Role role) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User addUser(java.lang.String companyId, boolean autoUserId, java.lang.String userId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean passwordReset, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String nickName, boolean male, java.util.Date birthday, java.lang.String emailAddress, java.util.Locale locale) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean deleteGroup(java.lang.String userId, java.lang.String groupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean deleteGroup(java.lang.String userId, com.liferay.portal.model.Group group) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean deleteRole(java.lang.String userId, java.lang.String roleId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean deleteRole(java.lang.String userId, com.liferay.portal.model.Role role) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void deleteUser(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List findByC_SMS(java.lang.String companyId) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User getDefaultUser(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getGroups(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getGroups(java.lang.String userId, int begin, int end) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public int getGroupsSize(java.lang.String userId) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getProjProjects(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getProjTasks(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getRoles(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getRoles(java.lang.String userId, int begin, int end) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public int getRolesSize(java.lang.String userId) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User getUserByEmailAddress(java.lang.String companyId, java.lang.String emailAddress) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User getUserById(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User getUserById(java.lang.String companyId, java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean hasGroupById(java.lang.String userId, java.lang.String groupId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean hasGroupByName(java.lang.String companyId, java.lang.String userId, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public boolean hasRedoUpdateSkin(java.lang.String userId) throws java.rmi.RemoteException;

    public boolean hasUndoUpdateSkin(java.lang.String userId) throws java.rmi.RemoteException;

    public com.liferay.portal.model.User redoUpdateSkin(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setGroups(java.lang.String userId, java.lang.String[] groupIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setGroups(java.lang.String userId, java.util.List groups) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setLayouts(java.lang.String userId, java.lang.String[] layoutIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setProjProjects(java.lang.String userId, java.lang.String[] projProjectIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setProjProjects(java.lang.String userId, java.util.List projProjects) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setProjTasks(java.lang.String userId, java.lang.String[] projTaskIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setProjTasks(java.lang.String userId, java.util.List projTasks) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setRoles(java.lang.String userId, java.lang.String[] roleIds) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void setRoles(java.lang.String userId, java.util.List roles) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User undoUpdateSkin(java.lang.String userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User updateActive(java.lang.String userId, boolean active) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User updateSkin(java.lang.String userId, java.lang.String skinId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User updateUser(java.lang.String userId, java.lang.String password1, java.lang.String password2, boolean passwordReset) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.User updateUser(java.lang.String userId, java.lang.String password, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, java.lang.String nickName, boolean male, java.util.Date birthday, java.lang.String emailAddress, java.lang.String smsId, java.lang.String aimId, java.lang.String icqId, java.lang.String msnId, java.lang.String ymId, java.lang.String favoriteActivity, java.lang.String favoriteBibleVerse, java.lang.String favoriteFood, java.lang.String favoriteMovie, java.lang.String favoriteMusic, java.lang.String languageId, java.lang.String timeZoneId, java.lang.String skinId, boolean dottedSkins, boolean roundedSkins, java.lang.String greeting, java.lang.String resolution, java.lang.String refreshRate, java.lang.String comments) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void validate(java.lang.String companyId, boolean autoUserId, java.lang.String userId, boolean autoPassword, java.lang.String password1, java.lang.String password2, java.lang.String firstName, java.lang.String lastName, java.lang.String emailAddress) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void validate(java.lang.String userId, java.lang.String password1, java.lang.String password2) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void validate(java.lang.String userId, java.lang.String firstName, java.lang.String lastName, java.lang.String emailAddress, java.lang.String smsId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;
}
