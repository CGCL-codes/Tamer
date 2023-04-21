package org.mss.application.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.update.ui.UpdateManagerUI;

/**
 * Action to invoke the Update configuration manager.
 */
public class ConfigurationManagerAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public ConfigurationManagerAction() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void dispose() {
    }

    public void run(IAction action) {
        BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {

            public void run() {
                UpdateManagerUI.openConfigurationManager(window.getShell());
            }
        });
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
