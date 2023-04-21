package com.aptana.ide.debug.internal.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.aptana.ide.debug.internal.ui.dialogs.AddExceptionBreakpointDialog;
import com.aptana.ide.debug.ui.DebugUiPlugin;

/**
 * @author Max Stepanov
 *
 */
public class AddExceptionBreakpointAction implements IWorkbenchWindowActionDelegate {

    public void run(IAction action) {
        AddExceptionBreakpointDialog dialog = new AddExceptionBreakpointDialog(DebugUiPlugin.getActiveWorkbenchShell());
        dialog.setTitle(Messages.AddExceptionBreakpointAction_AddJavaScriptExceptionBreakpoint);
        dialog.setMessage(Messages.AddExceptionBreakpointAction_ChooseException);
        dialog.open();
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
