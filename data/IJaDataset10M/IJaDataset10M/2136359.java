package org.eclipse.mylyn.internal.tasks.ui.actions;

import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.internal.tasks.ui.LocalTaskConnectorUi;
import org.eclipse.mylyn.internal.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.ui.TasksUiUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author Mik Kersten
 * @author Eugene Kuleshov
 */
public class NewTaskAction extends Action implements IViewActionDelegate, IExecutableExtension {

    public static final String ID = "org.eclipse.mylyn.tasklist.ui.repositories.actions.create";

    private boolean skipRepositoryPage;

    @Override
    public void run() {
        IWizard wizard;
        List<TaskRepository> repositories = TasksUiPlugin.getRepositoryManager().getAllRepositories();
        if (repositories.size() == 1) {
            TaskRepository taskRepository = repositories.get(0);
            AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(taskRepository.getConnectorKind());
            wizard = connectorUi.getNewTaskWizard(taskRepository);
            if (connectorUi instanceof LocalTaskConnectorUi) {
                wizard.performFinish();
                return;
            }
        } else if (skipRepositoryPage) {
            TaskRepository taskRepository = TasksUiUtil.getSelectedRepository();
            AbstractRepositoryConnectorUi connectorUi = TasksUiPlugin.getConnectorUi(taskRepository.getConnectorKind());
            wizard = connectorUi.getNewTaskWizard(taskRepository);
            if (connectorUi instanceof LocalTaskConnectorUi) {
                wizard.performFinish();
                return;
            }
        } else {
            wizard = new NewTaskWizard();
        }
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        if (shell != null && !shell.isDisposed()) {
            WizardDialog dialog = new WizardDialog(shell, wizard);
            dialog.setBlockOnOpen(true);
            dialog.open();
        } else {
        }
    }

    public void run(IAction action) {
        run();
    }

    public void init(IViewPart view) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        if ("skipFirstPage".equals(data)) {
            this.skipRepositoryPage = true;
        }
    }
}
