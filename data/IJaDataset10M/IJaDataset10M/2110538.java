package test.com.ivis.xprocess.core.project;

import test.com.ivis.xprocess.XprocessTest;
import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.PeriodOfAppointment;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.ProjectResource;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.RoleType;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.impl.ProjectResourceImpl;
import com.ivis.xprocess.util.Day;

public class TestProjectResource extends XprocessTest {

    public void testCreationByProject() {
        project = makeSimpleProject(testPortfolio);
        assertTrue(project.getProjectResources().isEmpty());
        setupOrganization();
        Person person = organization.createPerson("Person");
        ProjectResource projectResource = project.createProjectResource(person);
        assertEquals(project, projectResource.getContainedIn());
        assertAllInAnyOrder(new ProjectResource[] { projectResource }, project.getProjectResources());
        assertEquals(projectResource, project.getProjectResource(person));
        assertNull(findThis(projectResource, project.getExplorerContents()));
        mp.saveAndAdd();
        reconnectDataSource();
        project = findThis(project, testPortfolio.getExplorerContents());
        projectResource = findThis(projectResource, project.getProjectResources());
        assertEquals(project, projectResource.getContainedIn());
        assertAllInAnyOrder(new ProjectResource[] { projectResource }, project.getProjectResources());
        assertEquals(projectResource, project.getProjectResource(person));
        assertNull(findThis(projectResource, project.getExplorerContents()));
    }

    public void testGetSet() {
        project = makeSimpleProject(testPortfolio);
        assertTrue(project.getProjectResources().isEmpty());
        setupOrganization();
        Person person = organization.createPerson("Person");
        ProjectResource projectResource = project.createProjectResource(person);
        assertEquals(person, projectResource.getPerson());
        assertEquals(project, projectResource.getProject());
        mp.saveAndAdd();
        reconnectDataSource();
        organization = (Organization) findThis(organization, testPortfolio.getExplorerContents());
        project = (Xproject) findThis(project, testPortfolio.getExplorerContents());
        person = (Person) findThis(person, organization.getContainedElements());
        projectResource = (ProjectResource) findThis(projectResource, project.getProjectResources());
        assertNotNull(projectResource);
        assertEquals(person, projectResource.getPerson());
        assertEquals(project, projectResource.getProject());
    }

    public void testProjectResourceInActive() {
        project = makeSimpleProject(testPortfolio);
        assertTrue(project.getProjectResources().isEmpty());
        setupOrganization();
        Person person = organization.createPerson("Person");
        ProjectResource projectResource = project.createProjectResource(person);
        Xprocess process = testPortfolio.createProcess("MY PROCESS");
        RoleType roleType1 = process.createRoleType("DEV");
        RoleType roleType2 = process.createRoleType("TEST");
        Role role1 = person.createRole(roleType1);
        Role role2 = person.createRole(roleType2);
        projectResource.addRoles(role1);
        projectResource.addRoles(role2);
        projectResource.setAvailability(new Day().addDays(-50), new Day().addDays(100), 100, 100, 100, 100, 100, 100, 100);
        assertTrue(projectResource.isActiveResource());
        assertEquals(2, projectResource.getRoles().size());
        projectResource.makeInactiveResource();
        assertTrue(!projectResource.isActiveResource());
        assertEquals(2, projectResource.getPeriodsOfAppointment().size());
        for (PeriodOfAppointment poa : projectResource.getPeriodsOfAppointment()) {
            if (poa.getFrom().getDayNumber() == new Day().getDayNumber()) {
                assertEquals(0.0, poa.getMondayPercent());
                assertEquals(0.0, poa.getTuesdayPercent());
                assertEquals(0.0, poa.getWednesdayPercent());
                assertEquals(0.0, poa.getThursdayPercent());
                assertEquals(0.0, poa.getFridayPercent());
                assertEquals(0.0, poa.getSaturdayPercent());
                assertEquals(0.0, poa.getSundayPercent());
                assertEquals(Day.ALWAYS, poa.getTo());
            } else {
                assertEquals(100.0, poa.getMondayPercent());
                assertEquals(100.0, poa.getTuesdayPercent());
                assertEquals(100.0, poa.getWednesdayPercent());
                assertEquals(100.0, poa.getThursdayPercent());
                assertEquals(100.0, poa.getFridayPercent());
                assertEquals(100.0, poa.getSaturdayPercent());
                assertEquals(100.0, poa.getSundayPercent());
                assertEquals(new Day().addDays(-1), poa.getTo());
            }
        }
        assertEquals(2, projectResource.getRoles().size());
    }

    public void testProjectResourcesOverlapping() {
        project = makeSimpleProject(testPortfolio);
        assertTrue(project.getProjectResources().isEmpty());
        setupOrganization();
        Person person = organization.createPerson("Person");
        ProjectResource projectResource = project.createProjectResource(person);
        Xprocess process = testPortfolio.createProcess("MY PROCESS");
        RoleType roleType1 = process.createRoleType("DEV");
        RoleType roleType2 = process.createRoleType("TEST");
        Role role1 = person.createRole(roleType1);
        Role role2 = person.createRole(roleType2);
        projectResource.addRoles(role1);
        projectResource.addRoles(role2);
        projectResource.setAvailability(new Day().addDays(-50), new Day().addDays(100), 100, 100, 100, 100, 100, 100, 100);
        assertTrue(projectResource.isActiveResource());
        assertEquals(2, projectResource.getRoles().size());
        assertEquals(1, projectResource.getPeriodsOfAppointment().size());
        projectResource.setAvailability(new Day().addDays(-10), new Day().addDays(10), 10, 10, 10, 10, 10, 10, 10);
        assertEquals(3, projectResource.getPeriodsOfAppointment().size());
        projectResource.setAvailability(new Day().addDays(-110), new Day().addDays(110), 10, 10, 10, 10, 10, 10, 10);
        assertEquals(1, projectResource.getPeriodsOfAppointment().size());
        mp.saveAndAdd();
    }

