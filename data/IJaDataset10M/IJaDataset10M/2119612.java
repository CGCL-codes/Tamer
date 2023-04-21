package org.ozoneDB.core;

import java.io.*;
import java.util.logging.Logger;
import org.ozoneDB.DxLib.*;
import org.ozoneDB.OzoneInternalException;
import org.ozoneDB.util.*;

/**
 * The UserManager holds all information about users and groups.
 *
 * @author <a href="http://www.softwarebuero.de/">SMB</a>
 * @version $Revision: 1.14 $Date: 2004/10/16 21:12:53 $
 * @see User
 * @see Group
 */
public final class UserManager extends AbstractServerComponent {

    private static final Logger logger = Logger.getLogger(UserManager.class.getName());

    protected static final long serialVersionUID = 2;

    protected static final byte subSerialVersionUID = 1;

    public static final String GROUP_TABLE = "ozoneDB.userManager.groupTable";

    public static final String USER_TABLE = "ozoneDB.userManager.userTable";

    /**
     * All currently known users. Maps String into User.
     */
    protected DxMap userTable;

    /**
     * All currently known users. Maps IDs into User.
     */
    protected DxMap idUserTable;

    /**
     * All currently known groups. Maps String into Group.
     */
    protected DxMap groupTable;

    /**
     * All currently known groups. Maps IDs into Group.
     */
    protected DxMap idGroupTable;

    /**
    	This is the userID of the system. The system has all rights and is comparable to root in
    	UNIX systems.
    */
    protected static final int SYSTEM_USER_ID = -1;

    /**
    	The User object of the GarbageCollector.
    */
    protected static final User garbageCollectorUser = new User("garbageCollector", SYSTEM_USER_ID);

    /**
		Returns the User object of the GarbageCollector.
	*/
    protected User getGarbageCollectorUser() {
        return garbageCollectorUser;
    }

    public UserManager(Server server) {
        super(server);
        groupTable = new DxHashMap();
        userTable = new DxHashMap();
    }

    /**
     *
     * @throws OzoneInternalException admin user does not exist and cannot be 
     * created
     */
    public void startup() {
        logger.info("startup...");
        groupTable = (DxMap) getServer().getState().decodeObject(GROUP_TABLE);
        userTable = (DxMap) getServer().getState().decodeObject(USER_TABLE);
        boolean isInitialized = true;
        if (groupTable == null || userTable == null) {
            logger.info("No state properties found. Initializing...");
            groupTable = new DxHashMap();
            idGroupTable = new DxHashMap();
            userTable = new DxHashMap();
            isInitialized = false;
        }
        idUserTable = new DxHashMap();
        DxIterator it = userTable.iterator();
        User user;
        while ((user = (User) it.next()) != null) {
            idUserTable.addForKey(user, user.id());
        }
        idGroupTable = new DxHashMap();
        it = groupTable.iterator();
        Group group;
        while ((group = (Group) it.next()) != null) {
            idGroupTable.addForKey(group, group.id());
        }
        if (isInitialized == false) {
            String adminName = System.getProperty("user.name");
            logger.info("admin user: " + adminName);
            try {
                newUser(adminName, 0);
                newGroup("admin", 0);
                addUserToGroup(adminName, "admin");
            } catch (UserManagerException e) {
                throw new OzoneInternalException(e);
            }
        }
    }

    public void shutdown() {
        logger.info("shutdown...");
    }

    public boolean checkPermission(User user, ObjectContainer container, int lockLevel) {
        if (lockLevel <= Lock.LEVEL_READ) {
            return checkReadPermission(user, container);
        } else {
            return checkWritePermission(user, container);
        }
    }

    protected boolean checkReadPermission(User reader, ObjectContainer container) {
        if (container.permissions().allRead()) {
            return true;
        } else if (container.permissions().ownerID == reader.id) {
            return true;
        } else {
            if (container.permissions().groupRead()) {
                User owner = userForID(container.permissions().ownerID);
                DxIterator it = groupsOfUser(owner).iterator();
                Group group;
                while ((group = (Group) it.next()) != null) {
                    if (group.containsUser(reader)) {
                        return true;
                    }
                }
            }
        }
        if (reader.getID() == SYSTEM_USER_ID) {
            return true;
        }
        return false;
    }

    protected boolean checkWritePermission(User locker, ObjectContainer container) {
        if (container.permissions().allLock()) {
            return true;
        } else if (container.permissions().ownerID == locker.id) {
            return true;
        } else {
            if (container.permissions().groupLock()) {
                User owner = userForID(container.permissions().ownerID);
                DxIterator it = groupsOfUser(owner).iterator();
                Group group;
                while ((group = (Group) it.next()) != null) {
                    if (group.containsUser(locker)) {
                        return true;
                    }
                }
            }
        }
        if (locker.getID() == SYSTEM_USER_ID) {
            return true;
        }
        return false;
    }

