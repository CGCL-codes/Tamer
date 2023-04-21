package org.nakedobjects.nof.boot;

import org.nakedobjects.nof.core.conf.DefaultConfigurationLoader;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;

public class ArgumentsParser {

    private BootPrinter printer;

    private CommandLine cmd;

    public ArgumentsParser(final BootPrinter printer) {
        this.printer = printer;
    }

    public Level loggingLevel(final String[] args) {
        Level level = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-" + Arguments.DEBUG_OPT)) {
                level = Level.DEBUG;
                break;
            } else if (args[i].equals("-" + Arguments.QUIET_OPT)) {
                level = Level.ERROR;
                break;
            } else if (args[i].equals("-" + Arguments.VERBOSE_OPT)) {
                level = Level.INFO;
                break;
            }
        }
        return level;
    }

    public CommandLine parse(Options options, String[] args) {
        final CommandLineParser parser = new BasicParser();
        this.cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            printer.printErrorMessage(e.getMessage());
            printer.printHelp(options);
            return null;
        }
        if (cmd.hasOption(Arguments.HELP_OPT)) {
            printer.printHelp(options);
            return null;
        }
        if (cmd.hasOption(Arguments.VERSION_OPT)) {
            printer.printVersion();
            return null;
        }
        if (cmd.hasOption(Arguments.DIAGNOSTICS_OPT)) {
            printer.printDiagnostics();
            return null;
        }
        return cmd;
    }

    public String getMode() {
        ensureParsed();
        String mode = cmd.getOptionValue(Arguments.TYPE_OPT);
        return mode == null ? Arguments.TYPE_PROTOTYPE : mode.toLowerCase();
    }

    public boolean isExploration() {
        return Arguments.TYPE_EXPLORATION.equals(getMode());
    }

    public boolean isPrototype() {
        return Arguments.TYPE_PROTOTYPE.equals(getMode());
    }

    public boolean isClient() {
        return Arguments.TYPE_CLIENT.equals(getMode());
    }

    public boolean isServer() {
        return Arguments.TYPE_SERVER.equals(getMode());
    }

    public boolean isStandalone() {
        return Arguments.TYPE_STANDALONE.equals(getMode());
    }

    public String getConnection() {
        return cmd.getOptionValue(Arguments.CONNECTION_OPT);
    }

    public String getProgModel() {
        return cmd.getOptionValue(Arguments.PROGMODEL_OPT);
    }

    public DefaultConfigurationLoader configurationLoader() {
        final DefaultConfigurationLoader configurationLoader = new DefaultConfigurationLoader();
        final String config = cmd.getOptionValue(Arguments.CONFIGURATION_OPT);
        if (config != null) {
            configurationLoader.addConfigurationFile(config, true);
        }
        String mode = getMode();
        if (mode != null) {
            configurationLoader.addConfigurationFile(mode + ".properties", false);
        }
        return configurationLoader;
    }

    public String getPersistor() {
        ensureParsed();
        return cmd.getOptionValue(Arguments.PERSISTOR_OPT);
    }

    public String getUser() {
        ensureParsed();
        return cmd.getOptionValue(Arguments.USER_OPT);
    }

    public String getPassword() {
        ensureParsed();
        return cmd.getOptionValue(Arguments.PASSWORD_OPT);
    }

    public String getViewer() {
        ensureParsed();
        return cmd.getOptionValue(Arguments.VIEWER_OPT);
    }

    public String getFixture() {
        ensureParsed();
        return cmd.getOptionValue(Arguments.FIXTURE_OPT);
    }

    public boolean isSplash() {
        ensureParsed();
        boolean noSplash = cmd.hasOption('s');
        return !noSplash;
    }

    public String[] getAdditionalProperties() {
        ensureParsed();
        return cmd.getOptionValues(Arguments.ADDITIONAL_PROPERTY);
    }

    private void ensureParsed() {
        if (cmd == null) {
            throw new IllegalStateException("Call parse() first");
        }
    }
}
