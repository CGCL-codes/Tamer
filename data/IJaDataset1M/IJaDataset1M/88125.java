package com.dotmarketing.cms.factories;

import java.util.ArrayList;
import java.util.List;
import com.dotmarketing.util.Logger;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.ejb.GroupLocalManagerUtil;
import com.liferay.portal.ejb.UserLocalManagerUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;

/**
 *
 * @author David
 */
public class PublicGroupFactory {

    @SuppressWarnings("unchecked")
    public static List<Group> getAllGroupsForUser(String userId) {
        List<Group> myGroups = new ArrayList<Group>();
        try {
            myGroups = UserLocalManagerUtil.getGroups(userId);
        } catch (Exception e) {
        }
        return myGroups;
    }

    @SuppressWarnings("unchecked")
    public static List<User> getAllUsersInGroup(String groupId) {
        List users = new ArrayList();
        try {
            users = GroupLocalManagerUtil.getUsers(groupId);
        } catch (Exception e) {
            Logger.error(PublicGroupFactory.class, "getAllUsersInGroup: Errors getting users for groupId: " + groupId);
        }
        return users;
    }

    public static Group getGroupByName(String groupName) {
        Group group = new Group();
        try {
            group = GroupLocalManagerUtil.getGroupByName(com.dotmarketing.cms.factories.PublicCompanyFactory.getDefaultCompany().getCompanyId(), groupName);
        } catch (Exception e) {
            Logger.warn(PublicGroupFactory.class, "getGroupByName: Errors getting group: " + groupName, e);
        }
        return group;
    }

    public static boolean existGroup(String groupName) {
        try {
            String companyID = PublicCompanyFactory.getDefaultCompanyId();
            return GroupLocalManagerUtil.exists(companyID, groupName);
        } catch (Exception e) {
            return false;
        }
    }

    public static Group addGroup(String name) {
        Group group = new Group();
        try {
            group = GroupLocalManagerUtil.addGroup(PublicCompanyFactory.getDefaultCompanyId(), name);
        } catch (Exception e) {
            Logger.warn(PublicGroupFactory.class, "addGroup: Errors creating the group: " + name, e);
        }
        return group;
    }

    public static void addUserToGroup(Group group, User user) {
        addUserToGroup(group.getGroupId(), user.getUserId());
    }

    public static void addUserToGroup(String groupId, String userId) {
        try {
            GroupLocalManagerUtil.addUser(groupId, userId);
        } catch (Exception e) {
            Logger.warn(PublicGroupFactory.class, "addUserToGroup: Errors associating the user: " + userId + " to the group: " + groupId, e);
        }
    }

    public static void removeUserFromGroup(Group group, User user) {
        removeUserFromGroup(group.getGroupId(), user.getUserId());
    }

    public static void removeUserFromGroup(String groupId, String userId) {
        try {
            GroupLocalManagerUtil.deleteUser(groupId, userId);
        } catch (Exception e) {
            Logger.warn(PublicGroupFactory.class, "addUserToGroup: Errors removing the user: " + userId + " from the group: " + groupId, e);
        }
    }

    public static Group saveGroup(Group g) {
        try {
            g = GroupLocalManagerUtil.addGroup(g.getCompanyId(), g.getName());
            return g;
        } catch (PortalException e) {
            Logger.warn(PublicGroupFactory.class, "saveGroup: saving group failed: " + g.getCompanyId() + ": " + g.getName(), e);
        } catch (SystemException e) {
            Logger.warn(PublicGroupFactory.class, "saveGroup: saving group failed: " + g.getCompanyId() + ": " + g.getName(), e);
        }
        return null;
    }
}
