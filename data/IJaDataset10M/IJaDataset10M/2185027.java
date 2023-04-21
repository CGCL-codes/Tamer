package net.sf.securejdms.client.rcp.main.document.handlers;

import net.sf.securejdms.client.rcp.document.view.DocumentStorageView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShowDocumentStorageViewHandler extends AbstractHandler {

    /**
	 * The constructor.
	 */
    public ShowDocumentStorageViewHandler() {
    }

    /**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Command command = event.getCommand();
            boolean oldValue = HandlerUtil.toggleCommandState(command);
            IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if (!oldValue) {
                activePage.showView(DocumentStorageView.ID);
            } else {
                IViewPart findView = activePage.findView(DocumentStorageView.ID);
                if (findView != null) {
                    activePage.hideView(findView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
