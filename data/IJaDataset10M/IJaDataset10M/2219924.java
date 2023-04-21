package es.gavab.eclipse.pascalfc.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import es.gavab.eclipse.pascalfc.PascalFCCorePlugin;
import es.gavab.eclipse.pascalfc.builder.PascalFCNature;

public class NewPascalFCProjectWizard extends Wizard implements INewWizard {

    WizardNewProjectCreationPage page;

    public NewPascalFCProjectWizard() {
    }

    @Override
    public void addPages() {
        super.addPages();
        setDialogSettings(PascalFCCorePlugin.getDefault().getDialogSettings());
        setWindowTitle("Choose a project name");
        page = new WizardNewProjectCreationPage("ProjectCreationWizardPage");
        page.setDescription("Creates a new PascalFC project");
        page.setTitle("New PascalFC Project");
        this.addPage(page);
    }

    @Override
    public boolean performFinish() {
        try {
            IProject project = createProjectResource();
            IProjectDescription desc = project.getDescription();
            desc.setNatureIds(getProjectNatures());
            project.setDescription(desc, new NullProgressMonitor());
        } catch (CoreException e) {
            PascalFCCorePlugin.log(e);
        }
        return true;
    }

    private String getPreference(final String name) {
        return getStore().getString(name);
    }

    private Preferences getStore() {
        return PascalFCCorePlugin.getDefault().getPluginPreferences();
    }

    private String[] getProjectNatures() {
        return new String[] { PascalFCNature.NATURE_ID };
    }

    private IProject createProjectResource() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final String projectName = page.getProjectName();
        final String projectLocation = page.getLocationPath().toString();
        IProject result = root.getProject(projectName);
        IProjectDescription desc = null;
        if (isDefaultLocation(projectLocation)) {
            desc = null;
        } else {
            desc = result.getWorkspace().newProjectDescription(projectName);
            desc.setLocation(new Path(projectLocation));
        }
        if (!result.exists()) {
            result.create(desc, null);
        }
        if (!result.isOpen()) {
            result.open(null);
        }
        return result;
    }

    private boolean isDefaultLocation(final String projectLocation) {
        return null == projectLocation || "".equals(projectLocation) || Platform.getLocation().toString().equals(projectLocation);
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }
}
