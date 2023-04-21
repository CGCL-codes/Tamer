package org.netbeans.cubeon.gcode.internals;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.netbeans.cubeon.gcode.api.GCodeComment;
import org.netbeans.cubeon.gcode.api.GCodeIssue;
import org.netbeans.cubeon.gcode.api.GCodeIssueUpdate;
import org.netbeans.cubeon.gcode.api.GCodeQuery;

/**
 *
 * @author Anuradha
 */
public class GCodeSessionImplTest extends TestCase {

    private String testcubeon = "test-cubeon";

    private String user = null;

    private String password = null;

    public GCodeSessionImplTest(String testName) {
        super(testName);
    }

    public void testGetIssue() throws Exception {
        System.out.println("getIssue");
        GCodeSessionImpl instance = new GCodeSessionImpl(testcubeon, user, password);
        String id = "3";
        GCodeIssue expResult = null;
        GCodeIssue result = instance.getIssue(id);
        assertEquals("3", result.getId());
        assertEquals("theanuradha", result.getOwner());
        assertEquals("Started", result.getStatus());
        assertEquals("theanuradha", result.getReportedBy());
        assertEquals("Google Code Support", result.getSummary());
    }

    public void testCreateIssue() throws Exception {
        System.out.println("testCreateIssue");
        if (user != null && password != null) {
            GCodeSessionImpl instance = new GCodeSessionImpl(testcubeon, user, password);
            GCodeIssue expResult = makeNewIssue();
            GCodeIssue result = instance.createIssue(expResult, false);
            assertEquals(expResult.getOwner(), result.getOwner());
            assertEquals(expResult.getStatus(), result.getStatus());
            assertEquals(expResult.getReportedBy(), result.getReportedBy());
            assertEquals(expResult.getSummary(), result.getSummary());
            assertTrue(result.getLabels().containsAll(expResult.getLabels()));
            assertTrue(result.getCcs().containsAll(expResult.getCcs()));
        } else {
            System.out.println("Test case testCreateIssue() ignored due to user and password is null");
        }
    }

    public void testUpdateIssue() throws Exception {
        System.out.println("testUpdateIssue");
        if (user != null && password != null) {
            GCodeSessionImpl instance = new GCodeSessionImpl(testcubeon, user, password);
            GCodeIssue codeIssue = instance.createIssue(makeNewIssue(), false);
            GCodeIssueUpdate issueUpdate = new GCodeIssueUpdate(codeIssue.getId(), user);
            issueUpdate.setStatus("Accepted");
            issueUpdate.addLabel("-Priority-High");
            issueUpdate.addLabel("Priority-Low");
            issueUpdate.addCc("-" + user);
            issueUpdate.setSummary(codeIssue.getSummary() + "UPDATED");
            issueUpdate.setComment("test update comment ");
            GCodeIssue result = instance.updateIssue(issueUpdate, false);
            assertEquals(codeIssue.getSummary() + "UPDATED", result.getSummary());
            assertEquals("Accepted", result.getStatus());
            assertTrue(result.getCcs().isEmpty());
            assertTrue(result.getLabels().containsAll(Arrays.asList("Priority-Low", "Milestone-2009")));
            assertFalse(result.getLabels().containsAll(Arrays.asList("Priority-High")));
        } else {
            System.out.println("Test case testUpdateIssue() ignored due to user and password is null");
        }
    }

    public void testGetIssuesByQuery() throws Exception {
        if (user != null && password != null) {
            System.out.println("testGetIssuesByQuery");
            GCodeSessionImpl instance = new GCodeSessionImpl(testcubeon, user, password);
            GCodeIssue makeNewIssue = makeNewIssue();
            makeNewIssue.addLabel("Type-Other");
            makeNewIssue.setStatus("Started");
            instance.createIssue(makeNewIssue, false);
            GCodeQuery codeQuery = new GCodeQuery();
            codeQuery.setLabel("Type-Other");
            codeQuery.setStatus("Started");
            List<GCodeIssue> suesByQuery = instance.getIssuesByQuery(codeQuery);
            System.out.println("COUNT : " + suesByQuery.size());
            assertTrue(suesByQuery.size() > 0);
        } else {
            System.out.println("Test case testGetIssuesByQuery() ignored due to user and password is null");
        }
    }

    public void testGetIssuesByQueryString() throws Exception {
        if (user != null && password != null) {
            System.out.println("getIssuesByQueryString");
            GCodeSessionImpl instance = new GCodeSessionImpl(testcubeon, user, password);
            GCodeIssue makeNewIssue = makeNewIssue();
            makeNewIssue.addLabel("Type-Other");
            makeNewIssue.setStatus("Started");
            instance.createIssue(makeNewIssue, false);
            List<GCodeIssue> suesByQuery = instance.getIssuesByQueryString("" + "label:Type-Other label:UnitTest" + "" + " ");
            System.out.println("COUNT : " + suesByQuery.size());
            assertTrue(suesByQuery.size() > 1);
        } else {
            System.out.println("Test case testGetIssuesByQueryString() ignored due to user and password is null");
        }
    }

    protected GCodeIssue makeNewIssue() {
        GCodeIssue entry = new GCodeIssue("test", "issue summary", "issue description");
        entry.setReportedBy(user);
        entry.setStatus(("New"));
        entry.addLabel(("Priority-High"));
        entry.addLabel(("Milestone-2009"));
        entry.addCc(user);
        return entry;
    }

    public static void printGCodeIssue(GCodeIssue codeIssue, boolean printComments) {
        System.out.println("ID : " + codeIssue.getId());
        System.out.println("Summary : " + codeIssue.getSummary());
        System.out.println("Description : " + codeIssue.getDescription());
        System.out.println("Report By : " + codeIssue.getReportedBy());
        System.out.println("State : " + codeIssue.getState());
        System.out.println("Status : " + codeIssue.getStatus());
        System.out.println("Owner : " + codeIssue.getOwner());
        System.out.println("Stars : " + codeIssue.getStars());
        System.out.print("Cc: ");
        for (String cc : codeIssue.getCcs()) {
            System.out.print(cc + ", ");
        }
        System.out.println("");
        System.out.println("\nLabels___________________________");
        for (String label : codeIssue.getLabels()) {
            System.out.println(label);
        }
        if (printComments) {
            System.out.println("_________________________________");
            System.out.println("\nComments_________________________");
            for (GCodeComment comment : codeIssue.getComments()) {
                System.out.println("\tCommnet ID: " + comment.getCommentId());
                System.out.println("\tComment : " + comment.getComment());
                System.out.println("\tSummary : " + comment.getSummary());
                System.out.println("\tAuthor : " + comment.getAuthor());
                System.out.println("\tStatus : " + comment.getStatus());
                System.out.println("\tOwner : " + comment.getOwner());
                System.out.print("\tCc: ");
                for (String cc : comment.getCcs()) {
                    System.out.print(cc + ", ");
                }
                System.out.println("");
                System.out.println("\tLabels___________________________");
                for (String label : comment.getLabels()) {
                    System.out.println("\t\t" + label);
                }
                System.out.println(".....................................");
            }
            System.out.println("_________________________________");
        }
    }
}
