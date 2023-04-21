package protoj.lang.command;

import java.io.File;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import protoj.lang.StandardProject;
import protoj.lang.config.ConfigFeature;

/**
 * Command line access to the {@link ScpconfFeature} pojo. See the scpconf.txt
 * file for help.
 * 
 * @author Ashley Williams
 * 
 */
public final class ScpconfCommand {

    public final class Body implements Runnable {

        public void run() {
            Command delegate = getDelegate();
            OptionSet options = delegate.getOptions();
            if (!options.has(nameOption)) {
                String hint = "the name option is mandatory";
                delegate.throwBadOptionsException(hint);
            }
            String name = options.valueOf(nameOption);
            File keyfile = options.valueOf(keyfileOption);
            String passtext = options.valueOf(passtextOption);
            boolean interpolate = options.has(interpolateOption);
            ConfigFeature feature = project.getConfigFeature();
            if (options.has(cleanOption)) {
                feature.relativeRemoteClean(name, keyfile, passtext);
            } else {
                feature.relativeRemoteOverlay(name, interpolate, keyfile, passtext);
            }
        }
    }

    /**
	 * Provides the basic command functionality.
	 */
    private Command delegate;

    private OptionSpec<?> interpolateOption;

    private OptionSpec<?> cleanOption;

    private OptionSpec<String> passtextOption;

    private OptionSpec<File> keyfileOption;

    private final StandardProject project;

    private OptionSpec<String> nameOption;

    public ScpconfCommand(StandardProject project) {
        this.project = project;
        CommandStore store = project.getCommandStore();
        delegate = store.addCommand("scpconf", "32m", new Body());
        delegate.initHelpResource("/protoj-common/language/english/scpconf.txt");
        nameOption = delegate.getParser().accepts(getNameOption()).withRequiredArg();
        interpolateOption = delegate.getParser().accepts(getInterpolateOption());
        cleanOption = delegate.getParser().accepts(getCleanOption());
        passtextOption = delegate.getParser().accepts(getPasstextOption()).withRequiredArg();
        keyfileOption = delegate.getParser().accepts(getKeyfileOption()).withRequiredArg().ofType(File.class);
    }

    public String getNameOption() {
        return "name";
    }

    public String getInterpolateOption() {
        return "interpolate";
    }

    public String getCleanOption() {
        return "clean";
    }

    public String getPasstextOption() {
        return "passtext";
    }

    public String getKeyfileOption() {
        return "keyfile";
    }

    public Command getDelegate() {
        return delegate;
    }
}
