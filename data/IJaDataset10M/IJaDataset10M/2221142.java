package org.openymsg.legacy.network.event;

import java.util.Set;
import org.openymsg.legacy.network.YahooConference;
import org.openymsg.legacy.network.YahooUser;

public class SessionConferenceInviteEvent extends AbstractSessionConferenceEvent {

    private static final long serialVersionUID = 6550109967442109240L;

    private Set<YahooUser> invitedUsers;

    private Set<YahooUser> currentUsers;

    public SessionConferenceInviteEvent(Object o, String to, String from, String message, YahooConference conference, Set<YahooUser> invitedUsers, Set<YahooUser> currentUsers) {
        super(o, to, from, message, conference);
        this.invitedUsers = invitedUsers;
        this.currentUsers = currentUsers;
    }

    public Set<YahooUser> getInvitedUsers() {
        return invitedUsers;
    }

    public Set<YahooUser> getCurrentUsers() {
        return currentUsers;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        if (invitedUsers != null) sb.append(" invitedUsers(size):").append(invitedUsers.size());
        if (currentUsers != null) sb.append(" currentUsers(size):").append(currentUsers.size());
        return sb.toString();
    }
}
