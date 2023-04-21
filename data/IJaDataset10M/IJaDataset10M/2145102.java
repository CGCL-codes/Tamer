package org.km.xplane.airports.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.km.xplane.airports.gui.wizards.CreateAirportWizard;

/**
 * @author kmeier
 *
 */
public class CreateAirportAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        WizardDialog dlg = new WizardDialog(window.getShell(), new CreateAirportWizard(window));
        dlg.open();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
