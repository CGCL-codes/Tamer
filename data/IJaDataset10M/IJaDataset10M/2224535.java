package org.antdepo.tasks.controller.node;

import org.antdepo.common.Framework;
import org.antdepo.common.ICommand;
import org.antdepo.common.NodeDesc;
import org.antdepo.common.context.IExecutionContext;
import org.apache.tools.ant.Project;
import java.util.Collection;
import java.util.Iterator;

/**
 * Executes Module context commands to the nodes the nodes exist.
 */
public class ModuleCommandProxyDispatcher extends ActionProxyDispatcher {

    /**
     * Factory method. Calls base constructor.
     *
     * @param action         Execution context
     * @param project        Execution project
     * @return new ModuleCommandProxyDispatcher
     */
    public static IActionProxyDispatcher create(final IExecutionContext action, final Project project, final Framework framework) {
        return new ModuleCommandProxyDispatcher(action, project, framework);
    }

    /**
     * @param action         Execution context
     * @param project        Execution project
     */
    protected ModuleCommandProxyDispatcher(final IExecutionContext action, final Project project, final Framework framework) {
        super(action, project, framework);
    }

    /**
     * looks up nodes based on command context
     *
     * @return list of nodes
     */
    protected Collection lookupNodes() {
        return lookup(getAction().getCommandContext().asICommand());
    }
}
