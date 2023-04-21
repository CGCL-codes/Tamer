package sk.naive.talker.command;

import sk.naive.talker.*;
import sk.naive.talker.props.PropertyStoreException;
import sk.naive.talker.message.DefaultMessageFactory;
import sk.naive.talker.util.Utils;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.23 $ $Date: 2005/01/25 21:57:04 $
 */
public class Password extends AbstractCommand {

    /** Permission for changing password of another user. */
    public static final String PERM_PASSWORD_CHANGE = "passwordChange";

    public void exec() throws CommandException, RemoteException, PropertyStoreException {
        String[] sa = Utils.splitWords(params, 3);
        if (sa.length < 2 || sa.length > 3) {
            sendMisusageWarning();
            return;
        }
        try {
            if (sa.length == 2) {
                String old = Utils.passwordString(sa[0]);
                if (old.equals(user.get(User.UPROP_PASSWORD))) {
                    setPassword(user, sa[1]);
                    sendHelper().sendMessage(user, "password.successful");
                } else {
                    sendHelper().sendMessage(user, "password.incorrectPassword");
                }
            } else {
                Utils.checkPermission(user, PERM_PASSWORD_CHANGE);
                String victimLogin = sa[2];
                User victim = commandDispatcher.getUserFinder().findUserByLogin(victimLogin);
                if (victim == null) {
                    sendUserNotFoundMessage(victimLogin);
                    return;
                }
                if (victim == user) {
                    sendHelper().sendMessage(user, selfWarning());
                    return;
                }
                setPassword(victim, sa[1]);
                user.getProperties().put(DefaultMessageFactory.CTXKEY_VAL, victim.getProperties());
                sendHelper().sendMessage(user, "password.successful.invoker");
                actionNotify(victim, "password.successful.victim");
                logger().log(Level.INFO, "User {0} changed password for user {1}.", new Object[] { user.getLogin(), victim.getLogin() });
            }
        } catch (NoSuchAlgorithmException e) {
            throw new CommandException(e);
        }
    }

    private void setPassword(User u, String passwd) throws NoSuchAlgorithmException, PropertyStoreException {
        u.set(User.UPROP_PASSWORD, Utils.passwordString(passwd));
        u.getProperties().put(User.UPROP_PLAIN_PASSWORD, passwd);
    }
}
