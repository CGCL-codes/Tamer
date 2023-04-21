package net.sf.hsdd.telnet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.sf.hsdd.conf.usr.User;
import net.sf.hsdd.conf.usr.UserAlreadyExist;
import net.sf.hsdd.conf.usr.UserManager;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;

/**
 *
 * @author Alexey.Kurgan
 */
public class CreateUserCommand extends AbstractTelnetCommand {

    public CreateUserCommand() {
        super("createuser", "Create new user (only for user admin)", "createuser <user name>\n  Create user with <user name> (only for user admin)\n");
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        StringTokenizer tokens = new StringTokenizer(line);
        if (!authInfo.getUser().getUserName().equals("admin")) {
            tio.write(ColorHelper.colorizeText("This command granted only for user admin\n", ColorHelper.RED));
        } else if (tokens.countTokens() < 2) {
            tio.write("User name must be specified\n");
        } else {
            tokens.nextToken();
            String userName = trimAndRemoveQuotes(tokens.nextToken());
            try {
                UserManager.addUser(new User(userName, null));
                tio.write(MessageFormat.format("User {0} added to user database.\n" + "You must set password for this user for activate.\n", userName));
            } catch (UserAlreadyExist ex) {
                tio.write(MessageFormat.format("User {0} already exist in user database\n", userName));
            }
        }
        tio.flush();
    }
}
