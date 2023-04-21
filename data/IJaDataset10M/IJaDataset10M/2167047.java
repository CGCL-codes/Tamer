package com.reserveamerica.jirarmi;

import java.rmi.RemoteException;
import com.reserveamerica.jirarmi.beans.issue.IssueTypeKind;
import com.reserveamerica.jirarmi.beans.issue.IssueTypeRemote;
import com.reserveamerica.jirarmi.beans.issue.PriorityRemote;
import com.reserveamerica.jirarmi.beans.issue.ResolutionRemote;
import com.reserveamerica.jirarmi.beans.issue.StatusRemote;
import com.reserveamerica.jirarmi.exceptions.JiraException;

/**
 * Unit test for the Constants service.
 * 
 * @author bstasyszyn
 */
public class TestConstantsService extends JiraRMITestCase {

    protected ConstantsService constantsService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        constantsService = factory.create(ConstantsService.NAME, ConstantsService.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetIssueTypes() throws RemoteException, JiraException {
        for (IssueTypeRemote issueType : constantsService.getIssueTypes(IssueTypeKind.ALL_ISSUE_TYPES)) {
            System.out.println("Issue Type: " + issueType);
            IssueTypeRemote i = constantsService.getIssueType(issueType.getId());
            if (!i.getId().equals(issueType.getId())) {
                fail("Issue type IDs don't match.");
            }
            if (constantsService.getIssueType(i.getId()) != i) {
                fail("Issue types don't match.");
            }
        }
    }

    public void testGetDefaultPriority() throws RemoteException, JiraException {
        constantsService.getDefaultPriority();
    }

    public void testGetPriorities() throws RemoteException, JiraException {
        for (PriorityRemote priority : constantsService.getPriorities()) {
            System.out.println("Priority: " + priority);
            PriorityRemote p = constantsService.getPriority(priority.getId());
            if (!p.getId().equals(priority.getId())) {
                fail("Priority IDs don't match.");
            }
            if (constantsService.getPriority(p.getId()) != p) {
                fail("Priorities don't match.");
            }
        }
    }

    public void testGetResolutions() throws RemoteException, JiraException {
        for (ResolutionRemote resolution : constantsService.getResolutions()) {
            System.out.println("Resolution: " + resolution);
            ResolutionRemote r = constantsService.getResolution(resolution.getId());
            if (!r.getId().equals(resolution.getId())) {
                fail("Resolution IDs don't match.");
            }
            if (constantsService.getResolution(r.getId()) != r) {
                fail("Resolutions don't match.");
            }
        }
    }

    public void testGetStatuses() throws RemoteException, JiraException {
        for (StatusRemote status : constantsService.getStatuses()) {
            System.out.println("Status: " + status);
            StatusRemote s = constantsService.getStatus(status.getId());
            if (!s.getId().equals(status.getId())) {
                fail("Status IDs don't match.");
            }
            if (constantsService.getStatus(s.getId()) != s) {
                fail("Statuses don't match.");
            }
        }
    }
}
