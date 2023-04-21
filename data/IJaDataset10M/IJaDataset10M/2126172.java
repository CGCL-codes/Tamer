package jhomenet.server.console.command;

import java.util.Map;
import org.apache.log4j.Logger;
import jhomenet.commons.GeneralApplicationContext;
import jhomenet.commons.cfg.AbstractSystemConfiguration;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.commons.work.WorkQueue;
import jhomenet.ui.MainWindow;
import jhomenet.ui.UiManager;
import jhomenet.ui.window.IWindowListener;
import jhomenet.ui.window.DefaultWindowEvent;
import jhomenet.server.console.io.SystemInputStream;
import jhomenet.server.console.io.SystemPrintStream;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class OpenMainWindowCommand extends AbstractBlockingCommand implements IWindowListener {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(OpenMainWindowCommand.class.getName());

    /**
	 * Command.
	 */
    private static final String command = "main-window";

    /**
	 * 
	 * @param serverContext
	 */
    public OpenMainWindowCommand(GeneralApplicationContext serverContext) {
        super(serverContext);
    }

    /**
	 * @see jhomenet.server.console.command.Command#execute(jhomenet.server.console.io.SystemInputStream, jhomenet.server.console.io.SystemPrintStream, jhomenet.server.console.io.SystemPrintStream, java.lang.String[], java.util.Map)
	 */
    @Override
    void blockingExecute(SystemInputStream in, SystemPrintStream out, SystemPrintStream err, String[] args, Map<String, Object> env) throws Exception {
    }

    /**
	 * @see jhomenet.server.console.command.AbstractBlockingCommand#longRunningMethod()
	 */
    @Override
    void longRunningMethod() {
        UiManager.getInstance(serverContext).createMainGuiWindow(false, this);
    }

    /**
	 * @see jhomenet.commons.ui.WindowListener#windowClosing(jhomenet.commons.ui.WindowEvent)
	 */
    public void windowClosing(DefaultWindowEvent event) {
        this.unblock();
    }

    /**
	 * @see jhomenet.server.console.command.Command#getCommandName()
	 */
    public String getCommandName() {
        return command;
    }

    /**
	 * @see jhomenet.server.console.command.Command#getUsageString()
	 */
    public String getUsageString() {
        return "Open the hardware editor.\r\nUsage:\r\n  hardware-editor\r\n";
    }
}
