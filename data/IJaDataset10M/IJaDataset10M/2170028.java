package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.runner.Constants.PASSWORD_LONG_OPT;
import static org.nakedobjects.runtime.runner.Constants.PASSWORD_OPT;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.runner.BootPrinter;
import org.nakedobjects.runtime.runner.Constants;
import org.nakedobjects.runtime.runner.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerPassword extends OptionHandlerAbstract {

    private String password;

    public OptionHandlerPassword() {
        super();
    }

    @SuppressWarnings("static-access")
    public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("password").hasArg().withLongOpt(PASSWORD_LONG_OPT).withDescription("password to automatically log in with").create(PASSWORD_OPT);
        options.addOption(option);
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        password = commandLine.getOptionValue(Constants.PASSWORD_OPT);
        return true;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        configurationBuilder.add(SystemConstants.PASSWORD_KEY, password);
    }

    public String getPassword() {
        return password;
    }
}
