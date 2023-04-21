package com.centraview.contact.group;

import java.util.Vector;
import javax.ejb.EJBLocalObject;

public interface GroupLocal extends EJBLocalObject {

    public void addContactToGroup(int userId, int[] contactId, int groupId);

    public void deleteContactFromGroup(int[] contactId, int groupId);

    public int createGroup(int userId, GroupVO groupDetail);

    public GroupVO getGroupDetails(int userId, int groupId);

    public void updateGroup(int userId, GroupVO groupDetail);

    public void deleteGroup(int groupId);

    public void deleteGroupMember(int groupid, int memberId);

    public Vector getGroupMemberIDs(int userID, int groupId);

    public int duplicateGroup(int individualID, int groupID);

    /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
    public void setDataSource(String ds);
}
