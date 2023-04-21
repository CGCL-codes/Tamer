package org.intellij.idea.plugins.xplanner.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import org.intellij.idea.plugins.xplanner.ErrorHandler;
import org.intellij.idea.plugins.xplanner.model.Task;
import org.intellij.idea.plugins.xplanner.resources.Resources;
import org.intellij.idea.plugins.xplanner.view.SelectionManager;
import java.rmi.RemoteException;

/**
 * User: karpov Date: 09.02.2004 Time: 20:44:42
 */
public class ReopenAction extends AnAction {

    public void update(final AnActionEvent e) {
        super.update(e);
        e.getPresentation().setIcon(Resources.REOPEN_ICON);
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            final SelectionManager selectionManager = project.getComponent(SelectionManager.class);
            final Task selectedTask = selectionManager.getSelectedTask();
            e.getPresentation().setEnabled(selectedTask != null && selectedTask.isCompleted());
        }
    }

    public void actionPerformed(final AnActionEvent e) {
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            final SelectionManager selectionManager = project.getComponent(SelectionManager.class);
            final Task selectedTask = selectionManager.getSelectedTask();
            try {
                selectedTask.setCompleted(false);
            } catch (RemoteException e1) {
                ErrorHandler.processError(e1);
            }
        }
    }
}
