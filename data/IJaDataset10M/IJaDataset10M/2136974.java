package test.com.ivis.xprocess.core.scheduling;

import test.com.ivis.xprocess.XprocessTest;
import com.ivis.xprocess.core.Constraint;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.Constraint.DatedItemType;
import com.ivis.xprocess.core.RequiredResource.AllocationStatus;
import com.ivis.xprocess.util.Day;

public class TestAllocationStatus extends XprocessTest {

    public void testTaskClosedAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        t1.close();
        assertEquals(AllocationStatus.CLOSED, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t1.getAllocationStatus());
    }

    public void testTaskNotYetScheduledAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        assertEquals(AllocationStatus.NOT_YET_SCHEDULED, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.NOT_YET_SCHEDULED, t1.getAllocationStatus());
    }

    public void testTaskFullAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        project.schedule(today);
        assertEquals(AllocationStatus.FULL_ALLOCATION, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t1.getAllocationStatus());
    }

    public void testTaskNoAssignmentAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        project.schedule(today);
        Constraint constraint = t1.createConstraint();
        constraint.setDatedItemType(DatedItemType.CALENDAR_DATE);
        constraint.setCalendarDay(Day.NEVER);
        constraint.setOnStart(true);
        project.schedule(today);
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getAllocationStatus());
    }

    public void testTaskZeroAllocationAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        project.schedule(today);
        Constraint constraint = t1.createConstraint();
        constraint.setDatedItemType(DatedItemType.CALENDAR_DATE);
        constraint.setCalendarDay(Day.NEVER);
        constraint.setOnStart(true);
        t1.manuallyAssign(participantA, t1.getRequiredResources().iterator().next());
        project.schedule(today);
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t1.getAllocationStatus());
    }

    public void testTaskPartialAllocationStatus() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        project.setScheduleEnd(nextMonday.addDays(5));
        t1.setBest(5000);
        t1.setWorst(5000);
        t1.setMostLikely(5000);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getAllocationStatus());
    }

    public void testTaskPartialAllocationStatus2() {
        project = makeSimpleProject(testPortfolio);
        setupT1InSimpleProject();
        setupProjectResourceA();
        t1.setBest(5000);
        t1.setWorst(5000);
        t1.setMostLikely(5000);
        t1.getRequiredResources().iterator().next().manuallyBookTime(participantA, today, 200, true);
        projectResourceA.setAvailability(Day.ALWAYS, Day.NEVER, 0, 0, 0, 0, 0, 0, 0);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getRequiredResources().iterator().next().getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getAllocationStatus());
    }

    public void testParentTaskAllocationStatus() {
        setupTasksInSimpleProject();
        instancePattern = taskPattern.instantiate(t1, null, new Parameter[] { getNameParameter("Task 1.3"), getSizeParameter(2.0) });
        Xtask t13 = (Xtask) instancePattern.getPrototypes().iterator().next();
        instancePattern = taskPattern.instantiate(t1, null, new Parameter[] { getNameParameter("Task 1.4"), getSizeParameter(2.0) });
        Xtask t14 = (Xtask) instancePattern.getPrototypes().iterator().next();
        setupProjectResourceA();
        setupProjectResourceBWhoIsASpecifier();
        for (Role role : projectResourceB.getRoles()) {
            if (!role.equals(specifierB)) {
                projectResourceB.removeRole(role);
                break;
            }
        }
        assertEquals(1, projectResourceB.getRoles().size());
        super.projectResourceB.setAvailability(Day.ALWAYS, Day.NEVER, 0, 0, 0, 0, 0, 0, 0);
        super.projectResourceA.setAvailability(nextMonday.addDays(1), Day.NEVER, 0, 0, 0, 0, 0, 0, 0);
        t111.getRequiredResources().iterator().next().setRoleType(specifierRT);
        t13.getRequiredResources().iterator().next().setRoleType(adminRT);
        t14.close();
        t121.manuallyAssign(participantA, t121.getRequiredResources().iterator().next());
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t111.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t11.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getAllocationStatus());
        Role adminB = b.createRole(adminRT);
        super.projectResourceB.addRoles(adminB);
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t13.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getAllocationStatus());
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t13.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getAllocationStatus());
        t111.manuallyAssign(projectResourceB.getRoles().iterator().next(), t111.getRequiredResources().iterator().next());
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t111.getAllocationStatus());
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t11.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.NO_ASSIGNMENT, t1.getAllocationStatus());
        projectResourceB.setAvailability(nextMonday, nextMonday.addDays(1), 100, 100, 100, 100, 100, 100, 100);
        t13.manuallyAssign(specifierB, t13.getRequiredResources().iterator().next());
        t13.getRequiredResources().iterator().next().setActive(true);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t111.getAllocationStatus());
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t11.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.ZERO_ALLOCATION, t1.getAllocationStatus());
        projectResourceB.setAvailability(nextMonday.addDays(2), nextMonday.addDays(2), 100, 100, 100, 100, 100, 100, 100);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t111.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t11.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getAllocationStatus());
        projectResourceB.setAvailability(nextMonday, nextMonday.addDays(4), 100, 100, 100, 100, 100, 100, 100);
        t111.getRequiredResources().iterator().next().setActive(true);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.FULL_ALLOCATION, t111.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t11.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.PARTIAL_ALLOCATION, t1.getAllocationStatus());
        projectResourceA.setAvailability(nextMonday, nextMonday.addDays(25), 100, 100, 100, 100, 100, 100, 100);
        project.schedule(nextMonday);
        assertEquals(AllocationStatus.FULL_ALLOCATION, t111.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t11.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t121.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t12.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.FULL_ALLOCATION, t1.getAllocationStatus());
        t1.close();
        assertEquals(AllocationStatus.CLOSED, t111.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t11.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t121.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t12.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t13.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t14.getAllocationStatus());
        assertEquals(AllocationStatus.CLOSED, t1.getAllocationStatus());
    }

    @Override
    public String getTestRootDir() {
        return this.getClass().getSimpleName();
    }
}
