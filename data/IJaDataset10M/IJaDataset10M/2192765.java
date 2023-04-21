package net.sf.hsdd.telnet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import net.sf.hsdd.conf.usr.AuthInfo;
import net.sf.hsdd.conf.usr.User;
import net.sf.hsdd.conf.usr.UserManager;
import net.sf.hsdd.conf.usr.UserNotFound;
import net.sf.hsdd.util.Validator;
import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.terminal.ColorHelper;
import net.wimpi.telnetd.net.Connection;

/**
 *
 * @author Alexey.Kurgan
 */
public class PasswdCommand extends AbstractTelnetCommand {

    public PasswdCommand() {
        super("passwd", "Set new password for user", "passwd\n  Set new password for current user\n" + "passwd <user>\n  Set new password for <user> (only for user admin)\n");
    }

    public void run(String line, BasicTerminalIO tio, Connection connection, AuthInfo authInfo) throws IOException, ShutdownException, StopException {
        StringTokenizer tokens = new StringTokenizer(line);
        User user = null;
        String userName = null;
        if (tokens.countTokens() > 1) {
            if (!authInfo.getUser().getUserName().equals("admin")) {
                tio.write(ColorHelper.colorizeText("Set password for other user granted only for user admin\n", ColorHelper.RED));
                tio.flush();
                return;
            }
            tokens.nextToken();
            userName = trimAndRemoveQuotes(tokens.nextToken());
            try {
                user = UserManager.getUser(userName);
            } catch (UserNotFound ex) {
            }
        } else {
            user = authInfo.getUser();
        }
        if (user == null) {
            tio.write(MessageFormat.format("User \"{0}\" not found", userName));
        } else {
            String passwd = readPassword(tio);
            if (passwd == null) {
                tio.write("Password not set\n");
            } else {
                user.setPassword(passwd);
                UserManager.store();
                tio.write(MessageFormat.format("Password set for user {0}\n", user.getUserName()));
            }
        }
        tio.flush();
    }

    private String readPassword(BasicTerminalIO tio) throws IOException {
        boolean done = false;
        String userPasswd0 = null;
        String userPasswd1 = null;
        StringBuilder sbuf = new StringBuilder();
        tio.write("new password: ");
        tio.flush();
        while (!done) {
            int c = tio.read();
            if (c == '\n') {
                if (userPasswd0 == null) {
                    userPasswd0 = sbuf.toString();
                    tio.write("\nconfirm new password: ");
                    tio.flush();
                } else {
                    userPasswd1 = sbuf.toString();
                    done = true;
                    tio.write('\n');
                    tio.flush();
                }
                sbuf.delete(0, sbuf.length());
            } else if (c == BasicTerminalIO.BACKSPACE) {
                int length = sbuf.length();
                if (length > 0) sbuf.deleteCharAt(sbuf.length() - 1);
            } else sbuf.append((char) c);
        }
        if (Validator.isEmpty(userPasswd0)) {
            tio.write("Password is empty\n");
            tio.flush();
            return null;
        }
        if (!userPasswd0.equals(userPasswd1)) {
            tio.write("Passwords are not equals\n");
            tio.flush();
            return null;
        }
        return userPasswd0;
    }
}
