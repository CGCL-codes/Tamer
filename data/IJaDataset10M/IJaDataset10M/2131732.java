package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.util.Utils;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Shuts talker down.
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.24 $ $Date: 2004/10/07 20:10:35 $
 */
public class Shutdown extends AbstractCommand {

    public static final String PERM_SHUTDOWN = "shutdown";

    public void exec() throws RemoteException, CommandException {
        Utils.checkPermission(user, PERM_SHUTDOWN);
        logger().log(Level.INFO, "Shutdown initialized by user {0}.", user.getLogin());
        Iterator i = commandDispatcher.getUserFinder().onlineUsers().iterator();
        while (i.hasNext()) {
            User u2 = (User) i.next();
            if (!u2.getLogin().equals(user.getLogin())) {
                u2.send(getString("shutdown.seeYou", u2.getProperties()));
                i.remove();
                getTalker().userOut(u2);
            }
        }
        user.send(getString("shutdown.onlyYou", user.getProperties()));
        getTalker().userOut(user);
        getTalker().shutdown();
    }
}
