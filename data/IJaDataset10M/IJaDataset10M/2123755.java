package org.drftpd.commands.request;

import org.drftpd.GlobalContext;
import org.drftpd.commands.request.metadata.RequestUserData;
import org.drftpd.usermanager.User;
import org.drftpd.usermanager.UserResetHookInterface;
import java.util.Date;

/**
 * @author scitz0
 * @version $Id: RequestUserResetHook.java 2125 2010-09-29 11:37:42Z scitz0 $
 */
public class RequestUserResetHook implements UserResetHookInterface {

    public void init() {
    }

    public void resetHour(Date d) {
    }

    public void resetDay(Date d) {
    }

    public void resetWeek(Date d) {
        for (User user : GlobalContext.getGlobalContext().getUserManager().getAllUsers()) {
            user.getKeyedMap().setObject(RequestUserData.WEEKREQS, 0);
            user.commit();
        }
    }

    public void resetMonth(Date d) {
        resetWeek(d);
    }

    public void resetYear(Date d) {
        resetWeek(d);
    }
}
