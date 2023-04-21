package cu.ftpd.commands.site.actions;

import cu.ftpd.Connection;
import cu.ftpd.Server;
import cu.ftpd.ServiceManager;
import cu.ftpd.filesystem.FileSystem;
import cu.ftpd.user.User;
import cu.ftpd.user.userbases.NoSuchUserException;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-okt-26 : 23:37:36
 * @version $Id: Xdupe.java 262 2008-10-30 21:29:48Z jevring $
 */
public class Xdupe extends Action {

    public Xdupe() {
        super("xdupe");
    }

    public void execute(String[] parameterList, Connection connection, User user, FileSystem fs) {
        if (parameterList.length > 1) {
            try {
                int txdupe = Integer.parseInt(parameterList[1]);
                if (txdupe < 5) {
                    connection.setXdupe(txdupe);
                    connection.respond("200 Activated extended dupe mode " + connection.getXdupe() + '.');
                } else {
                    connection.respond("500 Unsuported xdupe mode.");
                }
            } catch (NumberFormatException e) {
                connection.respond("500 Can't set xdupe mode. " + parameterList[1] + " is not a number.");
            }
        } else {
            if (connection.getXdupe() == 0) {
                connection.respond("200 Extended dupe mode is disabled.");
            } else {
                connection.respond("200 Extended dupe mode " + connection.getXdupe() + " is enabled.");
            }
        }
    }
}
