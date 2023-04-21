package de.uniwue.dltk.textmarker.internal.console.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class PasteTextMarkerToConsole implements IObjectActionDelegate {

    private ISelection selection;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
}
