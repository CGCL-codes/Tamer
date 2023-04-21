package org.nexopenframework.ide.eclipse.actions;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.wizards.NewBusinessWizard;

/**
 * <p>NexOpen Framework</p>
 *  
 * <p></p>
 * 
 * @see IWorkbenchWindowActionDelegate
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class CreateNewBusinessAction extends Action implements IWorkbenchWindowActionDelegate {

    /**
	 * <p></p>
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
    public void dispose() {
    }

    /**
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
    public void init(final IWorkbenchWindow window) {
    }

    /**
	 * <p></p>
	 * 
	 * @see NewBusinessWizard
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(final IAction action) {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final NewBusinessWizard wizard = new NewBusinessWizard();
        wizard.init(workbench, evaluateCurrentSelection());
        final Shell shell = workbench.getActiveWorkbenchWindow().getShell();
        final WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        int status = dialog.open();
        if (status == WizardDialog.CANCEL) {
            Logger.log(Logger.INFO, "cancelled dialog of Business wizard");
        }
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
    }

    private IStructuredSelection evaluateCurrentSelection() {
        final IWorkbenchWindow window = JavaPlugin.getActiveWorkbenchWindow();
        if (window != null) {
            ISelection selection = window.getSelectionService().getSelection();
            if (selection instanceof IStructuredSelection) {
                return (IStructuredSelection) selection;
            }
        }
        return StructuredSelection.EMPTY;
    }
}
