package uk.co.q3c.deplan.rcp.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import uk.co.q3c.deplan.rcp.wizards.NewTaskWizard;

public class NewProjectTaskAction extends Action {

    private final IWorkbenchWindow window;

    public NewProjectTaskAction(IWorkbenchWindow window, String label) {
        this.window = window;
        setText(label);
        setId("deplan.newprojecttaskaction");
        setActionDefinitionId("deplan.newprojecttaskaction");
        setImageDescriptor(uk.co.q3c.deplan.rcp.DePlanRCPPlugin.getImageDescriptor("/icons/sample.gif"));
    }

    @Override
    public void run() {
        NewTaskWizard wizard = new NewTaskWizard();
        WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
        dialog.open();
    }
}
