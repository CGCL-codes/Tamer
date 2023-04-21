package com.halware.nakedide.eclipse.ext.nature;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import com.halware.nakedide.eclipse.ext.builders.BuilderUtil;
import com.halware.nakedide.eclipse.ext.builders.Constants;

public class NakedObjectNature implements IProjectNature {

    private static final Logger LOGGER = Logger.getLogger(NakedObjectNature.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    private IProject project;

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }

    public void configure() throws CoreException {
        IProject project = this.getProject();
        IProjectDescription desc = project.getDescription();
        IProgressMonitor monitor = new NullProgressMonitor();
        ICommand builderCommand = getBuilderCommand(desc, Constants.BUILDER_ID);
        if (builderCommand == null) {
            ICommand command = desc.newCommand();
            command.setBuilderName(Constants.BUILDER_ID);
            setBuilderCommand(desc, command, monitor, project);
            project.setDescription(desc, monitor);
        }
    }

    public void deconfigure() throws CoreException {
        IProject project = getProject();
        BuilderUtil.removeProjectBuilder(project, Constants.BUILDER_ID, new NullProgressMonitor());
    }

    private ICommand getBuilderCommand(IProjectDescription description, String builderId) {
        ICommand command = null;
        ICommand[] commands = description.getBuildSpec();
        for (int i = commands.length - 1; i >= 0; i--) {
            if (commands[i].getBuilderName().equals(builderId)) {
                command = commands[i];
                break;
            }
        }
        return command;
    }

    private void setBuilderCommand(IProjectDescription description, ICommand command, IProgressMonitor monitor, IProject project) throws CoreException {
        ICommand[] oldCommands = description.getBuildSpec();
        ICommand oldBuilderCommand = getBuilderCommand(description, command.getBuilderName());
        ICommand[] newCommands;
        if (oldBuilderCommand == null) {
            newCommands = new ICommand[oldCommands.length + 1];
            System.arraycopy(oldCommands, 0, newCommands, 0, oldCommands.length);
            newCommands[oldCommands.length] = command;
        } else {
            for (int i = 0, max = oldCommands.length; i < max; i++) {
                if (oldCommands[i] == oldBuilderCommand) {
                    oldCommands[i] = command;
                    break;
                }
            }
            newCommands = oldCommands;
        }
        description.setBuildSpec(newCommands);
        project.setDescription(description, monitor);
    }
}
