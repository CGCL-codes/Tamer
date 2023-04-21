package test.com.ivis.xprocess.multiuser;

import java.io.File;
import test.com.ivis.xprocess.util.Helper;
import com.ivis.xprocess.core.ActionCall;
import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.GatewayType;
import com.ivis.xprocess.core.InstantiationAction;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.ParameterizedAction;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.Portfolio;
import com.ivis.xprocess.core.ProjectResource;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.framework.CloneActionCall;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.exceptions.MoveException;
import com.ivis.xprocess.framework.exceptions.NoRootLogicalContainerException;
import com.ivis.xprocess.framework.impl.DataSourceImpl;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;
import com.ivis.xprocess.framework.vcs.exceptions.VCSNoAccessException;
import com.ivis.xprocess.framework.xml.PersistenceHelper;
import com.ivis.xprocess.util.Day;

/**
 * It is recommended that either:
 *  - a new repository is used with a separate access file for authorisation, or
 *  - a new location is added to the Apache httpd.conf that points to the existing
 *  junit repository but uses SVNPath and its own access file.
 */
public class TestSVNRestrictiveAccess extends XprocessSVNMultiUserTest {

    public void processInfo(String info) {
    }

    public void testReadAccessToAProject() throws Exception {
        assertNotNull("Check that the test Portfolio is available to use", testPortfolio1);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        makeTask(mp1, project, "Test Task 1");
        makeTask(mp1, project, "Test Task 2");
        assertEquals(2, project.getAllTasks().size());
        authenticate(ds1, USER_NAME, PASSWORD);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, project, USER_NAME2, "r");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 1 from VCS - " + e1.getMessage());
        }
        Xproject projectInDS2 = findThis(project, testPortfolio2.getContentsByType(Xproject.class));
        assertNotNull("Check that the project can be found in DS2", projectInDS2);
        makeTask(mp2, projectInDS2, "Test Task 3");
        assertEquals(3, projectInDS2.getAllTasks().size());
        mp2.saveAndAdd();
        try {
            ds2.getVcsProvider().commit();
            fail("A VCS Exception should have been thrown");
        } catch (VCSException e) {
            if (!(e instanceof VCSNoAccessException)) {
                fail("Expected - VCSNoAccessException, but found: " + e.getClass().getSimpleName());
            }
            assertTrue(e.getMessage().startsWith("org.tmatesoft.svn.core.SVNAuthenticationException: svn: Commit failed (details follow):"));
            assertTrue(e.getMessage().contains("403 Forbidden"));
        }
    }

    public void testReadAccessToATask() throws Exception {
        assertNotNull("Check that the test Portfolio is available to use", testPortfolio1);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        Xtask task = makeTask(mp1, project, "Test Task 1");
        assertEquals(1, project.getAllTasks().size());
        authenticate(ds1, USER_NAME, PASSWORD);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, task, USER_NAME2, "r");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 1 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        Xproject projectInDS2 = findThis(project, testPortfolio2.getContentsByType(Xproject.class));
        assertNotNull("Check that the project can be found in DS2", projectInDS2);
        Xtask taskInDS2 = findThis(task, projectInDS2.getContentsByType(Xtask.class));
        assertNotNull("Check that the task can be found in DS2", taskInDS2);
        taskInDS2.setName("Name change");
        mp2.saveAndAdd();
        try {
            ds2.getVcsProvider().commit();
            fail("A VCS Exception should have been thrown");
        } catch (VCSException e) {
            if (!(e instanceof VCSNoAccessException)) {
                fail("Expected - VCSNoAccessException, but found: " + e.getClass().getSimpleName());
            }
            assertTrue(e.getMessage().startsWith("org.tmatesoft.svn.core.SVNAuthenticationException: svn: Commit failed (details follow):"));
            assertTrue(e.getMessage().contains("403 Forbidden"));
        }
    }

    public void testNoAccessToRootPortfolio() throws Exception {
        String mp2FatasourceDir = test.com.ivis.xprocess.util.Helper.getExistingTemporaryDirectory(MP_2);
        File ds2Dir = new File(mp2FatasourceDir);
        if (ds2Dir.exists()) {
            test.com.ivis.xprocess.util.Helper.deleteAllFilesAndFoldersIn(ds2Dir);
        }
        createAccess(mp2, ds1.getRoot(), USER_NAME2, "");
        ds2 = mp2.getDataSource();
        ds2.setDatasourceURL(dsURL);
        ds2.setVCSType("SVN");
        ds2.setUserName(USER_NAME2);
        ds2.setPassword(PASSWORD2);
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getDescriptor().saveLocal();
            ds2.getVcsProvider().checkoutHead();
        } catch (VCSException e) {
            fail("Should have thrown a VCSException - " + e.getMessage());
        }
        try {
            String dataSourceDir = Helper.getExistingTemporaryDirectory(MP_2);
            new PersistenceHelper(dataSourceDir, dataSourceDir + DataSourceImpl.LOCAL_DIR, Portfolio.class);
            fail("Should have thrown a NoRootLogicalContainerException");
        } catch (NoRootLogicalContainerException noRootLogicalContainerException) {
            System.out.println(noRootLogicalContainerException.getMessage());
        }
    }

    /**
     * User1 and User2 have access to Project 1. User1 adds a new Task
     * and then gives User2 no access to Project 1. What does an
     * update by User2 do?
     */
    public void testCheckAccessScenario1() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        mp1.saveAndAdd();
        commit(ds1);
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 1 from VCS - " + e1.getMessage());
        }
        Xproject project1InDs2 = findThis(project, testPortfolio2.getContentsByType(Xproject.class));
        assertNotNull(project1InDs2);
        Xtask task = makeTask(mp1, project, "Test Task 1");
        mp1.saveAndAdd();
        authenticate(ds1, USER_NAME, PASSWORD);
        commit(ds1);
        createAccess(mp2, project1InDs2, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 1 from VCS - " + e1.getMessage());
        }
        Xtask taskInDs2 = findThis(task, project1InDs2.getDelegateTask().getContentsByType(Xtask.class));
        assertNull(taskInDs2);
        createAccess(mp2, project1InDs2, USER_NAME2, "rw");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 1 from VCS - " + e1.getMessage());
        }
    }

    /**
     * User1 and User2 have access to Project 1, but User 2 does not
     * have access to the Organization for Person on the Project.
     *
     * User2 should still be able to access the Project Resource, but
     * the Project will not be scheduled?
     */
    public void testCheckAccessScenario2() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Organization organization = testPortfolio1.createOrganization("Test Organization");
        Person person = organization.createPerson("Test Person");
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        ProjectResource projectResource = project.createProjectResource(person);
        projectResource.addRoles(person.getRoles().iterator().next());
        projectResource.setAvailability(new Day(), new Day().addWorkingTime(50), 100, 100, 100, 100, 100, 0, 0);
        Xtask task = makeTask(mp1, project, "Test Task 1");
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, organization, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        Portfolio rootPortfolio2 = (Portfolio) ds2.getRoot();
        testPortfolio2 = findThis(testPortfolio1, rootPortfolio2.getPortfolios());
        assertNotNull(testPortfolio2);
        Xproject project1InDs2 = findThis(project, testPortfolio2.getContentsByType(Xproject.class));
        assertNotNull(project1InDs2);
        ProjectResource projectResourceInDs2 = project1InDs2.getProjectResources().iterator().next();
        assertEquals("Person should be a ghost", true, projectResourceInDs2.getPerson().isGhost());
        Xtask taskInDs2 = findThis(task, project1InDs2.getDelegateTask().getTasks());
        assertNotNull(taskInDs2);
        project1InDs2.schedule(new Day());
        assertEquals(Day.NEVER, taskInDs2.getStart());
        assertEquals(Day.NEVER, taskInDs2.getEnd50());
        createAccess(mp1, organization, USER_NAME2, "rw");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        Organization organizationInDs2 = findThis(organization, testPortfolio2.getContentsByType(Organization.class));
        assertNotNull(organizationInDs2);
        Person personInDs2 = findThis(person, organizationInDs2.getContentsByType(Person.class));
        assertNotNull(personInDs2);
        project1InDs2 = findThis(project, testPortfolio2.getContentsByType(Xproject.class));
        assertNotNull(project1InDs2);
        taskInDs2 = findThis(task, project1InDs2.getDelegateTask().getTasks());
        assertNotNull(taskInDs2);
        projectResourceInDs2 = project1InDs2.getProjectResources().iterator().next();
        assertEquals("Test Person", projectResourceInDs2.getPerson().getName());
        assertEquals(1, projectResourceInDs2.getRoles().size());
        assertEquals("Person should no longer be a ghost", false, projectResourceInDs2.getPerson().isGhost());
        System.out.println("Persons name = " + projectResourceInDs2.getPerson().getLabel());
        project1InDs2.schedule(new Day());
        Assignment assignment = taskInDs2.getAllAssignments().iterator().next();
        RequiredResource requiredResource = assignment.getRequiredResource();
        assertNotNull(requiredResource);
        assertNotSame(Day.NEVER, taskInDs2.getStart());
        assertNotSame(Day.NEVER, taskInDs2.getEnd50());
    }

    /**
     * Test a Process that inherits from another Process that is
     * not accessible.
     */
    public void testCheckAccessScenario3() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xprocess process1 = testPortfolio1.createProcess("Test Process 1");
        Xprocess process2 = testPortfolio1.createProcess("Test Process 2");
        process2.addExtend(process1);
        assertEquals(2, process2.getExtends().size());
        assertEquals(2, process2.getAllExtends().size());
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, process1, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        assertEquals(1, testPortfolio2.getProcesses().size());
        Xprocess processInDs2 = findThis(process2, testPortfolio2.getProcesses());
        assertNotNull(processInDs2);
        assertEquals(1, processInDs2.getExtends().size());
        assertEquals(1, processInDs2.getAllExtends().size());
        assertEquals(7, processInDs2.getAllPatterns().size());
    }

    /**
     * Persons availability from an Organization that we do not have access
     * to
     */
    public void testCheckAccessScenario4() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Organization organization1 = testPortfolio1.createOrganization("Test Organization 1");
        Integer availabilityInOrg1 = new Integer(480);
        organization1.setAvailability(new Day(), new Day().addWorkingTime(10), availabilityInOrg1, availabilityInOrg1, availabilityInOrg1, availabilityInOrg1, availabilityInOrg1, 0, 0, null);
        Organization organization2 = testPortfolio1.createOrganization("Test Organization 2");
        Integer availabilityInOrg2 = new Integer(240);
        organization2.setAvailability(new Day(), new Day().addWorkingTime(10), availabilityInOrg2, availabilityInOrg2, availabilityInOrg2, availabilityInOrg2, availabilityInOrg2, availabilityInOrg2, availabilityInOrg2, null);
        Person person = organization2.createPerson("Test Person");
        int availabilityTodayInMinutes = person.getAvailability(new Day());
        assertEquals(240, availabilityTodayInMinutes);
        person.setDefaultAvailability(organization2, organization1);
        availabilityTodayInMinutes = person.getAvailability(new Day());
        assertEquals(480, availabilityTodayInMinutes);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, organization1, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        assertEquals(1, testPortfolio2.getOrganizations().size());
        Organization organization2InDs2 = findThis(organization2, testPortfolio2.getOrganizations());
        assertNotNull(organization2InDs2);
        Person personInDs2 = findThis(person, organization2InDs2.getPersons());
        assertNotNull(personInDs2);
        availabilityTodayInMinutes = personInDs2.getAvailability(new Day());
        assertEquals("Because we have no access to the Organization, the availability should be zero", 0, availabilityTodayInMinutes);
    }

    /**
     * User1 creates two Processes, a Project and a Task.
     * In Process1 a GatewayType is created and given to
     * the Task.
     *
     * User2 is not given access to Process1.
     */
    public void testGatewayNotAccessible() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xprocess process1 = testPortfolio1.createProcess("Test Process 1");
        GatewayType gatewayType = process1.createGatewayType("Test GatewayType");
        Xprocess process2 = testPortfolio1.createProcess("Test Process 2");
        process2.addExtend(process1);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        Xtask task1 = makeTask(mp1, project, "Test Task 1");
        task1.setGatewayType(gatewayType);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, process1, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        Xproject project1InDs2 = findThis(project, testPortfolio2.getProjects());
        assertNotNull(project1InDs2);
        Xtask task1InDs2 = findThis(task1, project1InDs2.getAllTasks());
        assertNotNull(task1InDs2);
        GatewayType gatewayTypeInDs2 = task1InDs2.getGatewayType();
        assertNotNull(gatewayTypeInDs2);
    }

    /**
     * Test the deletion of a Task when the user has no access to the Project
     * container
     */
    public void testDeletionOfTask() throws Exception {
        assertNotNull("Check that the test Portfolio is available to use", testPortfolio1);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        Xtask task1 = makeTask(mp1, project, "Test Task 1");
        authenticate(ds1, USER_NAME, PASSWORD);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, project, USER_NAME, "r");
        authenticate(ds1, USER_NAME, PASSWORD);
        task1.delete();
        try {
            ds1.getVcsProvider().commit();
            fail("A VCS exception should have been thrown");
        } catch (VCSException e) {
            if (e instanceof VCSNoAccessException) {
                VCSNoAccessException vcsNoAccessException = (VCSNoAccessException) e;
                Xelement element = ds1.getPersistenceHelper().getElement(vcsNoAccessException.getUuidNotAccessible());
                assertEquals(project.getUuid(), element.getUuid());
            } else {
                fail("Was expecting a VCSNoAccessException, but actually was - " + e);
            }
        }
    }

    /**
     * Process 2 inherits from Process 1
     * Process 1 has an Action
     * Process 2 has a Pattern that has an Instantiation Action
     * based on the Action in Process 1
     * User 2 has no access to Process 2
     * The Instantiation Action should be found but the action in
     * it should not be found
     */
    public void testCheckAccessScenario5() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xprocess process1 = testPortfolio1.createProcess("Test Process 1");
        Xprocess process2 = testPortfolio1.createProcess("Test Process 2");
        process2.addExtend(process1);
        ParameterizedAction parameterizedAction = process1.createParameterizedAction("Action 1", "e", false);
        Pattern pattern = process2.createPattern("Pattern 1");
        Xtask patternTask = pattern.createTask("Pattern Task 1");
        InstantiationAction instantiationAction = pattern.createInstantiationAction("Instantiation Action", parameterizedAction, null, patternTask);
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, process1, USER_NAME2, "");
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        Xprocess process2InDs2 = findThis(process2, testPortfolio2.getProcesses());
        assertNotNull(process2InDs2);
        Pattern patternInDs2 = findThis(pattern, process2InDs2.getPatterns());
        assertNotNull(patternInDs2);
        CloneActionCall instantiationActionInDs2 = findThis(instantiationAction, patternInDs2.getInstantiationActions());
        assertNotNull(instantiationActionInDs2);
        if (instantiationActionInDs2 instanceof InstantiationAction) {
            InstantiationAction ia = (InstantiationAction) instantiationActionInDs2;
            ActionCall actionCall = ia.getActionCall();
            System.out.println("ac = " + actionCall.getLabel());
            System.out.println("ac action = " + actionCall.getAction());
        }
    }

    /**
     * Try to move a Task to a Project that is read-only
     */
    public void testMovementOfTask() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xproject project = makeSimpleProject(testPortfolio1, "Test Project", "", new Day(), new Integer(10));
        Xtask task1 = makeTask(mp1, project, "Test Task 1");
        Xproject project2 = makeSimpleProject(testPortfolio1, "Test Project 2", "", new Day(), new Integer(10));
        mp1.saveAndAdd();
        commit(ds1);
        createAccess(mp1, project2, USER_NAME, "r");
        try {
            task1.moveTo(project2);
            fail("A MoveException should have been thrown");
        } catch (MoveException moveException) {
        }
    }

    /**
     * One Project that User1 and User2 has access to.
     * Then User2 is given read-only access, but should
     * still get updates to the Project made by User1.
     */
    public void testRemovedAccess() throws Exception {
        authenticate(ds1, USER_NAME, PASSWORD);
        Xproject testProject1 = makeSimpleProject(testPortfolio1, "Test Project 1", "", new Day(), new Integer(10));
        Xtask task = makeTask(mp1, testProject1, "Test Task 1");
        mp1.saveAndAdd();
        commit(ds1);
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        Xproject project1InDs2 = findThis(testProject1, testPortfolio2.getProjects());
        assertNotNull(project1InDs2);
        Xtask task1InDs2 = findThis(task, project1InDs2.getAllTasks());
        assertNotNull(task1InDs2);
        createAccess(mp1, testProject1, USER_NAME2, "r");
        testProject1.setName("Changed Project Name");
        task.setName("Changed Task Name");
        mp1.saveAndAdd();
        authenticate(ds1, USER_NAME, PASSWORD);
        commit(ds1);
        authenticate(ds2, USER_NAME2, PASSWORD2);
        try {
            ds2.getVcsProvider().update();
        } catch (VCSException e1) {
            fail("Unable to update Datasource 2 from VCS - " + e1.getMessage());
        }
        reloadTestPortfolio2();
        project1InDs2 = findThis(testProject1, testPortfolio2.getProjects());
        assertNotNull(project1InDs2);
        assertEquals("Changed Project Name", project1InDs2.getName());
        task1InDs2 = findThis(task, project1InDs2.getAllTasks());
        assertNotNull(task1InDs2);
        assertEquals("Changed Task Name", task1InDs2.getName());
    }
}
