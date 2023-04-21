package org.intellij.idea.plugins.xplanner.model.strategy;

import org.xplanner.Task;

/**
 * @author karpov
 * @since 27.01.2005 14:44:50
 */
public class UserTasksStrategy implements FilteringStrategy {

    private final int userId;

    public UserTasksStrategy(final int userId) {
        this.userId = userId;
    }

    public boolean accept(final Task task) {
        return userId == task.getAcceptorId();
    }
}
