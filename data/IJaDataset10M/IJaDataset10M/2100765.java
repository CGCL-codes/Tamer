package org.eclipse.mylyn.internal.tasks.ui.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.mylyn.internal.tasks.ui.views.TaskListView;
import org.eclipse.mylyn.tasks.core.AbstractTask;
import org.eclipse.ui.actions.BaseSelectionListenerAction;

/**
 * @author Willian Mitsuda
 */
public class ShowInTaskListAction extends BaseSelectionListenerAction {

    public ShowInTaskListAction() {
        super("&Show In Task List");
    }

    @Override
    public void run() {
        IStructuredSelection struSel = getStructuredSelection();
        if (!struSel.isEmpty()) {
            Object element = struSel.getFirstElement();
            if (element instanceof AbstractTask) {
                TaskListView.openInActivePerspective();
                TaskListView.getFromActivePerspective().selectedAndFocusTask((AbstractTask) element);
            }
        }
    }
}
