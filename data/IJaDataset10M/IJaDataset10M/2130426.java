package com.liferay.portal.ejb;

import java.rmi.RemoteException;

/**
 * <a href="GroupManagerSoap.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class GroupManagerSoap {

    public static com.liferay.portal.model.GroupModel addGroup(java.lang.String name) throws RemoteException {
        try {
            com.liferay.portal.model.Group returnValue = GroupManagerUtil.addGroup(name);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean addRoles(java.lang.String groupId, java.util.List roles) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.addRoles(groupId, roles);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean addUsers(java.lang.String groupId, java.util.List users) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.addUsers(groupId, users);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static void deleteGroup(java.lang.String groupId) throws RemoteException {
        try {
            GroupManagerUtil.deleteGroup(groupId);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean exists(java.lang.String name) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.exists(name);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel getGroupById(java.lang.String groupId) throws RemoteException {
        try {
            com.liferay.portal.model.Group returnValue = GroupManagerUtil.getGroupById(groupId);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel getGroupByName(java.lang.String name) throws RemoteException {
        try {
            com.liferay.portal.model.Group returnValue = GroupManagerUtil.getGroupByName(name);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel[] getLayouts(java.lang.String groupId) throws RemoteException {
        try {
            java.util.List returnValue = GroupManagerUtil.getLayouts(groupId);
            return (com.liferay.portal.model.Group[]) returnValue.toArray(new com.liferay.portal.model.Group[0]);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel[] getRoles(java.lang.String groupId) throws RemoteException {
        try {
            java.util.List returnValue = GroupManagerUtil.getRoles(groupId);
            return (com.liferay.portal.model.Group[]) returnValue.toArray(new com.liferay.portal.model.Group[0]);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel[] getRoles(java.lang.String groupId, int begin, int end) throws RemoteException {
        try {
            java.util.List returnValue = GroupManagerUtil.getRoles(groupId, begin, end);
            return (com.liferay.portal.model.Group[]) returnValue.toArray(new com.liferay.portal.model.Group[0]);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static int getRolesSize(java.lang.String groupId) throws RemoteException {
        try {
            int returnValue = GroupManagerUtil.getRolesSize(groupId);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel[] getUsers(java.lang.String groupId) throws RemoteException {
        try {
            java.util.List returnValue = GroupManagerUtil.getUsers(groupId);
            return (com.liferay.portal.model.Group[]) returnValue.toArray(new com.liferay.portal.model.Group[0]);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel[] getUsers(java.lang.String groupId, int begin, int end) throws RemoteException {
        try {
            java.util.List returnValue = GroupManagerUtil.getUsers(groupId, begin, end);
            return (com.liferay.portal.model.Group[]) returnValue.toArray(new com.liferay.portal.model.Group[0]);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static int getUsersSize(java.lang.String groupId) throws RemoteException {
        try {
            int returnValue = GroupManagerUtil.getUsersSize(groupId);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean removeRoles(java.lang.String groupId, java.lang.String[] roleIds) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.removeRoles(groupId, roleIds);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean removeUsers(java.lang.String groupId, java.lang.String[] userIds) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.removeUsers(groupId, userIds);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel renameGroupById(java.lang.String groupId, java.lang.String newName) throws RemoteException {
        try {
            com.liferay.portal.model.Group returnValue = GroupManagerUtil.renameGroupById(groupId, newName);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.GroupModel renameGroupByName(java.lang.String oldName, java.lang.String newName) throws RemoteException {
        try {
            com.liferay.portal.model.Group returnValue = GroupManagerUtil.renameGroupByName(oldName, newName);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setLayouts(java.lang.String groupId, java.lang.String[] layoutIds) throws RemoteException {
        try {
            GroupManagerUtil.setLayouts(groupId, layoutIds);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setRoles(java.lang.String groupId, java.lang.String[] roleIds) throws RemoteException {
        try {
            GroupManagerUtil.setRoles(groupId, roleIds);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static void setUsers(java.lang.String groupId, java.lang.String[] userIds) throws RemoteException {
        try {
            GroupManagerUtil.setUsers(groupId, userIds);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static boolean hasAdmin(java.lang.String groupId) throws RemoteException {
        try {
            boolean returnValue = GroupManagerUtil.hasAdmin(groupId);
            return returnValue;
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
