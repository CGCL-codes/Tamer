package sk.naive.talker;

import sk.naive.talker.message.*;
import sk.naive.talker.props.PropertyStoreException;
import java.rmi.RemoteException;

/**
 * AfkHelper.
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.9 $ $Date: 2005/02/07 21:20:38 $
 */
public class AfkHelper {

    private User user;

    public static final String UPROP_AFK = "afk";

    public AfkHelper(User user) {
        this.user = user;
    }

    public void set(String params) {
        AfkStatus afkStatus = getStatus();
        if (afkStatus != null) {
            afkStatus.setReason(params);
        } else {
            afkStatus = new AfkStatus(params);
        }
        user.getProperties().put(UPROP_AFK, afkStatus);
    }

    public void unset(Talker talker, boolean printUnafk) throws RemoteException, PropertyStoreException {
        AfkStatus afkStatus = getStatus();
        if (afkStatus != null) {
            user.set(User.UPROP_TOTAL_AFK_TIME, (Long) user.get(User.UPROP_TOTAL_AFK_TIME) + afkStatus.getTime());
            if (printUnafk) {
                user.getProperties().put(DefaultMessageFactory.CTXKEY_VAL + "afkTime", afkStatus.getTime() / Consts.MINUTE_MILLIS);
                user.send(talker.messageFactory().getString("afk.unset", user.getProperties()));
                user.getProperties().remove(DefaultMessageFactory.CTXKEY_VAL + "afkTime");
                Location loc = ((sk.naive.talker.server.Talker) talker).locationFinder().findLocationForUser(user);
                if (loc != null) {
                    talker.sendToUsers(talker.messageFactory().createMessage("afk.unsetAudience", user.getProperties()), loc.users(), new UserIgnoreExcluder(user, Category.AFK));
                }
            }
            user.getProperties().remove(UPROP_AFK);
        }
    }

    public AfkStatus getStatus() {
        return (AfkStatus) user.getProperties().get(UPROP_AFK);
    }
}
