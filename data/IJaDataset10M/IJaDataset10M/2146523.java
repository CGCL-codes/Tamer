package net.sf.drftpd.event;

import org.drftpd.usermanager.User;

/**
 * @author mog
 *
 * @version $Id: InviteEvent.java 1764 2007-08-04 02:01:21Z tdsoul $
 */
public class InviteEvent extends Event {

    private String _ircNick;

    private User _user;

    public InviteEvent(String command, String ircUser, User user) {
        super(command, System.currentTimeMillis());
        _ircNick = ircUser;
        _user = user;
    }

    public String getIrcNick() {
        return _ircNick;
    }

    public User getUser() {
        return _user;
    }
}
