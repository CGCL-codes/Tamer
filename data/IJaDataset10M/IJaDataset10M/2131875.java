package de.unkrig.loggifier.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class ShowLoggificationPropertyPageAction implements IObjectActionDelegate {

    private ISelection selection;

    private IWorkbenchPart targetPart;

    public ShowLoggificationPropertyPageAction() {
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    public void run(IAction action) {
        if (!(this.selection instanceof IStructuredSelection)) return;
        IStructuredSelection structuredSelection = (IStructuredSelection) this.selection;
        PreferencesUtil.createPropertyDialogOn(this.targetPart.getSite().getShell(), structuredSelection.getFirstElement(), "de.unkrig.loggifier.core.propertiesPage.Loggifier", null, null, 0).open();
    }
}