    public void testScheduleableTime() {
        setupT1InSimpleProject();
        assertTrue(project.getProjectResources().isEmpty());
        setupOrganization();
        Person person = organization.createPerson("Person");
        Role role = person.getRoles().iterator().next();
        ProjectResource projectResource = project.createProjectResource(person);
        projectResource.addRoles(role);
        projectResource.setAvailability(new Day().addDays(-50), new Day().addDays(100), 100, 100, 100, 100, 100, 100, 100);
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        t1.manuallyAssign(role, rr);
        rr.manuallyBookTime(role, today, 200, true);
        rr.manuallyBookTime(role, today.addWorkingTime(-5), 200, true);
        rr.manuallyBookTime(role, today.addWorkingTime(10), 200, true);
        rr.manuallyBookTime(role, today.addWorkingTime(25), 200, true);
        int[] times = ((ProjectResourceImpl) projectResource).getScheduleableTime(today.addDays(-10), today.addDays(30), null);
        int i = 0;
        for (Day day = today.addDays(-10); day.isOnOrBefore(today.addDays(30)); day = day.addDays(1)) {
            if (day.getDayNumber() == today.addWorkingTime(-5).getDayNumber()) {
                assertEquals(480 - 200, times[i]);
            } else if (day.getDayNumber() == today.addWorkingTime(10).getDayNumber()) {
                assertEquals(480 - 200, times[i]);
            } else if (day.getDayNumber() == today.addWorkingTime(25).getDayNumber()) {
                assertEquals(480 - 200, times[i]);
            } else if (day.isWeekday()) {
                if (day.getDayNumber() == today.getDayNumber()) {
                    assertEquals(480 - 200, times[i]);
                } else {
                    assertEquals(480, times[i]);
                }
            }
            i++;
        }
        Xtask t2 = setupCustomTaskInSimpleProject(project, "T2", 2.0);
        rr = t2.getRequiredResources().iterator().next();
        t2.manuallyAssign(role, rr);
        rr.manuallyBookTime(role, today, 100, true);
        rr.manuallyBookTime(role, today.addWorkingTime(-5), 100, true);
        rr.manuallyBookTime(role, today.addWorkingTime(10), 100, true);
        rr.manuallyBookTime(role, today.addWorkingTime(25), 100, true);
        i = 0;
        times = ((ProjectResourceImpl) projectResource).getScheduleableTime(today.addDays(-10), today.addDays(30), null);
        for (Day day = today.addDays(-10); day.isOnOrBefore(today.addDays(30)); day = day.addDays(1)) {
            if (day.getDayNumber() == today.addWorkingTime(-5).getDayNumber()) {
                assertEquals(480 - 200 - 100, times[i]);
            } else if (day.getDayNumber() == today.addWorkingTime(10).getDayNumber()) {
                assertEquals(480 - 200 - 100, times[i]);
            } else if (day.getDayNumber() == today.addWorkingTime(25).getDayNumber()) {
                assertEquals(480 - 200 - 100, times[i]);
            } else if (day.isWeekday()) {
                if (day.getDayNumber() == today.getDayNumber()) {
                    assertEquals(480 - 200 - 100, times[i]);
                } else {
                    assertEquals(480, times[i]);
                }
            }
            i++;
        }
    }

    public void testRemoveProjectResource() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        Assignment assignment = t1.manuallyAssign(a.getRoles().iterator().next(), t1.getRequiredResources().iterator().next());
        t1.getRequiredResources().iterator().next().manuallyBookTime(a.getRoles().iterator().next(), today.addDays(-5), 200, false);
        t1.getRequiredResources().iterator().next().manuallyBookTime(a.getRoles().iterator().next(), today, 200, true);
        t1.getRequiredResources().iterator().next().manuallyBookTime(a.getRoles().iterator().next(), today.addDays(15), 200, true);
        t1.getRequiredResources().iterator().next().manuallyBookTime(a.getRoles().iterator().next(), today.addDays(25), 200, false);
        assertEquals(1, project.getProjectResources().size());
        assertEquals(projectResourceA, project.getProjectResource(a));
        assertEquals(1, t1.getAllAssignmentsForPerson(a).size());
        assertEquals(4, t1.getAllAssignmentsForPerson(a).iterator().next().getDailyRecords().size());
        project.removeProjectResource(a);
        mp.saveAndAdd();
        assertEquals(0, project.getProjectResources().size());
        assertEquals(null, project.getProjectResource(a));
        assertEquals(1, t1.getAllAssignmentsForPerson(a).size());
        assertEquals(2, t1.getAllAssignmentsForPerson(a).iterator().next().getDailyRecords().size());
        assertEquals(2, assignment.getDailyRecords().size());
        assertFalse(assignment.isCurrent());
        assertEquals(0, assignment.getDailyRecordsFor(today.addDays(1), Day.NEVER).size());
    }

    @Override
    public String getTestRootDir() {
        return getClass().getSimpleName();
    }
}
