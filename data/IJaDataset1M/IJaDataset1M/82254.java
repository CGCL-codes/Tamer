package org.antdepo.cli.addeploy;

import org.antdepo.cli.CLIToolLogger;
import org.antdepo.common.*;
import org.antdepo.common.context.IDepotContext;
import org.antdepo.types.controller.Arg;
import org.apache.log4j.Category;

/**
 * A base class for making Deploy helpers
 */
public abstract class Deployer {

    static Category cat = Category.getInstance(Deployer.class.getName());

    /**
     * reference to the Deployments objects that contains deployment info
     */
    Deployments deployments;

    /**
     * Referene to the framework instance. 
     */
    final Framework framework;

    /**
     * Node info for this framework instance
     */
    final NodeDesc node;

    final IDepotContext depot;

    final CLIToolLogger logger;

    Deployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger) {
        if (null == framework) {
            throw new IllegalArgumentException("framework parameter is null");
        }
        this.framework = framework;
        node = NodeDesc.create(framework.getFrameworkNodeName(), framework.getFrameworkNodeName());
        if (null == depot) {
            throw new IllegalArgumentException("depot parameter null");
        }
        this.depot = depot;
        if (null == deployments) {
            throw new IllegalArgumentException("deployments parameter null");
        }
        this.deployments = deployments;
        this.logger = logger;
    }

    /**
     * Deploy the resource
     *
     * @throws DeployException
     */
    public abstract void deploy() throws DeployException;

    /**
     * Factor method creating a ModuleDeployer
     *
     * @param deployments
     * @param logger      log facility
     * @return a new ModuleDeployer
     */
    public static Deployer createModuleDeployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger) {
        return new ModuleDeployer(framework, depot, deployments, logger);
    }

    /**
     * Factory method creating an ObjectDeployer.
     *
     * @param deployments
     * @param logger      log facility
     * @param useStrict
     *
     * @return a new ObjectDeployer
     */
    public static Deployer createObjectDeployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger, final String namePattern, boolean useStrict) {
        return new ObjectDeployer(framework, depot, deployments, logger, namePattern, useStrict);
    }

    /**
     * Factory method creating an ObjectDeployer.
     *
     * @param deployments
     * @param logger      log facility
     *
     * @return a new ObjectDeployer
     */
    public static Deployer createObjectDeployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger) {
        return new ObjectDeployer(framework, depot, deployments, logger, null, false);
    }

    /**
     * Factory method creating an ObjectUndeployer.
     *
     * @param deployments
     * @param logger      log facility
     *
     * @return a new ObjectDeployer
     */
    public static Deployer createObjectUndeployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger) {
        return new ObjectUndeployer(framework, depot, deployments, logger);
    }

    /**
     * Factory method creating an ObjectUndeployer.
     *
     * @param deployments
     * @param logger      log facility
     *
     * @return a new ObjectDeployer
     */
    public static Deployer createObjectUndeployer(final Framework framework, final IDepotContext depot, final Deployments deployments, final CLIToolLogger logger, final String namePattern) {
        return new ObjectUndeployer(framework, depot, deployments, logger, namePattern, false);
    }

    /**
     * Generic method to run a Managed-Entity command for an object
     * @param iobj Targeted object
     * @param command Command name to run
     * @param arg Arg value
     * @throws DeployException  Exception thrown if command fails
     */
    protected void runObjectCommand(final IObject iobj, final String command, final Arg arg) throws DeployException {
        final DepotObject object;
        final CmdHandler handler;
        try {
            object = DepotObject.create(iobj, framework.getDepotResourceMgr());
            final CmdModule module = framework.getDepotResourceMgr().getModuleLookup().getCmdModule("Managed-Entity");
            handler = module.getCmdHandler(command);
            logger.verbose("executing " + command + " command for object: " + "(" + object.getEntityType() + ") " + object.getName());
            final AntProject ap = AntProject.create(object, handler, framework);
            if (null != arg) ap.setArg(arg);
            ap.parse();
            ap.execute();
        } catch (Throwable e) {
            throw new DeployException("failed executing command: " + e.getMessage(), e);
        }
    }

    protected void runObjectCommand(final IObject iobj, final String command) throws DeployException {
        runObjectCommand(iobj, command, null);
    }
}
