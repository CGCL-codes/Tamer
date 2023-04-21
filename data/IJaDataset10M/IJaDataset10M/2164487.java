package org.openymsg.legacy.roster;

import java.util.EventObject;
import org.openymsg.legacy.network.YahooUser;

/**
 * A RosterEvent will be forwarded to any registered {@link RosterListener} if a {@link Roster} object gets changed.
 * 
 * @author Guus der Kinderen, guus@nimbuzz.com
 */
public class RosterEvent extends EventObject {

    private static final long serialVersionUID = 138074150875043635L;

    private final transient YahooUser user;

    private final transient RosterEventType type;

    /**
     * Creates a new RosterEvent instance, based on the {@link Roster} object that is the source of this event, a
     * reference to the user on the Roster object that caused the change and the type of change that was made.
     * 
     * @param user
     * @param type
     * @param source
     */
    public RosterEvent(final Roster source, final YahooUser user, final RosterEventType type) {
        super(source);
        if (type == null) {
            throw new IllegalArgumentException("Argument 'type' cannot be null.");
        }
        if (user == null) {
            throw new IllegalArgumentException("Argument 'user' cannot be null.");
        }
        this.type = type;
        this.user = user;
    }

    /**
     * @return the user
     */
    public YahooUser getUser() {
        return user;
    }

    /**
     * @return the type
     */
    public RosterEventType getType() {
        return type;
    }

    /**
     * The object on which the Event initially occurred.
     * 
     * @return The object on which the Event initially occurred.
     */
    @Override
    public Roster getSource() {
        return (Roster) source;
    }

    /**
     * Returns a String representation of this RosterEvent.
     * 
     * @return A a String representation of this RosterEvent.
     */
    @Override
    public String toString() {
        return getClass().getName() + "[type=" + type + ", user=" + user + ", source=" + source + "]";
    }
}
