package com.technoetic.xplanner.acceptance.web;

import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import com.meterware.httpunit.WebTable;

public class TopPageTestScript extends AbstractPageTestScript {

    public TopPageTestScript(String test) {
        super(test);
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        tester.logout();
        super.tearDown();
    }

    public void testTopPageLinks() throws Exception {
        tester.login();
        tester.assertLinkPresentWithKey("projects.link.add_project");
        tester.assertLinkPresentWithKey("navigation.me");
        tester.assertLinkPresentWithKey("projects.link.people");
        tester.assertLinkPresentWithKey("projects.link.aggregate.timesheet");
    }

    public void testTopPageInViewerRole() throws Exception {
        assertTopPageInRole("viewer");
        tester.assertOnTopPage();
        tester.assertLinkNotPresentWithKey("projects.link.add_project");
    }

    public void testTopPageInEditorRole() throws Exception {
        assertTopPageInRole("editor");
        tester.assertOnTopPage();
        tester.assertLinkNotPresentWithKey("projects.link.add_project");
    }

    public void testTopPageInAdminRole() throws Exception {
        Project hiddenProject = newProject();
        hiddenProject.setHidden(true);
        Project project = newProject();
        Person person = newPerson();
        setUpRole(person, project, "admin");
        commitCloseAndOpenSession();
        tester.login(person.getUserId(), "test");
        tester.assertKeyPresent("projects.title");
        tester.assertTableEquals("objecttable", new String[][] { { tester.getMessage("projects.tableheading.actions"), tester.getMessage("objects.tableheading.id"), tester.getMessage("projects.tableheading.name"), tester.getMessage("projects.tableheading.iteration") }, { "", "" + project.getId(), project.getName(), "" } });
        tester.assertLinkPresentWithImage(EDIT_IMAGE);
        tester.assertLinkNotPresentWithKey("projects.link.add_project");
        traverseLinks();
    }

    public void testTopPageInSysAdminRole() throws Exception {
        Project hiddenProject = newProject();
        hiddenProject.setHidden(true);
        Project project = newProject();
        Person person = newPerson();
        setUpRole(person, null, "sysadmin");
        commitCloseAndOpenSession();
        tester.login(person.getUserId(), "test");
        tester.assertKeyPresent("projects.title");
        boolean foundProject = false;
        boolean foundHiddenProject = false;
        assertTrue("missing row", foundProject);
        assertTrue("missing hidden row", foundHiddenProject);
        traverseLinks();
    }

    public void _testProjectEditAndReturn() throws Exception {
        tester.login();
        Project project = newProject();
        commitSession();
        tester.gotoProjectsPage();
        tester.clickEditLinkInRowWithText(project.getName());
        tester.submit();
        tester.assertKeyPresent("projects.title");
    }

    private void assertTopPageInRole(String roleName) throws Exception {
        Project hiddenProject = newProject();
        hiddenProject.setHidden(true);
        Project project = newProject();
        Person person = newPerson();
        setUpRole(person, project, roleName);
        commitCloseAndOpenSession();
        tester.login(person.getUserId(), "test");
        assertTableLooksRight(project);
        traverseLinks();
    }

    private void assertTableLooksRight(Project project) {
        tester.assertKeyPresent("projects.title");
        tester.assertTableEquals("objecttable", new String[][] { { tester.getMessage("objects.tableheading.id"), tester.getMessage("projects.tableheading.name"), tester.getMessage("projects.tableheading.iteration") }, { "" + project.getId(), project.getName(), "" } });
    }

    private void traverseLinks() throws Exception {
        traverseLinkWithKeyAndReturn("navigation.me");
        traverseLinkWithKeyAndReturn("projects.link.people");
        traverseLinkWithKeyAndReturn("projects.link.aggregate.timesheet");
    }

    private void assertProjectDisplayedCorrectly(String expectedText, WebTable table, int r) {
        assertEquals(expectedText, table.getCellAsText(r, 4).trim());
        assertImageInCell(table, r, 0, EDIT_IMAGE);
        assertImageInCell(table, r, 0, DELETE_IMAGE);
    }
}
