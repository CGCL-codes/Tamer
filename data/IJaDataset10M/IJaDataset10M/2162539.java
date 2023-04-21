package ua.org.nuos.sdms.middle.service;

import org.apache.commons.lang.Validate;
import ua.org.nuos.sdms.middle.Role;
import ua.org.nuos.sdms.middle.dao.AbstractBean;
import ua.org.nuos.sdms.middle.dao.GroupDaoBean;
import ua.org.nuos.sdms.middle.dao.UserDaoBean;
import ua.org.nuos.sdms.middle.entity.Group;
import ua.org.nuos.sdms.middle.entity.User;
import ua.org.nuos.sdms.middle.entity.UserInGroup;
import ua.org.nuos.sdms.middle.util.HashUtil;
import ua.org.nuos.sdms.middle.util.exception.UpdateEntityException;
import ua.org.nuos.sdms.middle.util.exception.FindEntityException;
import ua.org.nuos.sdms.middle.util.exception.NotGroupLeaderException;
import ua.org.nuos.sdms.middle.util.exception.OneGroupLeaderException;
import ua.org.nuos.sdms.middle.vo.GroupSearchVO;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 20.11.11
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
@Local
@Stateless
@RolesAllowed({ Role.client })
public class ClientServiceBean extends AbstractBean {

    private static Logger logger = Logger.getLogger(ClientServiceBean.class.getName());

    @EJB(beanName = "UserDaoBean")
    protected UserDaoBean userDaoBean;

    @EJB(beanName = "GroupDaoBean")
    protected GroupDaoBean groupDaoBean;

    @EJB(beanName = "AuthenticationServiceBean")
    protected AuthenticationServiceBean authenticationServiceBean;

    public void updateCurrentUser(User user) throws UpdateEntityException {
        Validate.notNull(user);
        User currentUser = authenticationServiceBean.getCurrentUser();
        if (currentUser.getId() != user.getId()) {
            throw new UpdateEntityException("Can not edit another user");
        }
        Group lastGroup = user.getLastGroup();
        if (lastGroup != null) {
            boolean isInGroup = isCurrentUserInGroup(lastGroup.getId());
            if (!isInGroup) {
                throw new UpdateEntityException("Can not save last group if user does not attached to that group");
            }
        }
        userDaoBean.updateUser(user);
    }

