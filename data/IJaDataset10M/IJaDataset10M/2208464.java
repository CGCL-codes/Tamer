package eu.redseeds.sc.current.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import eu.redseeds.sc.current.ui.wizards.SCLElementWizard;
import eu.redseeds.scl.model.rsl.rsldomainelements.notions.NotionDTO;

public class AddNotionAction implements IWorkbenchWindowActionDelegate, IViewActionDelegate {

    private IViewPart navigator;

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
    }

    @Override
    public void run(IAction action) {
        IStructuredSelection select = (IStructuredSelection) navigator.getViewSite().getSelectionProvider().getSelection();
        SCLElementWizard wizard = new SCLElementWizard(NotionDTO.class);
        wizard.init(PlatformUI.getWorkbench(), select);
        WizardDialog dialog = new WizardDialog(null, wizard);
        dialog.open();
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }

    @Override
    public void init(IViewPart view) {
        navigator = view;
    }
}
