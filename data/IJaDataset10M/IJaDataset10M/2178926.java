package se.ramfelt.psn.model;

import java.util.Comparator;
import se.ramfelt.psn.model.Friend.Presence;

/**
 * Comparator for Friend class that orders friends according to their presence
 * and the their names.
 */
public class PresenceFriendComparator implements Comparator<Friend> {

    public int compare(Friend arg0, Friend arg1) {
        if (arg0.getPresence().equals(arg1.getPresence())) {
            if ((arg0.getPresence() == Presence.Offline) && (arg0.getLastSeenTimestamp() != arg1.getLastSeenTimestamp())) {
                return ((arg1.getLastSeenTimestamp() - arg0.getLastSeenTimestamp()) > 0 ? 1 : -1);
            } else {
                return arg0.getOnlineId().compareToIgnoreCase(arg1.getOnlineId());
            }
        }
        return arg0.getPresence().compareTo(arg1.getPresence());
    }
}
