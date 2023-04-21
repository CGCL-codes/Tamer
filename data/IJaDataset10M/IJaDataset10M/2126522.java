package com.microsoft.schemas.sharepoint.soap.directory;

public interface UserGroupSoap extends java.rmi.Remote {

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionFromSiteResponseGetUserCollectionFromSiteResult getUserCollectionFromSite() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionFromWebResponseGetUserCollectionFromWebResult getUserCollectionFromWeb() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetAllUserCollectionFromWebResponseGetAllUserCollectionFromWebResult getAllUserCollectionFromWeb() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionFromGroupResponseGetUserCollectionFromGroupResult getUserCollectionFromGroup(java.lang.String groupName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionFromRoleResponseGetUserCollectionFromRoleResult getUserCollectionFromRole(java.lang.String roleName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionResponseGetUserCollectionResult getUserCollection(com.microsoft.schemas.sharepoint.soap.directory.GetUserCollectionUserLoginNamesXml userLoginNamesXml) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserInfoResponseGetUserInfoResult getUserInfo(java.lang.String userLoginName) throws java.rmi.RemoteException;

    public void addUserToGroup(java.lang.String groupName, java.lang.String userName, java.lang.String userLoginName, java.lang.String userEmail, java.lang.String userNotes) throws java.rmi.RemoteException;

    public void addUserCollectionToGroup(java.lang.String groupName, com.microsoft.schemas.sharepoint.soap.directory.AddUserCollectionToGroupUsersInfoXml usersInfoXml) throws java.rmi.RemoteException;

    public void addUserToRole(java.lang.String roleName, java.lang.String userName, java.lang.String userLoginName, java.lang.String userEmail, java.lang.String userNotes) throws java.rmi.RemoteException;

    public void addUserCollectionToRole(java.lang.String roleName, com.microsoft.schemas.sharepoint.soap.directory.AddUserCollectionToRoleUsersInfoXml usersInfoXml) throws java.rmi.RemoteException;

    public void updateUserInfo(java.lang.String userLoginName, java.lang.String userName, java.lang.String userEmail, java.lang.String userNotes) throws java.rmi.RemoteException;

    public void removeUserFromSite(java.lang.String userLoginName) throws java.rmi.RemoteException;

    public void removeUserCollectionFromSite(com.microsoft.schemas.sharepoint.soap.directory.RemoveUserCollectionFromSiteUserLoginNamesXml userLoginNamesXml) throws java.rmi.RemoteException;

    public void removeUserFromWeb(java.lang.String userLoginName) throws java.rmi.RemoteException;

    public void removeUserFromGroup(java.lang.String groupName, java.lang.String userLoginName) throws java.rmi.RemoteException;

    public void removeUserCollectionFromGroup(java.lang.String groupName, com.microsoft.schemas.sharepoint.soap.directory.RemoveUserCollectionFromGroupUserLoginNamesXml userLoginNamesXml) throws java.rmi.RemoteException;

    public void removeUserFromRole(java.lang.String roleName, java.lang.String userLoginName) throws java.rmi.RemoteException;

    public void removeUserCollectionFromRole(java.lang.String roleName, com.microsoft.schemas.sharepoint.soap.directory.RemoveUserCollectionFromRoleUserLoginNamesXml userLoginNamesXml) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromSiteResponseGetGroupCollectionFromSiteResult getGroupCollectionFromSite() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromWebResponseGetGroupCollectionFromWebResult getGroupCollectionFromWeb() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromRoleResponseGetGroupCollectionFromRoleResult getGroupCollectionFromRole(java.lang.String roleName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionFromUserResponseGetGroupCollectionFromUserResult getGroupCollectionFromUser(java.lang.String userLoginName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionResponseGetGroupCollectionResult getGroupCollection(com.microsoft.schemas.sharepoint.soap.directory.GetGroupCollectionGroupNamesXml groupNamesXml) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetGroupInfoResponseGetGroupInfoResult getGroupInfo(java.lang.String groupName) throws java.rmi.RemoteException;

    public void addGroup(java.lang.String groupName, java.lang.String ownerIdentifier, java.lang.String ownerType, java.lang.String defaultUserLoginName, java.lang.String description) throws java.rmi.RemoteException;

    public void addGroupToRole(java.lang.String roleName, java.lang.String groupName) throws java.rmi.RemoteException;

    public void updateGroupInfo(java.lang.String oldGroupName, java.lang.String groupName, java.lang.String ownerIdentifier, java.lang.String ownerType, java.lang.String description) throws java.rmi.RemoteException;

    public void removeGroup(java.lang.String groupName) throws java.rmi.RemoteException;

    public void removeGroupFromRole(java.lang.String roleName, java.lang.String groupName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRoleCollectionFromWebResponseGetRoleCollectionFromWebResult getRoleCollectionFromWeb() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRoleCollectionFromGroupResponseGetRoleCollectionFromGroupResult getRoleCollectionFromGroup(java.lang.String groupName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRoleCollectionFromUserResponseGetRoleCollectionFromUserResult getRoleCollectionFromUser(java.lang.String userLoginName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRoleCollectionResponseGetRoleCollectionResult getRoleCollection(com.microsoft.schemas.sharepoint.soap.directory.GetRoleCollectionRoleNamesXml roleNamesXml) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRoleInfoResponseGetRoleInfoResult getRoleInfo(java.lang.String roleName) throws java.rmi.RemoteException;

    public void addRole(java.lang.String roleName, java.lang.String description, int permissionMask) throws java.rmi.RemoteException;

    public void addRoleDef(java.lang.String roleName, java.lang.String description, org.apache.axis.types.UnsignedLong permissionMask) throws java.rmi.RemoteException;

    public void updateRoleInfo(java.lang.String oldRoleName, java.lang.String roleName, java.lang.String description, int permissionMask) throws java.rmi.RemoteException;

    public void updateRoleDefInfo(java.lang.String oldRoleName, java.lang.String roleName, java.lang.String description, org.apache.axis.types.UnsignedLong permissionMask) throws java.rmi.RemoteException;

    public void removeRole(java.lang.String roleName) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetUserLoginFromEmailResponseGetUserLoginFromEmailResult getUserLoginFromEmail(com.microsoft.schemas.sharepoint.soap.directory.GetUserLoginFromEmailEmailXml emailXml) throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRolesAndPermissionsForCurrentUserResponseGetRolesAndPermissionsForCurrentUserResult getRolesAndPermissionsForCurrentUser() throws java.rmi.RemoteException;

    public com.microsoft.schemas.sharepoint.soap.directory.GetRolesAndPermissionsForSiteResponseGetRolesAndPermissionsForSiteResult getRolesAndPermissionsForSite() throws java.rmi.RemoteException;
}
