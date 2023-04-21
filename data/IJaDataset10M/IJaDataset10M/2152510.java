package cu.ftpd.user.userbases.actions;

import cu.ftpd.Connection;
import cu.ftpd.Server;
import cu.ftpd.ServiceManager;
import cu.ftpd.filesystem.FileSystem;
import cu.ftpd.user.User;
import cu.ftpd.user.userbases.NoSuchUserException;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-okt-26 : 23:55:59
 * @version $Id: PrimaryGroup.java 258 2008-10-26 12:47:23Z jevring $
 */
public class PrimaryGroup extends UserbaseAction {

    public PrimaryGroup() {
        super("primarygroup");
    }

    public void execute(String[] parameterList, Connection connection, User user, FileSystem fs) {
        if (user.isMemberOfGroup(parameterList[1])) {
            user.setPrimaryGroup(parameterList[1]);
            connection.respond("200 primarygroup set to: " + parameterList[1]);
        } else {
            connection.respond("550 Failed to set primarygroup: you are not a member of the specified group");
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
