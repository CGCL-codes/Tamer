package org.vikamine.app.rcp.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vikamine.app.rcp.control.AttributeGroupManager;
import org.vikamine.app.rcp.utils.RCPUtils;
import org.vikamine.app.rcp.view.AttributeNavigatorView;

/**
 * The Class CloneGroupHandler.
 */
public class CloneGroupHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
        AttributeNavigatorView view = (AttributeNavigatorView) page.findView(AttributeNavigatorView.ID);
        ISelection selection = view.getSite().getSelectionProvider().getSelection();
        String groupToClone = RCPUtils.getFirstSelectedOfClass(selection, String.class);
        if (groupToClone != null) {
            InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(), "Enter new", "Enter new Group name", "", new AddAttributeGroupHandler.GroupExistsValidator());
            if (dlg.open() == Window.OK) {
                AttributeGroupManager.getInstance().cloneGroup(groupToClone, dlg.getValue());
            }
        }
        return null;
    }
}
