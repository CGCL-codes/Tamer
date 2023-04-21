package net.sf.yari.ui.util.internal.dialogcreator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * opens a dialog where standard sample dialogs can be created
 * 
 * @author Remo Loetscher
 */
public class OpenDialogCreatorHandler implements IWorkbenchWindowActionDelegate {

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                new DialogCreator().run();
            }
        });
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
