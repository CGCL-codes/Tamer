package protoj.system;

import joptsimple.OptionSet;
import protoj.shell.internal.ConfigureCommand;

/**
 * The body that gets plugged into the parent command. See
 * {@link ConfigureCommand} for more information.
 * 
 * @author Ashley Williams
 * 
 */
public final class ConfigureBody implements Runnable {

    private final StandardProject project;

    private final ConfigureCommand command;

    public ConfigureBody(StandardProject project, ConfigureCommand command) {
        this.project = project;
        this.command = command;
    }

    public void run() {
        OptionSet options = command.getDelegate().getOptions();
        boolean hasName = options.has(command.getName());
        if (!hasName) {
            project.terminateApplication("no profile name: please specify the name of a profile when calling configure");
        }
        String name = options.valueOf(command.getName());
        ConfigureFeature feature = project.getConfigureFeature();
        if (options.has(command.getUndo())) {
            feature.undo(name);
        } else {
            feature.configure(name);
        }
    }
}
