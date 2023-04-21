package org.intellij.idea.plugins.xplanner.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import org.intellij.idea.plugins.xplanner.ErrorHandler;
import org.intellij.idea.plugins.xplanner.model.Model;
import org.intellij.idea.plugins.xplanner.model.ModelException;
import org.intellij.idea.plugins.xplanner.model.Task;
import org.intellij.idea.plugins.xplanner.resources.Resources;
import org.intellij.idea.plugins.xplanner.view.SelectionManager;
import java.rmi.RemoteException;

/**
 * User: karpov Date: 05.02.2004 Time: 20:37:49
 */
public class AcceptAction extends AnAction {

    public void update(final AnActionEvent e) {
        super.update(e);
        e.getPresentation().setIcon(Resources.ACCEPT_ICON);
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            final SelectionManager selectionManager = project.getComponent(SelectionManager.class);
            final Task selectedTask = selectionManager.getSelectedTask();
            e.getPresentation().setEnabled(selectedTask != null && !selectedTask.isCompleted() && selectedTask.getAcceptorId() == 0);
        }
    }

    public void actionPerformed(final AnActionEvent e) {
        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if (project != null) {
            final SelectionManager selectionManager = project.getComponent(SelectionManager.class);
            try {
                final Task selectedTask = selectionManager.getSelectedTask();
                selectedTask.accept();
                project.getComponent(Model.class).updateModel();
            } catch (RemoteException e1) {
                ErrorHandler.processError(e1);
            } catch (ModelException e1) {
                ErrorHandler.processError(e1);
            }
        }
    }
}
