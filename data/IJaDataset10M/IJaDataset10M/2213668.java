package org.intellij.idea.plugins.xplanner.model.strategy;

import org.xplanner.Task;

/**
 * @author karpov
 * @since 27.01.2005 17:22:37
 */
public class AllTasksStrategy implements FilteringStrategy {

    public boolean accept(final Task task) {
        return true;
    }
}
