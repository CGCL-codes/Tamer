package org.antdepo.cli;

import org.antdepo.AntdepoException;
import org.antdepo.common.*;
import org.antdepo.common.context.ICommandContext;
import org.antdepo.common.context.IExecutionContext;
import org.antdepo.common.context.IObjectContext;
import org.antdepo.tasks.controller.ActionResult;
import org.antdepo.tasks.controller.BaseActionResult;
import org.antdepo.tasks.controller.DispatchAction;
import org.antdepo.utils.CommandLogger;
import org.apache.log4j.Category;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import java.io.InputStream;

/**
 * A DispatchAction implmentation that provides the standard template methods to lookup handlers,
 * configure the project, and execute, returning an {@link ActionResult}.
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 21, 2005
 * Time: 9:12:57 AM
 */
public abstract class AbstractCmdrMainDispatchAction implements DispatchAction {

    static final Category logger = Category.getInstance(AbstractCmdrMainDispatchAction.class);

    private static final String[] BOOTSTRAP_CMDS = { "Install-Module", "Install", "Register" };

    protected final IDepotMgr depotResourceMgr;

    protected final IExecutionContext action;

    protected final CommandLogger cmdLogger;

    private final Framework framework;

    public AbstractCmdrMainDispatchAction(final Framework framework, final IExecutionContext ea, final IDepotMgr depotResourceMgr, final CommandLogger cmdLogger) {
        this.framework = framework;
        this.action = ea;
        this.depotResourceMgr = depotResourceMgr;
        this.cmdLogger = cmdLogger;
    }

    /**
     * Given an object, lookup up handler
     *
     * @param obj
     * @return
     */
    protected abstract CmdHandler lookupHandler(DepotObject obj);

    /**
     * Lookup up handler assuming a Handler level context (ie, no object)
     *
     * @param commandContext
     * @return
     */
    protected abstract CmdHandler lookupHandler(ICommandContext commandContext);

    /**
     * Lookups object given object context info
     *
     * @param objectContext Object context info
     * @return An instance of DepotObject.
     */
    protected DepotObject lookupObject(final IObjectContext objectContext) {
        final IObject iobj = objectContext.asIObject();
        if (DepotObject.exists(iobj, depotResourceMgr)) {
            return DepotObject.create(iobj, depotResourceMgr);
        } else {
            if (depotResourceMgr.isConfiguredObjectDeploymentsCheck(iobj.getDepot()) && !depotResourceMgr.isRegistered(iobj)) {
                throw new AntdepoException("object not registered as an object-deployment");
            }
            if (isBootstrapCommand(action.getCommandContext().getCommand())) {
                return DepotObject.create(iobj, false, depotResourceMgr);
            } else {
                throw new AntdepoException("object not found: " + objectContext);
            }
        }
    }

    /**
     * Kicks off the DispatchAction sequence.
     * Looks up the DepotObject if needed, gets {@link CmdHandler}, creates the {@link AntProject},
     * and calls {@link #execute(AntProject)}.
     *
     * @return
     */
    public ActionResult perform() {
        logger.debug("DEBUG: inside perform...");
        logger.debug("DEBUG: ea.toString:" + action.toString());
        final AntProject ap;
        if (action.isObjectContext()) {
            logger.debug("DEBUG: is object context");
            final DepotObject obj = lookupObject(action.getObjectContext());
            logger.debug("retrieved object: " + obj.getName() + " . looking up handler...");
            final CmdHandler handler = lookupHandler(obj);
            logger.debug("got handler: " + handler.getName() + ". creating AntProject...");
            ap = AntProject.create(obj, handler, framework);
            logger.debug("Created antproject ");
        } else if (action.isCommandContext()) {
            logger.debug("DEBUG: isCommandContext");
            final CmdHandler handler = lookupHandler(action.getCommandContext());
            ap = AntProject.create(handler, framework);
        } else {
            throw new AntdepoException("Can't execute command for specified context and command");
        }
        logger.debug("DEBUG: calling execute method...");
        return execute(ap);
    }

    /**
     * Executes the {@link AntProject} returning the {@link ActionResult}.
     * AntProject already has been loaded with its handler.
     * Calls the {@link #addBuildListeners(Project)}, {@link #configureIOStreams(Project)},
     * {@link #addInputHandlers(Project)} before executing the {@link Project#executeTarget(String)}
     *
     * @param ap
     * @return
     */
    protected ActionResult execute(final AntProject ap) {
        logger.info("inside execute()");
        final BaseActionResult result = BaseActionResult.create("output", "error", true);
        ap.setArg(action.getArg());
        final Project project = ap.getProject();
        addBuildListeners(project);
        final InputStream in = configureIOStreams(project);
        addInputHandlers(project);
        ap.parse();
        project.fireBuildStarted();
        Throwable error = null;
        try {
            logger.debug("calling ap.execute()");
            ap.execute();
            result.setSuccessful(true);
        } catch (BuildException be) {
            error = be;
            result.setBuildException(be);
        } catch (Throwable exc) {
            error = exc;
            logger.error("Caught throwable: " + exc.getClass().getName());
        } finally {
            project.fireBuildFinished(error);
            System.setOut(System.out);
            System.setErr(System.err);
            System.setIn(in);
        }
        return result;
    }

    /**
     * Adds needed {@link org.apache.tools.ant.input.InputHandler} objects
     *
     * @param project
     */
    protected abstract void addInputHandlers(final Project project);

    /**
     * Configures the IO streams
     *
     * @param project
     * @return
     */
    protected abstract InputStream configureIOStreams(Project project);

    /**
     * Adds needed {@link org.apache.tools.ant.BuildListener} objects
     *
     * @param project
     */
    protected abstract void addBuildListeners(Project project);

    /**
     * getter to the cmdLogger
     *
     * @return
     */
    protected CommandLogger getCommandLogger() {
        return cmdLogger;
    }

    protected boolean isBootstrapCommand(final String command) {
        for (int i = 0; i < BOOTSTRAP_CMDS.length; i++) {
            if (BOOTSTRAP_CMDS[i].equals(command)) {
                logger.debug("is bootstrap command: " + command);
                return true;
            }
        }
        return false;
    }

    public Framework getFramework() {
        return framework;
    }
}
