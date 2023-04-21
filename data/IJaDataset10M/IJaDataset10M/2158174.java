package com.reserveamerica.jirarmi;

import java.rmi.RemoteException;
import com.reserveamerica.jirarmi.beans.issue.ChangeHistoryRemote;
import com.reserveamerica.jirarmi.beans.issue.IssueDetailsRemote;
import com.reserveamerica.jirarmi.beans.issue.IssueRemote;
import com.reserveamerica.jirarmi.exceptions.IssueNotFoundException;
import com.reserveamerica.jirarmi.exceptions.JiraException;

/**
 * Unit test for the Change History Service.
 * 
 * @author bstasyszyn
 */
public class TestChangeHistoryService extends JiraRMITestCase {

    private static final String ISSUE_1 = "PROJX-1";

    private static final String MOVED_ISSUE = "PROJX-2";

    protected ChangeHistoryService changeHistoryService;

    protected IssueService issueService;

    private IssueRemote issue;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        changeHistoryService = factory.create(ChangeHistoryService.NAME, ChangeHistoryService.class);
        issueService = factory.create(IssueService.NAME, IssueService.class);
        issue = issueService.getIssue(token, ISSUE_1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetChangeHistoriesForUser() throws RemoteException, JiraException {
        for (ChangeHistoryRemote changeHistory : changeHistoryService.getChangeHistoriesForUser(token, issue.getId())) {
            System.out.println("changeHistory: " + changeHistory);
        }
    }

    public void testFindMovedIssue() throws RemoteException, JiraException {
        try {
            IssueDetailsRemote movedIssue = changeHistoryService.findMovedIssue(token, MOVED_ISSUE);
            System.out.println("movedIssue: " + movedIssue);
        } catch (IssueNotFoundException ex) {
            System.out.println("Moved issue not found.");
        }
    }
}
