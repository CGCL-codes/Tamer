package com.reserveamerica.jirarmi;

import java.rmi.RemoteException;
import com.reserveamerica.jirarmi.beans.search.PagerFilterRemote;
import com.reserveamerica.jirarmi.beans.search.SearchResultsRemote;
import com.reserveamerica.jirarmi.beans.user.GroupRemote;
import com.reserveamerica.jirarmi.exceptions.GroupExistsException;
import com.reserveamerica.jirarmi.exceptions.JiraException;

/**
 * Unit test for the Group service.
 * 
 * @author bstasyszyn
 */
public class TestGroupService extends JiraRMITestCase {

    private static final String[] TEST_GROUPS = { "test-group1", "test-group2" };

    protected GroupService groupService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        groupService = factory.create(GroupService.NAME, GroupService.class);
        for (String groupName : TEST_GROUPS) {
            try {
                groupService.createGroup(token, groupName);
            } catch (GroupExistsException ex) {
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        for (String group : TEST_GROUPS) {
            groupService.removeGroup(token, group);
        }
        super.tearDown();
    }

    public void testGetGroup() throws JiraException, RemoteException {
        for (String groupName : TEST_GROUPS) {
            GroupRemote group = groupService.getGroup(token, groupName);
            System.out.println(group);
        }
    }

    public void testGroupExists() throws JiraException, RemoteException {
        for (String groupName : TEST_GROUPS) {
            if (!groupService.groupExists(token, groupName)) {
                fail("Group [" + groupName + "] does not exist.");
            }
        }
    }

    public void testGetGroups() throws JiraException, RemoteException {
        for (GroupRemote group : groupService.getGroups(token)) {
            System.out.println(group);
        }
    }

    public void testGetGroupMemberNames() throws JiraException, RemoteException {
        for (GroupRemote group : groupService.getGroups(token)) {
            System.out.println("Members for group [" + group.getName() + "]:");
            PagerFilterRemote pager = new PagerFilterRemote(0, 3);
            SearchResultsRemote<String> searchResults;
            do {
                searchResults = groupService.getGroupMemberNames(token, group.getName(), pager);
                for (String userName : searchResults.getItems()) {
                    System.out.println("   " + userName);
                }
                pager.setStart(searchResults.getNextStart());
            } while (searchResults.getNextStart() < searchResults.getTotal());
        }
    }
}
