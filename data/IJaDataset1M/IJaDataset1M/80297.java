package protoj.lang.command;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import protoj.lang.Command;
import protoj.lang.CommandStore;
import protoj.lang.internal.ProtoProject;

/**
 * The command responsible for performing a release of protoj to the google code
 * website and sourceforge maven repository.
 * 
 * @author Ashley Williams
 * 
 */
public final class ReleaseCommand {

    private final class Body implements Runnable {

        private final ProtoProject project;

        private Body(ProtoProject project) {
            this.project = project;
        }

        public void run() {
            OptionSet options = delegate.getOptions();
            if (!options.has(gcUserOption)) {
                String message = "incorrect arguments: the release command requires a googlecode username";
                delegate.throwBadOptionsException(message);
            }
            if (!options.has(gcPasswordOption)) {
                String message = "incorrect arguments: the release command requires a googlecode password";
                delegate.throwBadOptionsException(message);
            }
            if (!options.has(sfUserOption)) {
                String message = "incorrect arguments: the release command requires a sourceforge username";
                delegate.throwBadOptionsException(message);
            }
            String gcUser = options.valueOf(gcUserOption);
            String gcPassword = options.valueOf(gcPasswordOption);
            String sfUser = options.valueOf(sfUserOption);
            if (options.has(gcOnlyOption)) {
                project.getReleaseFeature().uploadToGoogleCode(gcUser, gcPassword);
            } else if (options.has(sfOnlyOption)) {
                project.getReleaseFeature().uploadToMaven(sfUser);
            } else {
                project.getReleaseFeature().release(gcUser, gcPassword, sfUser);
            }
        }
    }

    /**
	 * Provides the basic command functionality.
	 */
    private Command delegate;

    private OptionSpec<String> gcUserOption;

    private OptionSpec<String> gcPasswordOption;

    private OptionSpec<String> sfUserOption;

    private OptionSpecBuilder gcOnlyOption;

    private OptionSpecBuilder sfOnlyOption;

    public ReleaseCommand(final ProtoProject project, String mainClass) {
        CommandStore store = project.getDelegate().getCommandStore();
        delegate = store.addCommand("release", "64m", new Body(project));
        delegate.initHelpResource("/protoj/language/english/release.txt");
        OptionParser parser = delegate.getParser();
        gcUserOption = parser.accepts(getGcuserOption()).withRequiredArg();
        gcPasswordOption = parser.accepts(getGcpasswordOption()).withRequiredArg();
        sfUserOption = parser.accepts(getSfuserOption()).withRequiredArg();
        gcOnlyOption = parser.accepts(getGconlyOption());
        sfOnlyOption = parser.accepts(getSfonlyOption());
    }

    public String getSfonlyOption() {
        return "sfonly";
    }

    public String getGconlyOption() {
        return "gconly";
    }

    public String getSfuserOption() {
        return "sfuser";
    }

    public String getGcpasswordOption() {
        return "gcpassword";
    }

    public String getGcuserOption() {
        return "gcuser";
    }

    public Command getDelegate() {
        return delegate;
    }
}
