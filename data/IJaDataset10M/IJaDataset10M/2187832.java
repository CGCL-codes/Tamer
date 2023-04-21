package com.aptana.ide.debug.internal.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import com.aptana.ide.debug.core.model.IJSLineBreakpoint;
import com.aptana.ide.debug.ui.DebugUiPlugin;

/**
 * @author Max Stepanov
 *
 */
public class BreakpointPropertiesAction implements IObjectActionDelegate {

    private IJSLineBreakpoint breakpoint;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        PropertyDialogAction propertyAction = new PropertyDialogAction(new SameShellProvider(DebugUiPlugin.getActiveWorkbenchShell()), new ISelectionProvider() {

            public void addSelectionChangedListener(ISelectionChangedListener listener) {
            }

            public ISelection getSelection() {
                return new StructuredSelection(breakpoint);
            }

            public void removeSelectionChangedListener(ISelectionChangedListener listener) {
            }

            public void setSelection(ISelection selection) {
            }
        });
        propertyAction.run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        breakpoint = null;
        if (selection instanceof IStructuredSelection) {
            Object object = ((IStructuredSelection) selection).getFirstElement();
            if (object instanceof IJSLineBreakpoint) {
                breakpoint = (IJSLineBreakpoint) object;
            }
        }
    }
}
