package org.antdepo.tasks.controller.node;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.antdepo.common.Framework;
import org.antdepo.common.INodeDesc;
import org.antdepo.common.NodeDesc;
import org.antdepo.common.context.DepotContext;
import org.antdepo.common.context.IExecutionContext;
import org.antdepo.common.context.ObjectContext;
import org.antdepo.tools.AntdepoTest;
import org.antdepo.types.Command;
import org.antdepo.types.controller.ExecuteAction;
import org.apache.tools.ant.Project;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Dec 30, 2005
 * Time: 9:10:34 AM
 */
public class TestRemoteCommand extends AntdepoTest {

    static final String CONTEXT_DEPOT = "TestRemoteCommand";

    static final String CONTEXT_TYPE = "TypeA";

    static final String CONTEXT_OBJ = "aTypeInstance";

    static final String CONTEXT_CMD = "aCommand";

    static final String CONTEXT_MOD = "TypeAm";

    static final String CONTEXT_USER = "rubble";

    private final String remoteNode;

    private final Command command;

    private final ObjectContext context;

    private final ExecuteAction action;

    public TestRemoteCommand(final String name) {
        super(name);
        super.setUp();
        remoteNode = getFrameworkInstance().getFrameworkNodeName();
        command = (Command) Command.create(CONTEXT_CMD, null);
        command.setDepot(CONTEXT_DEPOT);
        assertTrue(command.isCommandContext());
        context = ObjectContext.create(CONTEXT_OBJ, CONTEXT_TYPE, DepotContext.create(CONTEXT_DEPOT));
        assertTrue(context.isObjectContext());
        action = ExecuteAction.create(getFrameworkInstance(), context, command);
        action.setUsername(CONTEXT_USER);
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestRemoteCommand.class);
    }

    public void testCreate() {
        RemoteCommand.defaultProxyClass = SshCommand.class;
        Project project = new Project();
        getFrameworkInstance().configureProject(project);
        ICommandProxy cmd = RemoteCommand.createCommandProxy(action, NodeDesc.create(remoteNode, getFrameworkInstance().getFrameworkNodeName()), project, getFrameworkInstance());
        assertNotNull(cmd);
        assertEquals("wrong proxy class: " + cmd.getClass().getName(), SshCommand.class.getName(), cmd.getClass().getName());
        RemoteCommand.defaultProxyClass = this.getClass();
        Project project1 = new Project();
        getFrameworkInstance().configureProject(project1);
        Object prxy = RemoteCommand.createCommandProxy(action, NodeDesc.create(remoteNode, getFrameworkInstance().getFrameworkNodeName()), project1, getFrameworkInstance());
        assertTrue("wrong proxy class: " + cmd.getClass().getName(), prxy instanceof ICommandProxy);
    }

    public static ICommandProxy create(final IExecutionContext action, final INodeDesc node, final Project project, final Framework framework) {
        return new ICommandProxy() {

            /**
             * getter to the {@link org.antdepo.types.controller.ExecuteAction}
             *
             * @return the action the proxy was configured with
             */
            public IExecutionContext getAction() {
                return action;
            }

            /**
             * Method to activate the proxy to perform the configured action
             */
            public void perform() {
            }
        };
    }
}
