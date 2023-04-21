package uk.ac.lkl.migen.system.cdst.model;

import java.util.List;
import uk.ac.lkl.migen.system.cdst.model.event.GoalListener;
import uk.ac.lkl.migen.system.cdst.ui.goal.GoalStatus;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.task.TaskIdentifier;
import uk.ac.lkl.migen.system.task.goal.Goal;

public interface SingleStudentGoalTrackingModel {

    /**
     * Returns the goals for the current task.
     * 
     * @return the goals for the current task.
     */
    List<Goal> getGoals(TaskIdentifier taskId);

    /**
     * Adds a goal listener.
     * 
     * @param l a goal listener
     */
    void addGoalListener(GoalListener l);

    /**
     * Removes a goal listener.
     * 
     * @param l a goal listener
     */
    void removeGoalListener(GoalListener l);

    /**
     * Returns the status of this goal for this student for this task (for 
     * the current instance, i.e. expresser model).
     * 
     * @param student the student
     * @param task the task
     * @param goal the goal
     * 
     * @return the status of this goal for this student (for the current task instance/expresser model).
     */
    GoalStatus getGoalStatus(UserSet student, TaskIdentifier task, Goal goal);
}
