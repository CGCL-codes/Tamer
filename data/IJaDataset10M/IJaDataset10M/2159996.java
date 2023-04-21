package ca.ucalgary.cpsc.agilePlanner.applicationWorkbench.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import ca.ucalgary.cpsc.agilePlanner.applicationWorkbench.uielements.PersisterConnectDialog;

public class CreateNewProjectActionDelegate extends ca.ucalgary.cpsc.agilePlanner.util.Object implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public static final String ID = "rallydemogef.actions.PersisterSelectAction";

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        if (login()) {
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    private boolean login() {
        PersisterConnectDialog pcDialog = new PersisterConnectDialog(null, true, this.window, false);
        if (pcDialog.open() != Window.OK) {
            return false;
        } else {
            return true;
        }
    }
}
