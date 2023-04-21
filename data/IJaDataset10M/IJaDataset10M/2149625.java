package orcajo.azada.store.handlers;

import orcajo.azada.store.views.QueriesView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowQueriesHandler extends AbstractHandler {

    /**
	 * The constructor.
	 */
    public ShowQueriesHandler() {
    }

    /**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        if (window == null) {
            return null;
        }
        if (window != null) {
            try {
                window.getActivePage().showView(QueriesView.ID);
            } catch (PartInitException e) {
                e.printStackTrace();
                MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
            }
        }
        return null;
    }
}
