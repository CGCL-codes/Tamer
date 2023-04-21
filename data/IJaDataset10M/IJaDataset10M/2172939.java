package net.sf.hsdd.telnet;

import java.io.IOException;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;

/**
 *
 * @author Alexey.Kurgan
 */
public class ExitCommand extends AbstractTelnetCommand {

    public ExitCommand() {
        super("exit", "Exit and close telnet connection", null);
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        throw new StopException();
    }
}
