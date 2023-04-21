package edu.asu.vogon.fedora.texts.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

public class OpenServerViewActionDelegate extends ActionDelegate implements IWorkbenchWindowActionDelegate {

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ConnectionDialog dlg = new ConnectionDialog(shell);
        dlg.open();
    }
}
