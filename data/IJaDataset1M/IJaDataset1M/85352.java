package net.sourceforge.eclipsetrader.internal;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.update.ui.UpdateManagerUI;

/**
 * Action to invoke the Update install wizard.
 */
public class InstallWizardAction extends Action implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public InstallWizardAction() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void dispose() {
    }

    public void run() {
        openInstaller(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    }

    public void run(IAction action) {
        openInstaller(window);
    }

    private void openInstaller(final IWorkbenchWindow window) {
        BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {

            public void run() {
                UpdateManagerUI.openInstaller(window.getShell());
            }
        });
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
