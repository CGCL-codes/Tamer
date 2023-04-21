package jimo.osgi.modules.core;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import jimo.osgi.api.CommandContext;
import jimo.osgi.api.CommandHandler;
import jimo.osgi.api.Daemon;
import jimo.osgi.api.JIMOConstants;
import jimo.osgi.api.util.LogUtil;
import jimo.osgi.modules.logger.api.LogStreamListener;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

/**
 * TODO 
 * Convert to singleton, move daemon methods to CommandDaemon, make
 * bundleContext and commandContext into params.
 * @author logicfish@hotmail.com
 *
 */
public class CommandProcessor implements Daemon {

    private BundleContext context;

    private String name;

    CommandContext commandContext;

    boolean closeOnExit = false;

    private ServiceReference logReaderReference;

    private LogListener listener;

    static LogUtil log;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommandContext getCommandContext() {
        return commandContext;
    }

    public void setCommandContext(CommandContext commandContext) {
        this.commandContext = commandContext;
    }

    public CommandProcessor(InputStream in, PrintStream out, BundleContext ctx, String name, boolean interactive) {
        this.context = ctx;
        this.name = name;
        this.commandContext = new CommandContext(in, out, interactive);
        log = new LogUtil(context, getClass().getName(), null);
    }

    public CommandProcessor(CommandContext commandContext, BundleContext ctx, String name) {
        this.context = ctx;
        this.name = name;
        this.commandContext = commandContext;
    }

    public void run() {
        try {
            setupLogReader();
            String line = null;
            while (commandContext.isReady()) {
                commandContext.showPrompt();
                line = commandContext.getLine();
                if (line == null) return;
                String[] commands = line.split(";");
                for (int i = 0; i < commands.length; i++) {
                    process(commands[i].trim());
                }
            }
        } catch (Throwable e) {
            commandContext.error(e);
        } finally {
            if (logReaderReference != null) {
                LogReaderService service = (LogReaderService) context.getService(logReaderReference);
                service.removeLogListener(listener);
                context.ungetService(logReaderReference);
            }
            if (isCloseOnExit()) commandContext.close();
        }
    }

    /**
	 * 
	 */
    private void setupLogReader() {
        logReaderReference = context.getServiceReference(LogReaderService.class.getName());
        if (logReaderReference == null) {
            return;
        }
        LogReaderService service = (LogReaderService) context.getService(logReaderReference);
        if (service == null) {
            return;
        }
        service.addLogListener(new LogStreamListener(logReaderReference, commandContext.getOut(), LogService.LOG_INFO));
    }

    public void process(String cmd, String args) {
        ServiceReference[] serviceReferences = getCommands(cmd, context);
        if (serviceReferences == null || serviceReferences.length == 0) {
            commandContext.error("Unknown command: <" + cmd + ">");
            return;
        }
        for (int i = 0; i < serviceReferences.length; i++) {
            ServiceReference reference = serviceReferences[i];
            CommandHandler service = (CommandHandler) context.getService(reference);
            try {
                service.onCommand(args, commandContext);
            } catch (Throwable e) {
                commandContext.error(e);
            }
        }
    }

    public void process(String line) {
        if ("".equals(line) || line.charAt(0) == '#') return;
        StringTokenizer tokenizer = new StringTokenizer(Core.INSTANCE.getConfig().resolve(line));
        if (tokenizer.countTokens() == 0) return;
        String cmd = tokenizer.nextToken();
        String args = line.substring(cmd.length()).trim();
        process(cmd, args);
    }

    public static ServiceReference[] getCommands(BundleContext context) {
        try {
            return context.getServiceReferences(CommandHandler.class.getName(), null);
        } catch (InvalidSyntaxException e) {
        }
        return new ServiceReference[] {};
    }

    public static ServiceReference[] getCommands(String command, BundleContext context) {
        try {
            return context.getServiceReferences(CommandHandler.class.getName(), "(" + JIMOConstants.SERVICE_COMMANDNAME + "=" + command + ")");
        } catch (InvalidSyntaxException e) {
            if (log != null) {
                log.warn(e);
            } else {
                e.printStackTrace(System.err);
            }
        }
        return new ServiceReference[] {};
    }

    public boolean isCloseOnExit() {
        return closeOnExit;
    }

    public void setCloseOnExit(boolean closeOnExit) {
        this.closeOnExit = closeOnExit;
    }
}
