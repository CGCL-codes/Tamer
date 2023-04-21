package org.openymsg.roster;

/**
 * RosterListener objects can be added to a {@link Roster}, after which they will be notified of any changes to that
 * roster.
 * 
 * @author Guus der Kinderen, guus@nimbuzz.com
 */
public interface RosterListener {

    /**
     * Listener method that gets fired after each Roster change, if the event Listener has been registered with that
     * Roster.
     * 
     * @param event
     */
    public void rosterChanged(final RosterEvent event);
}
