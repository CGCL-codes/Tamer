package org.aspencloud.simple9.builder.wizards;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewModelWizard extends Simple9ProjectWizard {

    public static final String ID = NewModelWizard.class.getCanonicalName();

    private NewModelWizardPage page1;

    public NewModelWizard() {
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        page1 = new NewModelWizardPage("New Model", getSelectedProject(selection));
        addPage(page1);
    }

    @Override
    public boolean performFinish() {
        try {
            getContainer().run(false, true, new WorkspaceModifyOperation() {

                protected void execute(IProgressMonitor monitor) {
                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
