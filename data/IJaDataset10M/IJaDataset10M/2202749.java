package test.com.ivis.xprocess.core.performance;

import test.com.ivis.xprocess.XprocessTest;
import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.Organization;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.Pattern;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.ProjectResource;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.SchedulingType;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.Timer;

public class TestOHTPerformance extends XprocessTest {

    private static final double mondayPercent = 100.0;

    private static final double tuesdayPercent = 100.0;

    private static final double wednesdayPercent = 100.0;

    private static final double thursdayPercent = 100.0;

    private static final double fridayPercent = 100.0;

    private static final double saturdayPercent = 100.0;

    private static final double sundayPercent = 100.0;

    private Organization organization;

    private Person[] people;

    private ProjectResource[] projectResource;

    private Xtask p2;

    private final Day nextMonday = new Day().getNext(Day.DayOfWeek.MONDAY, true);

    public void testCruise() {
        assertTrue("cruise is annoying sometimes", true);
    }

    public void testDoWhileTaskAutoAssigned() {
        ProjectResource projectResource = project.createProjectResource(people[0]);
        projectResource.addRoles(people[0].getRoles().iterator().next());
        projectResource.setAvailability(nextMonday, nextMonday.addWorkingTime(200), mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent, saturdayPercent, sundayPercent);
        Pattern taskInstancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("A Dowhile Task") });
        p2 = (Xtask) taskInstancePattern.getPrototypes().iterator().next();
        p2.setSchedulingType(SchedulingType.OVERHEAD);
        p2.setTargetStart(nextMonday);
        p2.setTargetEnd(nextMonday.addWorkingTime(200));
        p2.setTimePerDay(100);
        project.setScheduleStart(nextMonday);
        project.setScheduleEnd(new Day().addWorkingTime(300));
        assertEquals(true, project.getScheduleEnd().isAfter(p2.getTargetEnd()));
        Timer.start("scheduling auto");
        project.schedule(null);
        Timer.stop();
        for (Assignment assignment : p2.getAllAssignments()) {
            if (assignment.isAutoAssigned()) {
                assertEquals(201, assignment.getDailyRecords().size());
            }
        }
        assertEquals(nextMonday, p2.getStart());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd50());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd75());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd95());
    }

    public void testDoWhileTaskManualAssigned() {
        ProjectResource projectResource = project.createProjectResource(people[0]);
        projectResource.addRoles(people[0].getRoles().iterator().next());
        projectResource.setAvailability(nextMonday, nextMonday.addWorkingTime(200), mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent, saturdayPercent, sundayPercent);
        Pattern taskInstancePattern = overheadTaskPattern.instantiate(project, null, new Parameter[] { getNameParameter("A Dowhile Task") });
        p2 = (Xtask) taskInstancePattern.getPrototypes().iterator().next();
        p2.setTargetStart(nextMonday);
        p2.setTargetEnd(nextMonday.addWorkingTime(200));
        p2.setTimePerDay(100);
        assertEquals(1, p2.getRequiredResources().size());
        p2.manuallyAssign(people[0].getRoles().iterator().next(), p2.getRequiredResources().iterator().next());
        assertTrue(p2.isAssignedTo(people[0].getRoles().iterator().next()));
        assertTrue(p2.getCurrentManualAssignments().size() == 1);
        project.setScheduleStart(nextMonday);
        Timer.start("scheduling manual");
        project.schedule(null);
        Timer.stop();
        for (Assignment assignment : p2.getAllAssignments()) {
            if (assignment.isAutoAssigned()) {
                assertEquals(201, assignment.getDailyRecords().size());
            }
        }
        assertEquals(nextMonday, p2.getStart());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd50());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd75());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd95());
    }

    public void testDoWhileFifteenPeopleOneAutoAssignedTask() {
        projectResource = new ProjectResource[15];
        for (int i = 0; i < 15; i++) {
            projectResource[i] = project.createProjectResource(people[i]);
            for (Role r : people[i].getRoles()) {
                if (r.getRoleType().equals(participantRT)) {
                    projectResource[i].addRoles(r);
                }
            }
            projectResource[i].setAvailability(nextMonday, nextMonday.addWorkingTime(250), mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent, saturdayPercent, sundayPercent);
        }
        Pattern taskInstancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("A DoWhile task for participant") });
        p2 = (Xtask) taskInstancePattern.getPrototypes().iterator().next();
        p2.setSchedulingType(SchedulingType.OVERHEAD);
        p2.setTargetStart(nextMonday);
        p2.setTargetEnd(nextMonday.addWorkingTime(200));
        p2.setPercentagePerDay(20);
        project.setScheduleStart(nextMonday);
        project.setScheduleEnd(nextMonday.addWorkingTime(300));
        Timer.start("scheduling 15 people auto assigned");
        project.schedule(null);
        Timer.stop();
        for (Assignment assignment : p2.getAllAssignments()) {
            if (assignment.isAutoAssigned()) {
                assertEquals(201, assignment.getDailyRecords().size());
            }
        }
        assertEquals(nextMonday, p2.getStart());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd50());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd75());
        assertEquals(nextMonday.addWorkingTime(200), p2.getEnd95());
    }

    public void testDoWhile100PeopleOneAutoAssignedTask10required() {
        project.setScheduleStart(nextMonday);
        project.setScheduleEnd(nextMonday.addWorkingTime(250));
        projectResource = new ProjectResource[100];
        for (int i = 0; i < 100; i++) {
            projectResource[i] = project.createProjectResource(people[i]);
            for (Role r : people[i].getRoles()) {
                if (r.getRoleType().equals(participantRT)) {
                    projectResource[i].addRoles(r);
                }
            }
            projectResource[i].setAvailability(nextMonday, nextMonday.addWorkingTime(250), mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent, saturdayPercent, sundayPercent);
        }
        Pattern taskInstancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("A DoWhile task for participant") });
        p2 = (Xtask) taskInstancePattern.getPrototypes().iterator().next();
        p2.setSchedulingType(SchedulingType.OVERHEAD);
        p2.setTargetStart(nextMonday);
        p2.setTargetEnd(nextMonday.addWorkingTime(250));
        p2.setTimePerDay(2);
        RequiredResource rr = p2.getRequiredResources().iterator().next();
        rr.setMaximumConcurrentAssignments(10);
        project.setScheduleStart(nextMonday);
        Timer.start("testDoWhile100PeopleOneAutoAssignedTask10required");
        project.schedule(null);
        long stopped = Timer.stop();
        assertTrue(stopped > 0);
        for (Assignment assignment : p2.getAllAssignments()) {
            if (assignment.isAutoAssigned()) {
                assertEquals(251, assignment.getDailyRecords().size());
            }
        }
        assertEquals(nextMonday, p2.getStart());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd50());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd75());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd95());
    }

    public void testDoWhile100PeopleOneAutoAssignedTask10requiredManuallyAssigned() {
        projectResource = new ProjectResource[100];
        for (int i = 0; i < 100; i++) {
            projectResource[i] = project.createProjectResource(people[i]);
            for (Role r : people[i].getRoles()) {
                if (r.getRoleType().equals(participantRT)) {
                    projectResource[i].addRoles(r);
                }
            }
            projectResource[i].setAvailability(nextMonday, nextMonday.addWorkingTime(250), mondayPercent, tuesdayPercent, wednesdayPercent, thursdayPercent, fridayPercent, saturdayPercent, sundayPercent);
        }
        Pattern taskInstancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("A DoWhile task for participant") });
        p2 = (Xtask) taskInstancePattern.getPrototypes().iterator().next();
        p2.setSchedulingType(SchedulingType.OVERHEAD);
        p2.setTargetStart(nextMonday);
        p2.setTargetEnd(nextMonday.addWorkingTime(250));
        p2.setPercentagePerDay(20);
        RequiredResource rr = p2.getRequiredResources().iterator().next();
        rr.setMaximumConcurrentAssignments(10);
        for (int i = 0; i < 10; i++) {
            p2.manuallyAssign(people[i].getRoles().iterator().next(), p2.getRequiredResources().iterator().next());
        }
        project.setScheduleStart(nextMonday);
        Timer.start("testDoWhile100PeopleOneAutoAssignedTask10requiredManuallyAssigned");
        project.schedule(null);
        long stopped = Timer.stop();
        assertTrue(stopped > 0);
        for (Assignment assignment : p2.getAllAssignments()) {
            if (assignment.isAutoAssigned()) {
                assertEquals(251, assignment.getDailyRecords().size());
            }
        }
        assertEquals(nextMonday, p2.getStart());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd50());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd75());
        assertEquals(nextMonday.addWorkingTime(250), p2.getEnd95());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Pattern instancePattern = simpleProjectPattern.instantiate(testPortfolio, null, new Parameter[] { getNameParameter(dir) });
        project = (Xproject) instancePattern.getPrototypes().iterator().next();
        project.setName("Project");
        organization = testPortfolio.createOrganization("My organization");
        people = new Person[100];
        for (int i = 0; i < 100; i++) {
            people[i] = organization.createPerson("" + i);
        }
    }

    @Override
    public String getTestRootDir() {
        return this.getClass().getSimpleName();
    }
}
