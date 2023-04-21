package protoj.lang.internal;

import java.util.List;
import joptsimple.OptionSet;
import protoj.core.Command;
import protoj.core.CommandStore;
import protoj.core.ProjectLayout;
import protoj.core.PropertyInfo;
import protoj.lang.InfoFeature;
import protoj.lang.StandardProject;

/**
 * The command responsible for reporting help information to the console. See
 * {@link InfoFeature}.
 * 
 * @author Ashley Williams
 * 
 */
public final class HelpCommand {

    private final class Body implements Runnable {

        public void run() {
            OptionSet options = delegate.getOptions();
            List<String> args = options.nonOptionArguments();
            String name = args.size() > 0 ? args.get(0) : null;
            Command command = project.getCommandStore().getCommand(name);
            PropertyInfo property = project.getPropertyStore().getInfo(name);
            if ((command != null)) {
                project.getInfoFeature().reportCommandHelp(command);
            } else if ((property != null)) {
                project.getInfoFeature().reportPropertyHelp(property);
            } else {
                project.getInfoFeature().reportProjectHelp();
            }
        }
    }

    /**
	 * Provides the basic command functionality.
	 */
    private Command delegate;

    /**
	 * The parent of this command.
	 */
    private final StandardProject project;

    public HelpCommand(StandardProject project) {
        this.project = project;
        ProjectLayout layout = project.getLayout();
        CommandStore store = project.getCommandStore();
        StringBuilder description = new StringBuilder();
        description.append("Reports help information to the console.");
        description.append("\nIf no argument is provided then general project information will be reported.");
        description.append("\nTo report help on a particular property or command, provide its name as ");
        description.append("\nan argument.");
        description.append("\n\nExample: jonny$ ./");
        description.append(layout.getScriptName());
        description.append(" help");
        description.append("\n\nExample: jonny$ ./");
        description.append(layout.getScriptName());
        description.append(" \"help tar\"");
        delegate = store.addCommand("help", description.toString(), "16m", new Body());
        delegate.initAliases("-help", "--help", "h", "-h", "--h", "?");
    }

    public Command getDelegate() {
        return delegate;
    }
}
