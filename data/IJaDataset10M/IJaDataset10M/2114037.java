package org.rascalli.framework.jabber;

import org.rascalli.framework.event.Event;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2009-07-22 14:01:34 +0200 (Mi, 22 Jul 2009) $<br/> $Revision: 2446 $
 * </p>
 * 
 * @author Christian Schollum
 */
public class JabberPresenceChanged implements Event {

    public enum Status {

        ONLINE, OFFLINE
    }

    private final Status status;

    private final String jabberId;

    /**
     * @param status
     */
    public JabberPresenceChanged(final String jabberId, final Status status) {
        this.jabberId = jabberId;
        this.status = status;
    }

    /**
     * @return the jabberId
     */
    public String getJabberId() {
        return jabberId;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    public boolean isOnline() {
        return status == Status.ONLINE;
    }
}
