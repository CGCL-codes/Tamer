package org.nakedobjects.runtime.internal;

import static org.nakedobjects.runtime.boot.args.Constants.CONFIGURATION_LONG_OPT;
import static org.nakedobjects.runtime.boot.args.Constants.CONFIGURATION_OPT;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.NotFoundPolicy;
import org.nakedobjects.runtime.boot.args.Constants;
import org.nakedobjects.runtime.boot.printer.BootPrinter;
import org.nakedobjects.runtime.system.SystemConstants;

public class FlagHandlerConfiguration extends FlagHandlerAbstract {

    private String configurationResource;

    public FlagHandlerConfiguration() {
        super();
    }

    @SuppressWarnings("static-access")
    public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("config file").hasArg().withLongOpt(CONFIGURATION_LONG_OPT).withDescription("read in configuration file (as well as nakedobjects.properties)").create(CONFIGURATION_OPT);
        options.addOption(option);
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        configurationResource = commandLine.getOptionValue(Constants.CONFIGURATION_OPT);
        return true;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        if (configurationResource == null) {
            return;
        }
        configurationBuilder.addConfigurationResource(configurationResource, NotFoundPolicy.FAIL_FAST);
    }
}
