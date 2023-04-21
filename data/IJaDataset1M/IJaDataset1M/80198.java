package net.sf.snvergui.action;

import net.sf.snvergui.Activator;
import net.sf.snvergui.ICommandIds;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class OpenLoadDataViewAction extends Action {

    private final IWorkbenchWindow window;

    private int instanceNum = 0;

    private final String load_data_viewId;

    public OpenLoadDataViewAction(IWorkbenchWindow window, String label, String viewId) {
        this.window = window;
        this.load_data_viewId = viewId;
        setText(label);
        setId(ICommandIds.CMD_OPEN);
        setActionDefinitionId(ICommandIds.CMD_OPEN);
        setImageDescriptor(net.sf.snvergui.Activator.getImageDescriptor("/icons/1.ico"));
    }

    public void run() {
        if (window != null) {
            try {
                window.getActivePage().showView(load_data_viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
            }
        }
    }
}
