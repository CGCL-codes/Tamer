package org.xaware.designer.lint;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.xaware.shared.util.logging.XAwareLogger;

public class XALintNature implements IProjectNature {

    /**
     * ID of this project nature
     */
    public static final String NATURE_ID = "org.xaware.designer.XALintNature";

    public static final String EXT_TOOL_BUILDER = "org.eclipse.ui.externaltools.ExternalToolBuilder";

    public static final String LAUNCH_KEY = "LaunchConfigHandle";

    /** Logger for XALintNature */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(XALintNature.class.getName());

    private static final String className = "XALintNature";

    private IProject project;

    public void configure() throws CoreException {
        final String methodName = "configure";
        logger.entering(className, methodName);
        final IProjectDescription desc = project.getDescription();
        final ICommand[] commands = desc.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            final String buildName = commands[i].getBuilderName();
            if (XALint.BUILDER_ID.equals(buildName)) {
                logger.exiting(className, methodName, "Build spec " + XALint.BUILDER_ID + " detected");
                return;
            }
        }
        final ICommand[] newCommands = new ICommand[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 0, commands.length);
        final ICommand command = desc.newCommand();
        command.setBuilderName(XALint.BUILDER_ID);
        newCommands[newCommands.length - 1] = command;
        desc.setBuildSpec(newCommands);
        project.setDescription(desc, null);
        logger.exiting(className, methodName, "Added build spec for " + XALint.BUILDER_ID);
    }

    public void deconfigure() throws CoreException {
        final String methodName = "deconfigure";
        logger.entering(className, methodName);
        final List<ICommand> savedCommands = new ArrayList<ICommand>();
        final IProjectDescription description = getProject().getDescription();
        final ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            final String buildName = commands[i].getBuilderName();
            if (XALint.BUILDER_ID.equals(buildName)) {
                logger.finest(className, methodName, "Removed build spec for " + XALint.BUILDER_ID);
            } else if (EXT_TOOL_BUILDER.equals(buildName)) {
                final String launchItem = (String) commands[i].getArguments().get(LAUNCH_KEY);
                if (launchItem != null && (launchItem.indexOf(XALint.BUILDER_ID) > 0)) {
                    logger.finest(className, methodName, "Removed external tool launch spec for " + XALint.BUILDER_ID);
                } else {
                    savedCommands.add(commands[i]);
                }
            } else {
                savedCommands.add(commands[i]);
            }
        }
        ICommand[] newCommands = new ICommand[savedCommands.size()];
        if (savedCommands.size() > 0) {
            newCommands = savedCommands.toArray(newCommands);
        }
        description.setBuildSpec(newCommands);
        getProject().setDescription(description, null);
        logger.exiting(className, methodName, "Removed " + (commands.length - savedCommands.size()) + " commands");
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(final IProject project) {
        this.project = project;
    }
}