    /**
     * Change my (current user) password
     *
     * @param newPassword new password
     */
    public void changeCurrentUserPassword(String newPassword) {
        Validate.notEmpty(newPassword);
        User currentUser = authenticationServiceBean.getCurrentUser();
        try {
            String password = HashUtil.hash(newPassword);
            userDaoBean.changeUserPassword(currentUser.getId(), password);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.WARNING, "can not hash user password", e);
        }
    }

    /**
     * Remove me (current user) from SDMS
     * All information will be deleted !
     */
    public void removeCurrentUser() throws OneGroupLeaderException {
        User currentUser = authenticationServiceBean.getCurrentUser();
        List<Group> groups = groupDaoBean.findUserGroups(currentUser.getId());
        final int minLeadersInGroup = 2;
        for (Group g : groups) {
            UserInGroup.Type type = groupDaoBean.findUserTypeInGroup(currentUser.getId(), g.getId()).getType();
            if (UserInGroup.Type.LEADER.equals(type)) {
                int leaderNumber = groupDaoBean.findGroupLeaderNumber(g.getId());
                if (leaderNumber < minLeadersInGroup) {
                    throw new OneGroupLeaderException(g);
                }
            }
        }
        try {
            userDaoBean.removeAllUserRoles(currentUser.getId());
            for (Group g : groups) {
                groupDaoBean.detachUserFromGroup(currentUser.getId(), g.getId());
            }
        } catch (FindEntityException e) {
            logger.log(Level.WARNING, "can not remove user roles");
        }
        userDaoBean.removeUser(currentUser.getId());
    }

    /**
     * Create new group
     *
     * @param group group
     *              entity can contains just name
     */
    public Group createGroup(Group group) {
        Validate.notNull(group, "Group is NULL");
        groupDaoBean.createGroup(group);
        attachCurrentUserToGroup(group.getId(), UserInGroup.Type.LEADER);
        return group;
    }

    /**
     * Update existing group
     *
     * @param group group entity
     */
    public void updateGroup(Group group) throws UpdateEntityException {
        Validate.notNull(group, "Group is NULL");
        boolean isUserGroup = isCurrentUserInGroup(group.getId());
        if (isUserGroup) {
            groupDaoBean.updateGroup(group);
        } else {
            throw new UpdateEntityException("Can not edit group if current user is not group member");
        }
    }

    /**
     * Find existing group by name
     *
     * @param groupName group name
     * @return not null group list
     */
    public List<Group> findGroupByName(String groupName) {
        return groupDaoBean.findGroupByName(groupName);
    }

    /**
     * Find existing group by id
     *
     * @param groupId group id
     * @return not null group
     */
    public Group findGroupById(long groupId) {
        return groupDaoBean.findGroupById(groupId);
    }

    /**
     * Find all user groups
     *
     * @param userId user ID
     * @return not null group list
     */
    public List<Group> findUserGroups(long userId) {
        return groupDaoBean.findUserGroups(userId);
    }

    public List<User> findGroupUsers(long groupId) {
        return groupDaoBean.findGroupUsers(groupId);
    }

    /**
     * Find all my (current user) groups
     *
     * @return not null group list
     */
    public List<Group> findCurrentUserGroups() {
        User currentUser = authenticationServiceBean.getCurrentUser();
        return groupDaoBean.findUserGroups(currentUser.getId());
    }

    /**
     * Find all groups
     *
     * @return not null group list
     */
    public List<Group> findAllGroups() {
        return groupDaoBean.findAllGroups();
    }

    /**
     * Find groups by parameters
     *
     * @param search parameters container
     * @return not null group list
     */
    public List<Group> findGroup(GroupSearchVO search) {
        return groupDaoBean.findGroup(search);
    }

    public UserInGroup.Type findCurrentUserGroupType(long groupId) {
        User currentUser = authenticationServiceBean.getCurrentUser();
        return findUserGroupType(currentUser.getId(), groupId);
    }

    public UserInGroup.Type findUserGroupType(long userId, long groupId) {
        Validate.isTrue(userId > 0);
        Validate.isTrue(groupId > 0);
        UserInGroup userInGroup = groupDaoBean.findUserTypeInGroup(userId, groupId);
        if (userInGroup == null) {
            return null;
        } else {
            return userInGroup.getType();
        }
    }

    /**
     * Remove existing group
     *
     * @param groupId group id
     */
    public void removeGroup(long groupId) throws NotGroupLeaderException {
        UserInGroup.Type type = findCurrentUserGroupType(groupId);
        if (UserInGroup.Type.LEADER.equals(type)) {
            List<User> users = groupDaoBean.findGroupUsers(groupId);
            for (User u : users) {
                groupDaoBean.detachUserFromGroup(u.getId(), groupId);
            }
            groupDaoBean.removeGroup(groupId);
        } else {
            throw new NotGroupLeaderException("Can not remove group if current user is not group leader");
        }
    }

    public boolean isCurrentUserInGroup(long groupId) {
        UserInGroup.Type type = findCurrentUserGroupType(groupId);
        return type != null;
    }

    /**
     * Add existing user to existing group
     *
     * @param groupId group id
     */
    public void attachCurrentUserToGroup(long groupId, UserInGroup.Type type) {
        Validate.isTrue(groupId > 0);
        Validate.notNull(type);
        User currentUser = authenticationServiceBean.getCurrentUser();
        groupDaoBean.attachUserToGroup(currentUser.getId(), groupId, type);
    }

    /**
     * Remove existing user from existing group
     *
     * @param userId  user email
     * @param groupId group id
     */
    public void detachUserFromGroup(long userId, long groupId) {
        Validate.isTrue(userId > 0);
        Validate.isTrue(groupId > 0);
        UserInGroup.Type type = findCurrentUserGroupType(groupId);
        if (UserInGroup.Type.LEADER.equals(type)) {
            groupDaoBean.detachUserFromGroup(userId, groupId);
        }
    }

    /**
     * Remove me (current user) from existing group
     *
     * @param groupId group id
     */
    public void detachCurrentUserFromGroup(long groupId) throws UpdateEntityException, OneGroupLeaderException {
        Validate.isTrue(groupId > 0);
        User currentUser = authenticationServiceBean.getCurrentUser();
        UserInGroup.Type type = findCurrentUserGroupType(groupId);
        if (UserInGroup.Type.LEADER.equals(type)) {
            final int minLeadersInGroup = 2;
            int leaderNumber = groupDaoBean.findGroupLeaderNumber(groupId);
            if (leaderNumber < minLeadersInGroup) {
                throw new OneGroupLeaderException(groupDaoBean.findGroupById(groupId));
            }
        }
        groupDaoBean.detachUserFromGroup(currentUser.getId(), groupId);
    }

    public void updateUserInGroupType(long userId, long groupId, UserInGroup.Type newType) throws UpdateEntityException {
        Validate.isTrue(userId > 0);
        Validate.isTrue(groupId > 0);
        Validate.notNull(newType);
        UserInGroup.Type currentUserType = findCurrentUserGroupType(groupId);
        if (UserInGroup.Type.LEADER.equals(currentUserType)) {
            groupDaoBean.updateUserInGroupType(userId, groupId, newType);
        }
    }

    /**
     * For current user
     * Become not leader but user (UserInGroup.Type) in group with ID - groupId
     */
    public void updateCurrentUserGroupTypeFromLeaderToUser(long groupId) throws UpdateEntityException, OneGroupLeaderException {
        User currentUser = authenticationServiceBean.getCurrentUser();
        UserInGroup.Type type = findCurrentUserGroupType(groupId);
        if (UserInGroup.Type.LEADER.equals(type)) {
            final int minLeadersInGroup = 2;
            int leaderNumber = groupDaoBean.findGroupLeaderNumber(groupId);
            if (leaderNumber < minLeadersInGroup) {
                throw new OneGroupLeaderException(groupDaoBean.findGroupById(groupId));
            }
        }
        updateUserInGroupType(currentUser.getId(), groupId, UserInGroup.Type.USER);
    }
}
