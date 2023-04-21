package net.entropysoft.jmx.plugin.explorer.actions.popup;

import net.entropysoft.jmx.plugin.explorer.wizard.NewMBeanServerWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * Action that creates a new MBeanServer using a wizard
 * 
 * @author cedric
 *
 */
public class NewMBeanServerAction extends ActionDelegate implements IViewActionDelegate {

    public void run(IAction action) {
        NewMBeanServerWizard wizard = new NewMBeanServerWizard();
        wizard.init(PlatformUI.getWorkbench(), null);
        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        dialog.create();
        dialog.open();
    }

    public void init(IViewPart view) {
    }
}
