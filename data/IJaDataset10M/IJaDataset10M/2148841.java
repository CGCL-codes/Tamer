package cu.ftpd.commands.site.actions;

import cu.ftpd.Connection;
import cu.ftpd.Server;
import cu.ftpd.ServiceManager;
import cu.ftpd.filesystem.FileSystem;
import cu.ftpd.user.User;
import cu.ftpd.user.userbases.NoSuchUserException;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-okt-27 : 00:14:59
 * @version $Id: Stat.java 262 2008-10-30 21:29:48Z jevring $
 */
public class Stat extends Action {

    public Stat() {
        super("stat");
    }

    public void execute(String[] parameterList, Connection connection, User user, FileSystem fs) {
        connection.statline(-1);
    }
}
