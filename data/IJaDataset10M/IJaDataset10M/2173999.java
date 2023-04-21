package com.controltier.ctl.tasks.controller;

import com.controltier.ctl.Constants;
import com.controltier.ctl.common.AntProject;
import com.controltier.ctl.common.CmdHandler;
import com.controltier.ctl.common.Framework;
import com.controltier.ctl.common.context.IExecutionContext;
import com.controltier.ctl.common.context.IObjectContext;
import com.controltier.ctl.common.context.ICommandContext;
import com.controltier.ctl.utils.CtlArgUtil;
import org.apache.log4j.Category;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.types.Commandline;

/**
 * Wrapper around {@link ExecTask} to execute handlers by exec'ing the command shell with the needed args.
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 14, 2005
 * Time: 6:18:54 PM
 */
public class ExecExecutionStrategy extends BaseExecutionStrategy {

    public static final Category logger = Category.getInstance(ExecExecutionStrategy.class);

    private ExecTask exec;

    public Task getTask() {
        return exec;
    }

    private ExecExecutionStrategy(final IExecutionContext ea, ActionResult result, final CmdHandler handler, final Project project, final Framework framework) {
        super(ea, result, handler, project, framework);
    }

    public static ExecutionStrategy create(final IExecutionContext ea, ActionResult result, final CmdHandler handler, final Project project, final Framework framework) {
        return new ExecExecutionStrategy(ea, result, handler, project, framework);
    }

    /**
     * Creates an {@link ExecTask} to the CmdrMain class, configuring CLOPTS appropriately depending on
     * context; eg, if object context uses -p,-t,-o,-c, if handler context, -m,-c
     *
     * @return
     */
    public Task createTask() {
        final AntProject ap = AntProject.create(getProject(), getExecuteAction().getObjectContext().asIObject(), getCmdHandler(), getFramework());
        ap.parse();
        setProject(ap.getProject());
        exec = (ExecTask) getProject().createTask(Constants.EXECUTEACTION_STRATEGY_EXEC);
        exec.setResultProperty(result.getResultproperty());
        if (getProject().getProperties().containsKey("modules.cmdr.shell")) {
            exec.setExecutable(getProject().getProperty("modules.cmdr.shell"));
        } else {
            logger.debug("\"modules.cmdr.shell\" property not set. Defaulting to \"cmdr\"");
            exec.setExecutable(Constants.CMDR_SHELL);
        }
        final ArgumentGenerator gen = new ArgumentGenerator(exec, getExecuteAction().getObjectContext(), getExecuteAction().getCommandContext());
        final Commandline.Argument arg = gen.createArgument();
        logger.info("arg line info: " + gen.toString(arg));
        if (null != result.getOutputproperty()) {
            exec.setOutputproperty(result.getOutputproperty());
        }
        if (null != result.getErrorproperty()) {
            exec.setErrorProperty(result.getErrorproperty());
        }
        exec.setFailIfExecutionFails(result.isFailonerror());
        exec.setFailonerror(result.isFailonerror());
        return exec;
    }

    public ActionResult execute() {
        return super.execute();
    }

    class ArgumentGenerator {

        private ExecTask task;

        private final IObjectContext object;

        private final ICommandContext command;

        protected ArgumentGenerator(final ExecTask task, final IObjectContext object, final ICommandContext command) {
            this.task = task;
            this.object = object;
            this.command = command;
        }

        /**
         * Creates an {@link org.apache.tools.ant.types.Commandline.Argument} object given context.
         * Used for configuring CLOPTS appropriately depending on
         * context; eg, if object context uses -p,-t,-o,-c, if handler context, -m,-c*
         *
         * @return
         */
        protected Commandline.Argument createArgument() {
            final Commandline.Argument arg = task.createArg();
            final StringBuffer sb = new StringBuffer(CtlArgUtil.buildAsString(object, command));
            arg.setLine(sb.toString());
            return arg;
        }

        /**
         * Prints out components of arg
         *
         * @param arg
         * @return
         */
        public String toString(final Commandline.Argument arg) {
            final StringBuffer sb = new StringBuffer();
            sb.append("Argument{");
            for (int i = 0; i < arg.getParts().length; i++) {
                sb.append(arg.getParts()[i] + " ");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
