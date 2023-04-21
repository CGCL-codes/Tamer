package ch.cobex.client.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import ch.cobex.client.ui.ICommandIds;

public class OpenReportViewAction extends Action {

    private final IWorkbenchWindow window;

    private int instanceNum = 0;

    private final String viewId;

    public OpenReportViewAction(IWorkbenchWindow window, String label, String viewId) {
        this.window = window;
        this.viewId = viewId;
        setText(label);
        setId(ICommandIds.CMD_OPEN_REPORT);
        setActionDefinitionId(ICommandIds.CMD_OPEN_REPORT);
        setImageDescriptor(ch.cobex.client.Activator.getImageDescriptor("/icons/reporting.gif"));
    }

    public void run() {
        if (window != null) {
            try {
                window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
            } catch (PartInitException e) {
                MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
            }
        }
    }
}
