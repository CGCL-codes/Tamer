package org.powerstone.ca.service;

import java.util.*;
import org.powerstone.ca.model.Group;

public interface GroupManager {

    public Group findGroup(String groupID);

    public List findAllGroups();

    public List findGroupMembers(String groupID);

    public List findGroupMembersByPage(String groupID, int pageNum, int pageSize);

    public void addUserToGroup(String userID, String groupID);

    public Group createGroup(Group group);

    public Group createSubGroup(String groupID, Group sonGroup);

    public void removeGroup(String groupID);

    public void removeUserFromGroup(String groupID, String userID);

    public Group updateGroup(Group group);
}
