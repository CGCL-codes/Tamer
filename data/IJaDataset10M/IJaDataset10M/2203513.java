package org.maveryx.ide.ui.toolbar;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * @author Mauro Garofalo
 * 
 */
public class RunClassWizardAction implements IWorkbenchWindowActionDelegate {

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
    }

    @Override
    public void run(IAction action) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        OpenMaveryxClassWizardAction my = new OpenMaveryxClassWizardAction();
        my.setShell(shell);
        my.run();
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
