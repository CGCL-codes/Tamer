package org.eclipse.tptp.test.tools.web.criterion.ba.assertion_wizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.tptp.models.web.common.test.data.rep.IAssertionRep;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionDelegate;

public class BAWizardActionDelegate extends ActionDelegate implements IObjectActionDelegate {

    private IWorkbenchPart part;

    private IAssertionRep rep = null;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.part = targetPart;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (selection != null && (selection instanceof IStructuredSelection)) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            if (!sel.isEmpty()) {
                Object obj = sel.getFirstElement();
                if (obj instanceof IAssertionRep) {
                    rep = (IAssertionRep) obj;
                }
            }
        }
    }

    public void run(IAction action) {
        IWorkbenchSite site = part.getSite();
        IWorkbenchWindow window = site.getWorkbenchWindow();
        BAGenerationWizard wizard = new BAGenerationWizard();
        wizard.init(window.getWorkbench(), rep);
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.open();
    }
}
