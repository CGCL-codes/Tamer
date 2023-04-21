package org.gcli.commands;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.gcli.Command;
import org.gcli.CommandLineInterface;
import org.gcli.CommandProvider;
import org.gcli.DefaultCommandLine;
import org.gcli.ExecutionContext;
import org.gcli.ExecutionContextProvider;
import org.gcli.Line;
import org.gcli.MapExecutionContextProvider;
import org.gcli.io.InputProvider;
import org.gcli.io.OutputProvider;
import org.gcli.io.StringOutput;

/**
 * Base class for unit tests for {@link Command}s.
 * 
 * @see HelpCommandTest
 * @see QuitCommandTest
 * @author Guy Nirpaz
 * 
 */
public abstract class AbstractSingleCommandTest extends TestCase {

    public static class MockCommandLineInterface implements CommandLineInterface {

        private CommandProvider commandProvider;

        private InputProvider inputProvider = null;

        public void destroy() {
        }

        public void execute() throws Exception {
        }

        public CommandProvider getCommandProvider() {
            return commandProvider;
        }

        public void init() {
        }

        public void setCommandProvider(CommandProvider commandProvider) {
            this.commandProvider = commandProvider;
        }

        public void setInputProvider(InputProvider inputProvider) {
            this.inputProvider = inputProvider;
        }

        public void setOutputProvider(OutputProvider outputProvider) {
        }
    }

    private StringOutput output;

    private ExecutionContext context;

    private Map<String, Command> commandMap;

    public AbstractSingleCommandTest() {
        super();
    }

    public AbstractSingleCommandTest(String name) {
        super(name);
    }

    protected void doExecute(String commandLine) throws Exception {
        final Line cl = new DefaultCommandLine(commandLine);
        final Command cmd = getCommandUnderTest();
        cmd.execute(cl, getExecutionContext(), getOutput());
        System.out.println(getOutput().toString());
    }

    protected CommandLineInterface getCli() {
        final CommandLineInterface cli = new MockCommandLineInterface();
        cli.setCommandProvider(new CommandProvider() {

            public Map<String, Command> getCommands() {
                return getCommandMap();
            }
        });
        return cli;
    }

    protected Map<String, Command> getCommandMap() {
        if (commandMap == null) {
            commandMap = new HashMap<String, Command>();
        }
        return commandMap;
    }

    protected abstract Command getCommandUnderTest();

    protected ExecutionContext getExecutionContext() {
        if (context == null) {
            final ExecutionContextProvider execCtxProvider = new MapExecutionContextProvider();
            context = execCtxProvider.getExecutionContext();
            context.initialize(getCli());
        }
        return context;
    }

    protected StringOutput getOutput() {
        if (output == null) {
            output = new StringOutput();
        }
        return output;
    }

    @Override
    protected void setUp() throws Exception {
        getOutput().clear();
    }
}
