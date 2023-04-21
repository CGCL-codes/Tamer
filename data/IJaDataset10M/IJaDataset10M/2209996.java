package com.ivis.xprocess.ui.datawrappers.project;

import com.ivis.xprocess.core.Assignment;
import com.ivis.xprocess.core.DailyRecord;
import com.ivis.xprocess.core.RequiredResource;
import com.ivis.xprocess.core.Role;
import com.ivis.xprocess.core.RoleType;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.core.ui.RequiredResourceAssignmentRow;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.UIType;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.tables.columns.definition.XProcessColumn;
import com.ivis.xprocess.util.Day;

/**
 * ElementWrapper for a UI based component - RequiredResourceAssignmentRow. It is
 * never cached and therefore it is recreated each time it is needed.
 *
 * Its parent wrapper is ProjectAvailabilityAndAssignmentWrapper.
 *
 */
public class RequiredResourceRoleWrapper extends TaskWrapper implements IManageTime {

    private RequiredResourceAssignmentRow taskPlannerRow;

    public RequiredResourceRoleWrapper(IElementWrapper parent, RequiredResourceAssignmentRow taskPlannerRow) {
        super(parent, taskPlannerRow.getRequiredResource());
        this.taskPlannerRow = taskPlannerRow;
    }

    @Override
    public boolean testAttribute(Object target, String name, String value) {
        if (isGhost() || getTask().isGhost()) {
            return false;
        }
        boolean flag = super.testAttribute(target, name, value);
        if (name.equals("parentIsProject")) {
            if (getTask().getContainedIn() instanceof Xproject) {
                flag = true;
            }
        }
        return flag;
    }

    public Assignment getAssignment() {
        return taskPlannerRow.getAssignment();
    }

    /**
     * Returns the real task, not the transient.
     *
     * @see com.ivis.xprocess.ui.datawrappers.project.TaskWrapper#getTask()
     */
    public Xtask getTask() {
        return (Xtask) UIPlugin.getPersistenceHelper().getElement(taskPlannerRow.getTask().getUuid());
    }

    public int getTime(Day day) {
        return taskPlannerRow.getTimeFor(day);
    }

    public DailyRecord getDailyRecord(Day day) {
        return taskPlannerRow.getDailyRecord(day);
    }

    public void setTimeFor(Day day, int minutes, boolean confirmed) {
        taskPlannerRow.setTimeFor(day, minutes, confirmed);
    }

    @Override
    public String getLabel() {
        if (getTask().isDesignatedAsParent() || (getTask().getTasks().size() > 0)) {
            return "REST of " + getTask().getLabel();
        }
        return getTask().getLabel();
    }

    @Override
    public UIType getUIType() {
        return TaskWrapper.getUIType(getTask());
    }

    public RoleType getRoleType() {
        return taskPlannerRow.getRequiredResource().getRoleType();
    }

    public boolean isCurrent() {
        if ((taskPlannerRow != null) && (taskPlannerRow.isCurrentRole() != null)) {
            return taskPlannerRow.isCurrentRole();
        }
        return false;
    }

    public Role getRole() {
        return taskPlannerRow.getRole();
    }

    public IElementWrapper getElementWrapper() {
        return this;
    }

    public int getMaxRanking() {
        return 0;
    }

    public void setMaxRanking(int ranking) {
    }

    public Object[] getTasks() {
        return null;
    }

    public Object[] getRequiredResources() {
        return new Object[] { getTask().getRequiredResources() };
    }

    public RequiredResource getRequiredResource() {
        return taskPlannerRow.getRequiredResource();
    }

    public boolean isAuto(Day day) {
        return taskPlannerRow.isAuto(day);
    }

    public boolean isManual(Day day) {
        return (!taskPlannerRow.isAuto(day) && !taskPlannerRow.isConfirmed(day));
    }

    public boolean isConfirmed(Day day) {
        return taskPlannerRow.isConfirmed(day);
    }

    /**
     * Save the Xtask
     */
    public void save() {
        ProjectAvailabilityAndAssignmentWrapper projectAvailabilityAndAssignmentWrapper = (ProjectAvailabilityAndAssignmentWrapper) getParent();
        Xtask localTransientTask = projectAvailabilityAndAssignmentWrapper.getLocalTransientTaskFor(getTask().getUuid());
        Xtask baseTransientTask = projectAvailabilityAndAssignmentWrapper.getBaseTransientTaskFor(getTask().getUuid());
        localTransientTask.mergeAndSave(baseTransientTask);
    }

    /**
     * @return the local transient version of the Xtazk from its parent - ProjectAvailabilityAndAssignmentWrapper
     */
    public Xtask getLocalTransientTaskFor() {
        Xtask localTransientTask = null;
        if (getParent() instanceof ProjectDailyLogWrapper) {
            ProjectDailyLogWrapper projectDailyLogWrapper = (ProjectDailyLogWrapper) getParent();
            localTransientTask = projectDailyLogWrapper.getLocalTransientTaskFor(getTask().getUuid());
        } else {
            ProjectAvailabilityAndAssignmentWrapper projectAvailabilityAndAssignmentWrapper = (ProjectAvailabilityAndAssignmentWrapper) getParent();
            localTransientTask = projectAvailabilityAndAssignmentWrapper.getLocalTransientTaskFor(getTask().getUuid());
        }
        return localTransientTask;
    }

    public void revertDailyRecordFor(Day day) {
        Assignment assignment = getAssignment();
        if (!assignment.isGhost() && (assignment.getCurrentRole() != null)) {
            assignment.getRequiredResource().revertTime(assignment.getCurrentRole(), day);
        }
    }

    public boolean canInplaceEdit(XProcessColumn column) {
        if (getTask().isDesignatedAsParent()) {
            return false;
        }
        return !(getTask().getTasks().size() > 0);
    }

    public void setAssignment(Assignment assignment) {
        taskPlannerRow.setAssignment(assignment);
    }

    public boolean hasWorkLog(Day day) {
        return taskPlannerRow.hasWorkLog(day);
    }

    public boolean isShortcut() {
        return false;
    }
}
