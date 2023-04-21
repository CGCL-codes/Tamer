package net.sf.hsdd.telnet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.sf.hsdd.util.Validator;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Alexey.Kurgan
 */
public class GatewayCommand implements TelnetCommand {

    private static Map<String, TelnetCommand> commands = new HashMap<String, TelnetCommand>(50);

    private static Log log = LogFactory.getLog(GatewayCommand.class);

    static {
        new ClearCommand();
        new ExitCommand();
        new ShutdownCommand();
        new VAOCommand();
        new SAOCommand();
        new VUCommand();
        new PasswdCommand();
        new CreateUserCommand();
        new DelUserCommand();
        new CreateHostCommand();
        new VHCommand();
        new SHOCommand();
        new DCommand();
        new VDCommand();
        new RFDCommand();
        new PDCommand();
        new CDCommand();
        new RSDCommand();
        new RemoveCommand();
        new VDICommand();
        new DelHostCommand();
        new TDCommand();
        new MoveCommand();
        new HelpCommand(commands);
    }

    public String getCommandName() {
        return null;
    }

    public String getCommandHelp() {
        return null;
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        if (Validator.isHardEmpty(line)) return;
        StringTokenizer tokens = new StringTokenizer(line);
        String commandName = tokens.nextToken().trim().toLowerCase();
        TelnetCommand command = commands.get(commandName);
        if (command == null) {
            if (log.isTraceEnabled()) log.trace(MessageFormat.format("Telnet command \"{0}\" not found with command line \"{1}\"", commandName, line));
            tio.write(MessageFormat.format("{0} {1} {2}", ColorHelper.colorizeText("Command", ColorHelper.RED), ColorHelper.colorizeText(commandName, ColorHelper.YELLOW), ColorHelper.colorizeText("not found\n", ColorHelper.RED)));
            tio.flush();
            return;
        }
        try {
            if (log.isTraceEnabled()) log.trace(MessageFormat.format("Executing \"{0}\" telnet command with command line \"{1}\"", commandName, line));
            command.run(line, tio, connection, authInfo);
        } catch (IllegalArgumentException ex) {
            log.error(MessageFormat.format("Unexpected exception by executing telnet command \"{0}\"", commandName), ex);
            tio.write(ColorHelper.colorizeText(MessageFormat.format("Unexpected exception: {0}\nWith message: {1}\n", ex.getClass().getName(), ex.getMessage()), ColorHelper.RED));
            tio.flush();
        }
    }

    public static void registerTelnetCommand(TelnetCommand command) {
        commands.put(command.getCommandName(), command);
        if (log.isDebugEnabled()) log.debug(MessageFormat.format("Register \"{0}\" telnet command", command.getCommandName()));
    }

    public String getLongHelp() {
        return null;
    }

    public int compareTo(TelnetCommand o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TelnetCommand);
    }
}
