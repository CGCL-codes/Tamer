package jhomenet.plugins.console;

import java.util.Map;
import jhomenet.commons.work.WorkQueue;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.server.console.IOUtils;
import jhomenet.server.console.ConsoleService;
import jhomenet.server.console.command.Command;
import jhomenet.server.console.io.SystemInputStream;
import jhomenet.server.console.io.SystemPrintStream;

/**
 * TODO: Class description.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class TestCommand implements Command {

    /**
	 * Command name;
	 */
    private static final String commandName = "plugin-test";

    /**
	 * Constructor.
	 */
    public TestCommand() {
        super();
    }

    /**
	 * @see jhomenet.server.console.command.Command#execute(jhomenet.server.console.io.SystemInputStream, jhomenet.server.console.io.SystemPrintStream, jhomenet.server.console.io.SystemPrintStream, java.lang.String[], java.util.Map)
	 */
    public void execute(SystemInputStream in, SystemPrintStream out, SystemPrintStream err, String[] args, Map<String, Object> env) throws Exception {
        HardwareManager hardwareManager = (HardwareManager) env.get(ConsoleService.ENV_HARDWAREMANAGER);
        WorkQueue workQueue = (WorkQueue) env.get(ConsoleService.ENV_WORKQUEUE);
        IOUtils.writeLine("This is a test plugin command.", out);
        IOUtils.newLine(out);
    }

    /**
	 * @see jhomenet.server.console.command.Command#getCommandName()
	 */
    public String getCommandName() {
        return commandName;
    }

    /**
	 * @see jhomenet.server.console.command.Command#getUsageString()
	 */
    public String getUsageString() {
        return "A test plugin command.\r\nUsage:\r\n  " + getCommandName() + "\r\n";
    }
}
