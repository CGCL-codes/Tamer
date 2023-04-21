package imi.character.behavior;

import imi.objects.SpatialObject;
import imi.character.statemachine.GameContext;
import java.util.Stack;
import org.jdesktop.wonderland.common.ExperimentalAPI;

/**
 *  This class provides a framework for adding behvaior tasks for a character.
 *  Tasks at the top of the stack are highest priority.
 * 
 * @author Lou Hayt
 */
@ExperimentalAPI
public class CharacterBehaviorManager {

    private GameContext context = null;

    private Stack<Task> taskList = new Stack<Task>();

    private Task currentTask = null;

    private boolean enabledState = false;

    /**
     * Create a new instance with the given name using the provided GameContext
     * @param name
     * @param gameContext
     */
    public CharacterBehaviorManager(GameContext gameContext) {
        context = gameContext;
    }

    /**
     * This method updates the task list, making sure that the current and valid
     * task is the top of the stack.
     * @param deltaTime
     */
    public void update(float deltaTime) {
        if (!enabledState || taskList.isEmpty()) return;
        if (currentTask != null && currentTask != taskList.peek()) currentTask.onHold();
        currentTask = taskList.peek();
        if (!currentTask.verify()) {
            taskList.remove(currentTask);
            context.resetTriggersAndActions();
            currentTask = null;
        } else currentTask.update(deltaTime);
    }

    /**
     * Adds a task to the stack with top priority
     * @param task
     */
    public void addTaskToTop(Task task) {
        taskList.add(task);
    }

    /**
     * Adds a task to the stack with the lowest priority
     * @param task
     */
    public void addTaskToBottom(Task task) {
        taskList.insertElementAt(task, 0);
    }

    /**
     * Find an existing task by class type, returns the first one found.
     * @param taskClass
     * @return
     */
    public <T extends Task> T findTask(Class<T> taskClass) {
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getClass().equals(taskClass)) return taskClass.cast(task);
        }
        return null;
    }

    /**
     * Return the SpatialObject that the current task is heading towards
     * @return
     */
    public SpatialObject getGoal() {
        if (currentTask != null) return currentTask.getGoal();
        return null;
    }

    /**
     * Return the currently processing task.
     * @return
     */
    public Task getCurrentTask() {
        return currentTask;
    }

    public void start() {
        enabledState = true;
    }

    public void stop() {
        enabledState = false;
        context.resetTriggersAndActions();
        if (currentTask != null) currentTask.onHold();
    }

    public void setEnable(boolean state) {
        if (state) start(); else stop();
    }

    /**
     * Clears out the accumulated stack of tasks
     * and also calls context.resetTriggersAndActions() and puts current task
     * on hold, then sets it to null.
     */
    public void clearTasks() {
        if (currentTask != null) currentTask.onHold();
        context.resetTriggersAndActions();
        taskList.clear();
        currentTask = null;
    }

    /**
     * enable / disable
     * @return
     */
    public boolean toggleEnable() {
        if (enabledState) stop(); else start();
        return enabledState;
    }

    public boolean isEnabled() {
        return enabledState;
    }
}
