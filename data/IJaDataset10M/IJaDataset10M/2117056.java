package cu.ftpd.user.userbases.actions;

import cu.ftpd.Connection;
import cu.ftpd.Server;
import cu.ftpd.ServiceManager;
import cu.ftpd.filesystem.FileSystem;
import cu.ftpd.logging.Formatter;
import cu.ftpd.user.User;
import cu.ftpd.user.UserPermission;
import cu.ftpd.user.userbases.NoSuchUserException;
import java.util.Map;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-okt-27 : 00:03:00
 * @version $Id: Users.java 258 2008-10-26 12:47:23Z jevring $
 */
public class Users extends UserbaseAction {

    private static final String lineFormat = "200- %-1s %-16s %-54s %-1s";

    public Users() {
        super("users");
    }

    public void execute(String[] parameterList, Connection connection, User user, FileSystem fs) {
        if (user.hasPermission(UserPermission.USERS)) {
            Map<String, User> users = ServiceManager.getServices().getUserbase().getUsers();
            connection.respond(createHeader());
            connection.respond(String.format(lineFormat, Formatter.getBar(), "User", "Group", Formatter.getBar()));
            connection.respond(Formatter.createLine(200));
            for (User u : users.values()) {
                if (u.isHidden() && !user.hasPermission(UserPermission.SEE_HIDDEN)) {
                    continue;
                }
                connection.respond(String.format(lineFormat, Formatter.getBar(), u.getUsername(), u.getPrimaryGroup(), Formatter.getBar()));
            }
            connection.respond(Formatter.createFooter(200));
        } else {
            connection.respond("531 Permission denied.");
        }
    }

    protected boolean currentUserIsGadminForUser(String username, User user) {
        try {
            User u = ServiceManager.getServices().getUserbase().getUser(username);
            for (String gadminGroup : user.getGadminGroups()) {
                if (u.isMemberOfGroup(gadminGroup)) {
                    return true;
                }
            }
        } catch (NoSuchUserException e) {
        }
        return false;
    }
}
