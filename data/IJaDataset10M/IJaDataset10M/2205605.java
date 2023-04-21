package net.sourceforge.openconferencer.client.action;

import net.sourceforge.openconferencer.client.Activator;
import net.sourceforge.openconferencer.client.ui.wizard.AddAccountWizard;
import net.sourceforge.openconferencer.client.util.LogHelper;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author aleksandar
 */
public class AddAccountAction extends Action {

    @Override
    public void run() {
        AddAccountWizard addAccountWizard = new AddAccountWizard();
        Shell shell = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
        WizardDialog dialog = new WizardDialog(shell, addAccountWizard);
        try {
            dialog.open();
        } catch (Exception ex) {
            LogHelper.error("Error occured during execution of Add Account wizard", ex);
        }
    }
}
