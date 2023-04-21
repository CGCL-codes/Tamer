package org.eclipse.mylyn.internal.tasks.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.mylyn.internal.tasks.core.ScheduledTaskContainer;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryQuery;
import org.eclipse.mylyn.tasks.core.AbstractTaskContainer;
import org.eclipse.mylyn.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.TaskList;
import org.eclipse.mylyn.tasks.ui.TasksUiPlugin;
import org.eclipse.ui.IWorkingSet;

/**
 * AbstractTaskListFilter for task working sets
 * 
 * @author Eugene Kuleshov
 */
public class TaskWorkingSetFilter extends AbstractTaskListFilter {

    private final TaskList taskList = TasksUiPlugin.getTaskListManager().getTaskList();

    private IWorkingSet currentWorkingSet;

    @Override
    public boolean select(Object parent, Object element) {
        if (parent instanceof AbstractTaskContainer && !(parent instanceof ScheduledTaskContainer)) {
            return selectWorkingSet((AbstractTaskContainer) parent);
        }
        if (element instanceof AbstractTask) {
            AbstractRepositoryQuery query = taskList.getQueryForHandle(((AbstractTask) element).getHandleIdentifier());
            if (query != null) {
                return selectWorkingSet(query);
            }
        }
        return true;
    }

    private boolean selectWorkingSet(AbstractTaskContainer container) {
        if (currentWorkingSet == null) {
            return true;
        }
        boolean seenTaskWorkingSets = false;
        String handleIdentifier = container.getHandleIdentifier();
        for (IAdaptable adaptable : currentWorkingSet.getElements()) {
            if (adaptable instanceof AbstractTaskContainer) {
                seenTaskWorkingSets = true;
                if (handleIdentifier.equals(((AbstractTaskContainer) adaptable).getHandleIdentifier())) {
                    return true;
                }
            }
        }
        return !seenTaskWorkingSets;
    }

    public void setCurrentWorkingSet(IWorkingSet currentWorkingSet) {
        this.currentWorkingSet = currentWorkingSet;
    }
}
