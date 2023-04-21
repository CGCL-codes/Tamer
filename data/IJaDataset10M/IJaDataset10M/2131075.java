package com.ibm.celldt.environment.ui.deploy.events;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import com.ibm.celldt.environment.control.ITargetStatus;
import com.ibm.celldt.environment.control.ITargetControl;
import com.ibm.celldt.environment.core.ITargetElement;
import com.ibm.celldt.environment.core.TargetElement;
import com.ibm.celldt.environment.ui.deploy.wizard.CellManagerSelectionPage;

public class ManageEnvironmentAction implements IViewActionDelegate {

    IViewPart view = null;

    ISelection selection = null;

    public void init(IViewPart v) {
        view = v;
    }

    public void run(IAction action) {
        Shell shell = view.getViewSite().getShell();
        final Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj != null) {
            if (TargetElement.class.isAssignableFrom(obj.getClass())) {
                try {
                    ITargetElement element = (ITargetElement) obj;
                    ITargetControl control = element.getControl();
                    if ((control.query() == ITargetStatus.RESUMED) || (control.query() == ITargetStatus.PAUSED)) {
                        openWizard(control);
                    } else {
                        MessageDialog.openInformation(shell, Messages.ManageEnvironmentAction_0, Messages.ManageEnvironmentAction_1);
                    }
                    return;
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
        MessageDialog.openInformation(shell, Messages.ManageEnvironmentAction_2, Messages.ManageEnvironmentAction_3);
    }

    private void openWizard(final ITargetControl control) {
        Wizard wizard = new Wizard() {

            public void addPages() {
                addPage(new CellManagerSelectionPage("CellManagerSelectionPage", control));
            }

            public boolean canFinish() {
                return false;
            }

            public boolean performFinish() {
                return false;
            }
        };
        wizard.setWindowTitle(Messages.ManageEnvironmentAction_4);
        wizard.setForcePreviousAndNextButtons(true);
        WizardDialog dialog = new WizardDialog(view.getViewSite().getShell(), wizard);
        dialog.open();
    }

    public void selectionChanged(IAction action, ISelection newSelection) {
        this.selection = newSelection;
    }
}