    public void newGroup(String name, int id) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        Group group = new Group(name, id);
        if (groupForID(id) != null) {
            throw new UserManagerException("Group id " + id + " already exists.");
        }
        if (groupForName(name) != null) {
            throw new UserManagerException("Group name '" + name + "' already exists.");
        }
        groupTable.addForKey(group, name);
        idGroupTable.addForKey(group, new Integer(id));
        getServer().getState().encodeObject(GROUP_TABLE, groupTable);
    }

    /**
     * Delete the group for the given name.
     */
    public void removeGroup(String name) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        Group group = groupForName(name);
        if (group == null) {
            throw new UserManagerException("Group '" + name + "' does not exist.");
        }
        groupTable.removeForKey(group.name);
        idGroupTable.removeForKey(new Integer(group.id));
        getServer().getState().encodeObject(GROUP_TABLE, groupTable);
    }

    protected DxBag groupsOfUser(User user) {
        DxArrayBag result = new DxArrayBag();
        DxIterator it = groupTable.iterator();
        Group group;
        while ((group = (Group) it.next()) != null) {
            if (group.containsUser(user)) {
                result.add(group);
            }
        }
        return result;
    }

    public void newUser(String name, int id) throws UserManagerException {
        newUser(name, name, id);
    }

    public void newUser(String name, String passwd, int id) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        if (passwd == null) {
            passwd = name;
        }
        User user = new User(name, passwd, id);
        if (userForID(id) != null) {
            throw new UserManagerException("User id " + id + " already exists.");
        }
        if (userForName(name) != null) {
            throw new UserManagerException("User name '" + name + "' already exists.");
        }
        userTable.addForKey(user, user.name);
        idUserTable.addForKey(user, new Integer(user.id));
        getServer().getState().encodeObject(USER_TABLE, userTable);
    }

    public void addUserToGroup(String userName, String groupName) throws UserManagerException {
        if (groupName == null) {
            throw new UserManagerException("groupname is null.");
        }
        if (userName == null) {
            throw new UserManagerException("username is null.");
        }
        Group group = groupForName(groupName);
        User user = userForName(userName);
        if (group == null) {
            throw new UserManagerException("Group '" + groupName + "' does not exist.");
        }
        if (user == null) {
            throw new UserManagerException("User '" + userName + "' does not exist.");
        }
        if (!group.addUser(user)) {
            throw new UserManagerException("User '" + userName + "' is in this group already.");
        }
        getServer().getState().encodeObject(GROUP_TABLE, groupTable);
    }

    public void removeUserFromGroup(String userName, String groupName) throws UserManagerException {
        if (groupName == null) {
            throw new UserManagerException("groupname is null.");
        }
        if (userName == null) {
            throw new UserManagerException("username is null.");
        }
        Group group = groupForName(groupName);
        User user = userForName(userName);
        if (group == null) {
            throw new UserManagerException("Group '" + groupName + "' does not exist.");
        }
        if (user == null) {
            throw new UserManagerException("User '" + userName + "' does not exist.");
        }
        if (!group.containsUser(user)) {
            throw new UserManagerException("User '" + userName + "' is not member of '" + groupName + "'.");
        }
        getServer().getState().encodeObject(GROUP_TABLE, groupTable);
        group.removeUser(user);
    }

    public void removeUser(String name) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        User user = (User) userTable.removeForKey(name);
        if (user == null) {
            throw new UserManagerException("User '" + name + "' does not exist.");
        }
        idUserTable.removeForKey(new Integer(user.id));
        DxIterator it = groupsOfUser(user).iterator();
        Group group;
        while ((group = (Group) it.next()) != null) {
            group.removeUser(user);
        }
        getServer().getState().encodeObject(USER_TABLE, userTable);
    }

    public Group groupForName(String name) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        return (Group) groupTable.elementForKey(name);
    }

    public Group groupForID(int id) {
        return (Group) idGroupTable.elementForKey(new Integer(id));
    }

    public User userForName(String name) throws UserManagerException {
        if (name == null) {
            throw new UserManagerException("username is null.");
        }
        return (User) userTable.elementForKey(name);
    }

    public User userForID(int id) {
        return (User) idUserTable.elementForKey(new Integer(id));
    }

    public DxCollection allGroups() {
        return groupTable;
    }

    public DxCollection allUsers() {
        return userTable;
    }
}
