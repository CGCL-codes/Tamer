package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.runner.Constants.VERSION_OPT;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.runner.BootPrinter;
import org.nakedobjects.runtime.runner.Constants;
import org.nakedobjects.runtime.runner.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerVersion extends OptionHandlerAbstract {

    public OptionHandlerVersion() {
        super();
    }

    public void addOption(Options options) {
        options.addOption(VERSION_OPT, false, "print version information");
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        if (commandLine.hasOption(Constants.VERSION_OPT)) {
            bootPrinter.printVersion();
            return false;
        }
        return true;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
    }
}
