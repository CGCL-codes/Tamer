package org.radrails.rails.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.radrails.rails.internal.core.RailsPlugin;

/**
 * Helper class to provide convenient access to the currently selected project
 * in the workspace. Subclasses will use these methods to perform operations on
 * the selected project.
 * 
 * @author mkent
 * 
 */
public abstract class NewProjectBasedResourceWizard extends Wizard implements INewWizard {

    protected IWorkbench workbench;

    /**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
    }

    /**
	 * @return the parent project of the current selected resource, null if no
	 *         resource selected
	 */
    public IProject getProjectForName(String name) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    }

    /**
	 * @return the name of the selected project, "" if no project is selected
	 */
    public String getSelectedProjectName() {
        IProject project = RailsPlugin.getInstance().getSelectedRailsOrRubyProject();
        String name = "";
        if (project != null) {
            name = project.getName();
        }
        return name;
    }
}
