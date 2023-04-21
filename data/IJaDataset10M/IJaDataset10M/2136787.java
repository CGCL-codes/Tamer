package org.nakedobjects.runtime.options.standard;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.NotFoundPolicy;
import org.nakedobjects.runtime.runner.BootPrinter;
import org.nakedobjects.runtime.runner.Constants;
import org.nakedobjects.runtime.runner.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;
import static org.nakedobjects.runtime.runner.Constants.TYPE_LONG_OPT;
import static org.nakedobjects.runtime.runner.Constants.TYPE_OPT;

public abstract class OptionHandlerDeploymentType extends OptionHandlerAbstract {

    private final DeploymentType defaultDeploymentType;

    private final String types;

    private DeploymentType deploymentType;

    public OptionHandlerDeploymentType(final DeploymentType defaultDeploymentType, String types) {
        this.defaultDeploymentType = defaultDeploymentType;
        this.types = types;
    }

    @SuppressWarnings("static-access")
    public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("name").hasArg().withLongOpt(TYPE_LONG_OPT).withDescription("deployment type: " + types).create(TYPE_OPT);
        options.addOption(option);
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        String deploymentTypeName = commandLine.getOptionValue(Constants.TYPE_OPT);
        if (deploymentTypeName == null) {
            deploymentType = defaultDeploymentType;
            return true;
        }
        deploymentType = DeploymentType.lookup(deploymentTypeName.toUpperCase());
        if (deploymentType != null) {
            return true;
        }
        bootPrinter.printErrorAndHelp(options, "Unable to determine deployment type");
        return false;
    }

    /**
     * Only populated after {@link #handle(CommandLine, BootPrinter, Options)}.
     */
    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        String type = deploymentType.nameLowerCase();
        configurationBuilder.addConfigurationResource(type + ".properties", NotFoundPolicy.CONTINUE);
        configurationBuilder.add(SystemConstants.DEPLOYMENT_TYPE_KEY, deploymentType.name());
    }
}
