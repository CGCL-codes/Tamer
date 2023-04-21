package org.aspencloud.simple9.builder.wizards;

import static org.aspencloud.simple9.builder.projects.ProjectGenerator.*;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewSimple9ProjectWizard extends Wizard implements INewWizard {

    public static final String ID = NewSimple9ProjectWizard.class.getCanonicalName();

    private NewSimple9ProjectWizardPage page1;

    public NewSimple9ProjectWizard() {
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        page1 = new NewSimple9ProjectWizardPage("New Project");
        addPage(page1);
    }

    @Override
    public boolean performFinish() {
        try {
            getContainer().run(false, true, new WorkspaceModifyOperation() {

                protected void execute(IProgressMonitor monitor) {
                    String name = page1.getProjectName();
                    if (monitor == null) monitor = new NullProgressMonitor();
                    switch(page1.getType()) {
                        case Plugin:
                            createPluginProject(name, monitor);
                            break;
                        case Server:
                        default:
                            createServerProject(name, monitor);
                            break;
                    }
                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
