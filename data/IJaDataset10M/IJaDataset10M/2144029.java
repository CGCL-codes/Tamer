package de.iritgo.aktera.usergroupmgr;

/**
 * @author schatterjee
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface GroupManager {

    public static final String ROLE = GroupManager.class.getName();

    public Group find(Group.Property property, Object value) throws UserMgrException;

    public Group[] list() throws UserMgrException;

    public Group add(Group group) throws UserMgrException;

    public boolean delete(Group group) throws UserMgrException;

    public boolean update(Group group) throws UserMgrException;

    public User[] listUsers(Group group) throws UserMgrException;

    public boolean addUser(Group group, User user) throws UserMgrException;

    public boolean deleteUser(Group group, User user) throws UserMgrException;

    public Group[] listGroups(User user) throws UserMgrException;

    public Group createGroup(String name, String description) throws UserMgrException;
}
