package org.eclipse.mylyn.internal.tasks.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPreferenceConstants;
import org.eclipse.mylyn.internal.tasks.ui.views.TaskListView;
import org.eclipse.mylyn.tasks.ui.TasksUiPlugin;

/**
 * @author Rob Elves
 */
public class FilterSubTasksAction extends Action {

    public static final String ID = "org.eclipse.mylyn.tasklist.actions.filter.subtasks";

    private static final String LABEL = "Filter SubTasks";

    private final TaskListView view;

    public FilterSubTasksAction(TaskListView view) {
        this.view = view;
        setText(LABEL);
        setToolTipText(LABEL);
        setId(ID);
        setChecked(TasksUiPlugin.getDefault().getPreferenceStore().getBoolean(TasksUiPreferenceConstants.FILTER_SUBTASKS));
    }

    @Override
    public void run() {
        TasksUiPlugin.getDefault().getPreferenceStore().setValue(TasksUiPreferenceConstants.FILTER_SUBTASKS, isChecked());
        try {
            view.getViewer().getControl().setRedraw(false);
            view.getViewer().collapseAll();
            if (view.isFocusedMode()) {
                view.getViewer().expandAll();
            }
            view.getViewer().refresh();
        } finally {
            view.getViewer().getControl().setRedraw(true);
        }
    }
}
