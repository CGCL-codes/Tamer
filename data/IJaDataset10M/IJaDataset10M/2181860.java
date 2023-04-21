package net.sourceforge.ivi.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class iviNewProjectWizard extends BasicNewResourceWizard implements INewWizard, IExecutableExtension {

    /****************************************************************
	 * CreateProjOp
	 ****************************************************************/
    private class CreateProjOp extends WorkspaceModifyOperation {

        /************************************************************
    	 * CreateProjOp()
    	 ************************************************************/
        public CreateProjOp(iviNewProjectWizard parent) {
            d_parent = parent;
        }

        /************************************************************
    	 * execute()
    	 ************************************************************/
        protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
            try {
                execute_int(monitor);
            } catch (Exception e) {
                System.out.println("execute Exception");
                e.printStackTrace();
            }
        }

        /************************************************************
    	 * execute_int()
    	 ************************************************************/
        private void execute_int(IProgressMonitor monitor) throws CoreException {
            monitor.beginTask("Create Project", 1);
            try {
                d_project = createProject(monitor);
            } catch (CoreException e) {
                System.out.println("coreException");
                e.printStackTrace();
            }
            monitor.worked(1);
        }

        /************************************************************
    	 * createProject()
    	 ************************************************************/
        private IProject createProject(IProgressMonitor m) throws CoreException {
            IWorkspace wspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot wsroot = wspace.getRoot();
            IProject proj = wsroot.getProject(d_mainPage.getProjectNameFieldValue());
            IPath p = d_mainPage.getLocationPath();
            if (!Platform.getLocation().equals(p)) {
                IProjectDescription desc = proj.getWorkspace().newProjectDescription(proj.getName());
                desc.setLocation(p);
                proj.create(desc, new SubProgressMonitor(m, 1000));
                if (m.isCanceled()) {
                    throw new OperationCanceledException();
                }
            } else {
                proj.create(m);
            }
            proj.open(new SubProgressMonitor(m, 1000));
            addNatureToProject(proj, "net.sourceforge.ivi.core.iviProjectNature", m);
            clientAddProjectNatures(proj, m);
            clientAddProjectBuilders(proj, m);
            return proj;
        }

        private iviNewProjectWizard d_parent;
    }

    /****************************************************************
     * iviNewProjectWizard()
     ****************************************************************/
    public iviNewProjectWizard(String title) {
        super();
        setWindowTitle(title);
    }

    /****************************************************************
     * iviNewProjectWizard()
     ****************************************************************/
    public iviNewProjectWizard() {
        super();
        setWindowTitle("New IVI Project");
    }

    /****************************************************************
     * init()
     ****************************************************************/
    public void init(IWorkbench workbench, IStructuredSelection sel) {
        d_workbench = workbench;
    }

    /****************************************************************
     * setInitializationData()
     ****************************************************************/
    public void setInitializationData(IConfigurationElement c, String prop, Object obj) {
        d_configElem = c;
    }

    /****************************************************************
     * addPages()
     ****************************************************************/
    public void addPages() {
        super.addPages();
        d_mainPage = new iviNewProjectMainPage("page1");
        d_mainPage.setTitle("Create an IVI Project");
        d_mainPage.setDescription("Create a new IVI project");
        addPage(d_mainPage);
        clientAddPages();
    }

    /****************************************************************
     * clientAddPages()
     ****************************************************************/
    protected void clientAddPages() {
    }

    /****************************************************************
     * performFinish()
     ****************************************************************/
    public boolean performFinish() {
        try {
            getContainer().run(false, true, new CreateProjOp(this));
        } catch (InvocationTargetException e) {
        } catch (InterruptedException e) {
        }
        BasicNewProjectResourceWizard.updatePerspective(d_configElem);
        selectAndReveal(d_project, d_workbench.getActiveWorkbenchWindow());
        return true;
    }

    /****************************************************************
     * clientAddProjectNatures()
     ****************************************************************/
    protected void clientAddProjectNatures(IProject proj, IProgressMonitor monitor) throws CoreException {
    }

    /****************************************************************
     * clientAddProjectBuilders()
     ****************************************************************/
    protected void clientAddProjectBuilders(IProject proj, IProgressMonitor monitor) throws CoreException {
    }

    /****************************************************************
     * addNatureToProject()
     ****************************************************************/
    protected void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor) throws CoreException {
        IProjectDescription description = proj.getDescription();
        String[] prevNatures = description.getNatureIds();
        String[] newNatures = new String[prevNatures.length + 1];
        System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
        newNatures[prevNatures.length] = natureId;
        description.setNatureIds(newNatures);
        proj.setDescription(description, monitor);
    }

    /****************************************************************
     * addBuilderToProject()
     ****************************************************************/
    private void addBuilderToProject(IProject proj, String builderId) throws CoreException {
        IProjectDescription description = proj.getDescription();
        ICommand command = description.newCommand();
        command.setBuilderName(builderId);
        ICommand[] oldBuilders = description.getBuildSpec();
        ICommand[] newBuilders = new ICommand[oldBuilders.length + 1];
        System.arraycopy(oldBuilders, 0, newBuilders, 1, oldBuilders.length);
        newBuilders[0] = command;
        description.setBuildSpec(newBuilders);
        proj.setDescription(description, null);
    }

    /****************************************************************
     * Private Data
     ****************************************************************/
    protected iviNewProjectMainPage d_mainPage;

    protected IConfigurationElement d_configElem;

    protected IProject d_project;

    protected IWorkbench d_workbench;
}
