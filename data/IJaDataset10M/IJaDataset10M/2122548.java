package org.jactr.eclipse.core.project;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Project nature for ACTR projects. This merely sets up the builder
 */
public class ACTRProjectNature implements IProjectNature {

    public static final String BUILDER_ID = "org.jactr.eclipse.core.builder.actrBuilder";

    public static final String NATURE_ID = "org.jactr.eclipse.core.project.actrNature";

    private IProject project;

    public void configure() throws CoreException {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();
        boolean found = false;
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(BUILDER_ID)) {
                found = true;
                break;
            }
        }
        if (!found) {
            ICommand command = desc.newCommand();
            command.setBuilderName(BUILDER_ID);
            ICommand[] newCommands = new ICommand[commands.length + 1];
            System.arraycopy(commands, 0, newCommands, 1, commands.length);
            newCommands[0] = command;
            desc.setBuildSpec(newCommands);
            project.setDescription(desc, null);
        }
    }

    public void deconfigure() throws CoreException {
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject value) {
        project = value;
    }
}
