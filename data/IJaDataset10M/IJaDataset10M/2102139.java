package org.eclipse.emf.builder.internal;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Nature for EMF projects
 * 
 * @author hceylan
 * 
 */
public class EMFNature implements IProjectNature {

    /**
	 * ID of this project nature
	 */
    public static final String NATURE_ID = "org.eclipse.emf.builder.emfNature";

    /**
	 * Toggles emf nature on a project
	 * 
	 * @param project
	 *            to have emf nature added or removed
	 * @param monitor
	 */
    public static void toggleNature(IProject project, IProgressMonitor monitor) {
        try {
            final IProjectDescription description = project.getDescription();
            final String[] natures = description.getNatureIds();
            for (int i = 0; i < natures.length; ++i) {
                if (EMFNature.NATURE_ID.equals(natures[i])) {
                    final String[] newNatures = new String[natures.length - 1];
                    System.arraycopy(natures, 0, newNatures, 0, i);
                    System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
                    description.setNatureIds(newNatures);
                    project.setDescription(description, monitor);
                    EMFBuilderPlugin.getDependencyManager().unregisterProject(project, monitor);
                    return;
                }
            }
            final String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = EMFNature.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
            EMFBuilderPlugin.getDependencyManager().registerProject(project, monitor);
        } catch (final CoreException e) {
        }
    }

    private IProject project;

    public void configure() throws CoreException {
        final IProjectDescription desc = this.project.getDescription();
        final ICommand[] commands = desc.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(EMFBuilder.BUILDER_ID)) {
                return;
            }
        }
        final ICommand[] newCommands = new ICommand[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 1, commands.length);
        final ICommand command = desc.newCommand();
        command.setBuilderName(EMFBuilder.BUILDER_ID);
        newCommands[0] = command;
        desc.setBuildSpec(newCommands);
        this.project.setDescription(desc, null);
    }

    public void deconfigure() throws CoreException {
        final IProjectDescription description = this.getProject().getDescription();
        final ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(EMFBuilder.BUILDER_ID)) {
                final ICommand[] newCommands = new ICommand[commands.length - 1];
                System.arraycopy(commands, 0, newCommands, 0, i);
                System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
                description.setBuildSpec(newCommands);
                this.project.setDescription(description, null);
                return;
            }
        }
    }

    public IProject getProject() {
        return this.project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }
}
