package org.antdepo.cli;

import org.antdepo.Constants;
import org.antdepo.cli.depotsetup.CreateAction;
import org.antdepo.cli.depotsetup.DeployAction;
import org.antdepo.cli.depotsetup.DepotSetupException;
import org.antdepo.cli.depotsetup.DepotSetupInvalidArgumentsException;
import org.antdepo.cli.depotsetup.RemoveAction;
import org.antdepo.cli.depotsetup.UndeployAction;
import org.antdepo.common.Framework;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Main class for creating new depots. This is called via depot-setup shell command.
 */
public class ADDepotSetupMain implements ActionMaker, CLITool {

    public static final String ACTION_CREATE = "create";

    public static final String ACTION_DEPLOY = "deploy";

    public static final String ACTION_INSTALL = "install";

    public static final String ACTION_REMOVE = "remove";

    public static final String ACTION_PURGE = "purge";

    /**
     * control property to overwrite existing installations
     */
    boolean overwrite;

    /**
     * Controls output verbosity
     */
    boolean verbose;

    /**
     * Reference to command line params
     */
    protected CommandLine cli;

    /**
     * reference to the command line {@link org.apache.commons.cli.Options} instance.
     */
    protected static final Options options = new Options();

    /**
     * Add the commandline options common to any command
     */
    static {
        options.addOption("h", "help", false, "print this message");
        options.addOption("n", "name", true, "object name or pattern used by install/purge actions");
        options.addOption("p", "project", true, "project depot name");
        options.addOption("a", "action", true, "action to run {create | install | remove | purge}");
        options.addOption("v", "verbose", false, "verbose messages");
        options.addOption("b", "buildfile", true, "setup ant buildfile to run");
        options.addOption("M", "packagedmoduledir", true, "packaged module directory");
        options.addOption("L", "depotmoduledir", true, "depot module directory");
        options.addOption("D", "install", false, "run install action.");
        options.addOption("f", "file", true, "deployments.properties file");
        options.addOption("S", "strict", false, "strictly use the registration info from the depot deployments.properties");
    }

    public ADDepotSetupMain() {
        String antdepo_base = Constants.getSystemAntdepoBase();
        framework = Framework.getInstance(antdepo_base);
        PropertyConfigurator.configure(new File(new File(framework.getBaseDir(), "etc"), "log4j.properties").getAbsolutePath());
    }

    /**
     * Reference to the framework instance
     */
    private final Framework framework;

    /**
     * Creates an instance and executes {@link #run(String[])}.
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final ADDepotSetupMain c = new ADDepotSetupMain();
        c.run(args);
    }

    /**
     * Runs the initArgs and go methods.
     *
     * @param args Command line arg vector
     */
    public final void run(final String[] args) {
        int exitCode = 1;
        try {
            parseArgs(args);
            executeAction();
            exitCode = 0;
        } catch (Throwable t) {
            if (null == t.getMessage() || verbose || "true".equals(System.getProperty("antdepo.traceExceptions"))) {
                t.printStackTrace();
            } else {
                error(t);
            }
        }
        exit(exitCode);
    }

    /**
     * Calls the exit method
     *
     * @param code return code to exit with
     */
    public void exit(final int code) {
        System.exit(code);
    }

    /**
     * prints usage info
     */
    public void help() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "depot-setup [options]", "options:", options, "Examples:\n" + "\tdepot-setup -p depot; # create depot\n" + "\tdepot-setup -p depot --action create; # same as above\n" + "\tdepot-setup -p depot --action deploy; # deprecated: use install\n" + "\tdepot-setup -p depot --action install; # Install objects and their modules in depot\n" + "\tdepot-setup -p depot -L /path/to/depot-module-libdir; # create depot but use specified dir for modules\n" + "\tdepot-setup -p depot --action purge ; # purges all objects in depot\n" + "\n");
    }

    /**
     * Executes the setup helper actions
     *
     * @throws DepotSetupException thrown if action failed
     */
    public void executeAction() throws DepotSetupException {
        final String actionName;
        if (cli.hasOption('a')) {
            String optAction = cli.getOptionValue('a');
            if (ACTION_DEPLOY.equals(optAction)) {
                optAction = ACTION_INSTALL;
                warn("deploy action name deprecated. Use 'install'");
            }
            actionName = optAction;
        } else if (cli.hasOption('D')) {
            actionName = ACTION_INSTALL;
        } else {
            actionName = ACTION_CREATE;
        }
        final Action action = createAction(actionName);
        try {
            action.exec();
        } catch (Throwable t) {
            throw new DepotSetupException(t);
        }
    }

    /**
     * processes the command line input
     *
     * @param args command line arg vector
     */
    public CommandLine parseArgs(final String[] args) throws DepotSetupException {
        final CommandLineParser parser = new PosixParser();
        try {
            cli = parser.parse(options, args);
        } catch (ParseException e) {
            help();
            throw new DepotSetupException(e);
        }
        initArgs();
        return cli;
    }

    /**
     * ActionMaker interface implementations
     */
    public void initArgs() {
        if (null == cli) {
            throw new IllegalStateException("parseArgs must be called to instantiate the cli");
        }
        if (cli.hasOption("h")) {
            help();
            exit(1);
        }
        if (cli.hasOption('v')) {
            verbose = true;
        }
        if (cli.hasOption("S")) {
            verbose("strict flag set. will use registration info from deployments.properties file");
        }
        if (cli.hasOption('n')) {
            try {
                Pattern.compile(cli.getOptionValue('n'));
            } catch (PatternSyntaxException e) {
                throw new DepotSetupException("--name argument is not a valid name or expression: \"" + cli.getOptionValue('n') + "\"", e);
            }
        }
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public Action createAction(final String actionName) {
        try {
            if (ACTION_CREATE.equals(actionName)) {
                return new CreateAction(this, framework, cli);
            } else if (ACTION_INSTALL.equals(actionName) || ACTION_DEPLOY.equals(actionName)) {
                return new DeployAction(this, framework, cli);
            } else if (ACTION_REMOVE.equals(actionName)) {
                return new RemoveAction(this, framework, cli);
            } else if (ACTION_PURGE.equals(actionName)) {
                return new UndeployAction(this, framework, cli);
            } else {
                throw new IllegalArgumentException("unknown action name: " + actionName);
            }
        } catch (DepotSetupInvalidArgumentsException e) {
            help();
            throw e;
        }
    }

    /**
     * Interfaces for the CLIToolLogger
     */
    public void error(final String output) {
        System.err.println("error: " + output);
    }

    public void warn(final String output) {
        System.err.println("warn: " + output);
    }

    private void error(final Throwable t) {
        final StringBuffer sb = new StringBuffer();
        sb.append(t.getMessage());
        if (t.getCause() != null && t.getCause().getMessage() != null) {
            sb.append(": ");
            sb.append(t.getCause().getMessage());
        }
        log(sb.toString());
    }

    public void log(final String message) {
        System.out.println(message);
    }

    public void verbose(final String message) {
        if (verbose) {
            log(message);
        }
    }
}
