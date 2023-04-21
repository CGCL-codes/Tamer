package test.com.ivis.xprocess.core.task;

import test.com.ivis.xprocess.XprocessTest;
import com.ivis.xprocess.core.Constraint;
import com.ivis.xprocess.core.Parameter;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.SchedulingType;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.Constraint.DatedItemType;
import com.ivis.xprocess.core.RequiredResource.AssignmentStatus;
import com.ivis.xprocess.core.exceptions.CyclicConstraintException;
import com.ivis.xprocess.util.Day;

public class TestAssignmentStatus extends XprocessTest {

    public void testRRAssignedLessStatusWhenRoleNotAvailable() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        setupProjectResourceA();
        rr.setRoleType(specifierRT);
        project.schedule(today);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, rr.getAssignmentStatus());
    }

    public void testRRFullyAssignedStatus() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        setupProjectResourceA();
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr.getAssignmentStatus());
    }

    public void testRRAssignedLessStatusWhenAutoAssignedMaxNotPossible() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setMaximumConcurrentAssignments(2);
        setupProjectResourceA();
        project.schedule(today);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, rr.getAssignmentStatus());
        setupProjectResourceBWhoIsASpecifier();
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr.getAssignmentStatus());
    }

    public void testRRFullyAssignedStatusWhenManuallyAssigned() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        setupProjectResourceA();
        t1.manuallyAssign(projectResourceA.getRoles().iterator().next(), rr);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr.getAssignmentStatus());
    }

    public void testRRAssignedLessStatusWhenRequiredRoleFullyBookedStatus() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        setupProjectResourceA();
        projectResourceA.setAvailability(Day.ALWAYS, Day.NEVER, 0, 0, 0, 0, 0, 0, 0);
        project.schedule(today);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, rr.getAssignmentStatus());
    }

    public void testRRNoEffortRequiredStatus() {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        t1.setBest(0);
        t1.setWorst(0);
        t1.setMostLikely(0);
        setupProjectResourceA();
        project.schedule(today);
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, rr.getAssignmentStatus());
    }

    public void testRRNoEffortRequiredOHTStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        t1.setSchedulingType(SchedulingType.OVERHEAD);
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        t1.setTimePerDay(0);
        t1.setPercentagePerDay(0.0);
        project.schedule(today);
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, rr.getAssignmentStatus());
    }

    public void testRRClosedStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        t1.close();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        assertEquals(AssignmentStatus.CLOSED, rr.getAssignmentStatus());
    }

    public void testRRConstraintStartStatus() throws CyclicConstraintException {
        setupT1InSimpleProject();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        setupProjectResourceA();
        instancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("Task 2"), getSizeParameter(2.0) });
        Xtask t2 = (Xtask) instancePattern.getPrototypes().iterator().next();
        t2.getRequiredResources().iterator().next().setRoleType(specifierRT);
        Constraint constraint = t1.createConstraint();
        constraint.setOnStart(true);
        constraint.setWorkPackage(t2);
        constraint.setDatedItemType(DatedItemType.FORECAST_END);
        project.schedule(today);
        assertEquals(AssignmentStatus.CONSTRAINED_START, rr.getAssignmentStatus());
    }

    public void testTaskConstraintStartStatus() throws CyclicConstraintException {
        setupT1InSimpleProject();
        setupProjectResourceA();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setNominalAmount(50);
        RequiredResource rr2 = t1.createRequiredResource(specifierRT);
        rr2.setNominalAmount(50);
        instancePattern = taskPattern.instantiate(project, null, new Parameter[] { getNameParameter("Task 2"), getSizeParameter(2.0) });
        Xtask t2 = (Xtask) instancePattern.getPrototypes().iterator().next();
        t2.getRequiredResources().iterator().next().setRoleType(specifierRT);
        Constraint constraint = t1.createConstraint();
        constraint.setOnStart(true);
        constraint.setWorkPackage(t2);
        constraint.setDatedItemType(DatedItemType.FORECAST_END);
        project.schedule(today);
        assertEquals(AssignmentStatus.CONSTRAINED_START, rr.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, rr2.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t1.getAssignmentStatus());
    }

    public void testTaskNoEffortSpecifiedStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setNominalAmount(50);
        RequiredResource rr2 = t1.createRequiredResource(specifierRT);
        rr2.setNominalAmount(50);
        t1.setBest(0);
        t1.setWorst(0);
        t1.setMostLikely(0);
        project.schedule(today);
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, rr.getAssignmentStatus());
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, rr2.getAssignmentStatus());
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, t1.getAssignmentStatus());
    }

    public void testTaskClosedStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setNominalAmount(50);
        RequiredResource rr2 = t1.createRequiredResource(specifierRT);
        rr2.setNominalAmount(50);
        t1.close();
        assertEquals(AssignmentStatus.CLOSED, rr.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, rr2.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t1.getAssignmentStatus());
    }

    public void testTaskFullyAssignedStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setNominalAmount(50);
        RequiredResource rr2 = t1.createRequiredResource(specifierRT);
        rr2.setNominalAmount(50);
        t1.manuallyAssign(projectResourceA.getRoles().iterator().next(), rr);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr2.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t1.getAssignmentStatus());
    }

    public void testTaskAssignedLessThanMaxStatus() {
        setupT1InSimpleProject();
        setupProjectResourceA();
        RequiredResource rr = t1.getRequiredResources().iterator().next();
        rr.setNominalAmount(50);
        RequiredResource rr2 = t1.createRequiredResource(specifierRT);
        rr2.setNominalAmount(50);
        rr2.setMaximumConcurrentAssignments(2);
        t1.manuallyAssign(projectResourceA.getRoles().iterator().next(), rr);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, rr.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, rr2.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t1.getAssignmentStatus());
    }

    public void testParentTaskAssignmentStatus() throws CyclicConstraintException {
        setupTasksInSimpleProject();
        instancePattern = taskPattern.instantiate(t1, null, new Parameter[] { getNameParameter("Task 1.3"), getSizeParameter(2.0) });
        Xtask t13 = (Xtask) instancePattern.getPrototypes().iterator().next();
        instancePattern = taskPattern.instantiate(t1, null, new Parameter[] { getNameParameter("Task 1.4"), getSizeParameter(2.0) });
        Xtask t14 = (Xtask) instancePattern.getPrototypes().iterator().next();
        setupProjectResourceA();
        setupProjectResourceBWhoIsASpecifier();
        super.projectResourceB.setAvailability(Day.ALWAYS, Day.NEVER, 0, 0, 0, 0, 0, 0, 0);
        t111.getRequiredResources().iterator().next().setRoleType(specifierRT);
        t121.getRequiredResources().iterator().next().setMaximumConcurrentAssignments(3);
        t13.getRequiredResources().iterator().next().setRoleType(adminRT);
        t14.close();
        project.schedule(today);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t1.getAssignmentStatus());
        Role adminB = b.createRole(adminRT);
        super.projectResourceB.addRoles(adminB);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t1.getAssignmentStatus());
        project.schedule(today);
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t1.getAssignmentStatus());
        super.projectResourceB.setAvailability(Day.ALWAYS, Day.NEVER, 100, 100, 100, 100, 100, 100, 100);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t1.getAssignmentStatus());
        t121.getRequiredResources().iterator().next().setMaximumConcurrentAssignments(2);
        t121.setBest(10000);
        t121.setWorst(10000);
        t121.setMostLikely(10000);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t1.getAssignmentStatus());
        projectResourceB.removeRole(adminB);
        Constraint constraint = t121.createConstraint();
        constraint.setDatedItemType(DatedItemType.FORECAST_END);
        constraint.setOnStart(true);
        constraint.setWorkPackage(t13);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t1.getAssignmentStatus());
        t121.setBest(0);
        t121.setWorst(0);
        t121.setMostLikely(0);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.ASSIGNED_LESS_THAN_MAX, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.CONSTRAINED_START, t1.getAssignmentStatus());
        projectResourceB.addRoles(adminB);
        project.schedule(today);
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t111.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t11.getAssignmentStatus());
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, t121.getAssignmentStatus());
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, t12.getAssignmentStatus());
        assertEquals(AssignmentStatus.FULLY_ASSIGNED, t13.getAssignmentStatus());
        assertEquals(AssignmentStatus.CLOSED, t14.getAssignmentStatus());
        assertEquals(AssignmentStatus.NO_EFFORT_SPECIFIED, t1.getAssignmentStatus());
    }

    @Override
    public String getTestRootDir() {
        return this.getClass().getSimpleName();
    }
}
