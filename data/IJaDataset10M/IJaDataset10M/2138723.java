package com.android.ide.eclipse.adt.internal.wizards.actions;

import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;

public class ExportWizardAction implements IObjectActionDelegate {

    private ISelection mSelection;

    private IWorkbench mWorkbench;

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        mWorkbench = targetPart.getSite().getWorkbenchWindow().getWorkbench();
    }

    public void run(IAction action) {
        if (mSelection instanceof IStructuredSelection) {
            IStructuredSelection selection = (IStructuredSelection) mSelection;
            ExportWizard wizard = new ExportWizard();
            wizard.init(mWorkbench, selection);
            WizardDialog dialog = new WizardDialog(mWorkbench.getDisplay().getActiveShell(), wizard);
            dialog.open();
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.mSelection = selection;
    }
}
