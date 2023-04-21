package net.sf.mxlosgi.mxlosgixmppbundle;

import net.sf.mxlosgi.mxlosgiutilsbundle.StringUtils;

/**
 * Represents XMPP presence packets.
 * 
 * @see http://www.ietf.org/rfc/rfc3920.txt
 * @author noah
 */
public class Presence extends Packet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7452359020047855954L;

    private Type type = Type.available;

    private String status = null;

    private int priority = Integer.MIN_VALUE;

    private Show show = null;

    /**
	 * Creates a new presence update. Status, priority, and mode are left
	 * un-set.
	 * 
	 * @param type
	 *                  the type.
	 */
    public Presence(Type type) {
        setType(type);
    }

    /**
	 * Creates a new presence update with a specified status, priority,
	 * and mode.
	 * 
	 * @param type
	 *                  the type.
	 * @param status
	 *                  a text message describing the presence update.
	 * @param priority
	 *                  the priority of this presence update.
	 * @param mode
	 *                  the mode type for this presence update.
	 */
    public Presence(Type type, String status, int priority, Show show) {
        setType(type);
        setStatus(status);
        setPriority(priority);
        setShow(show);
    }

    /**
	 * Returns true if the {@link Type presence type} is available
	 * (online) and false if the user is unavailable (offline), or if this
	 * is a presence packet involved in a subscription operation. This is
	 * a convenience method equivalent to
	 * <tt>getType() == Presence.Type.available</tt>.
	 * 
	 * @return true if the presence type is available.
	 */
    public boolean isAvailable() {
        return type == Type.available;
    }

    /**
	 * Returns true if the presence type is
	 * {@link Type#available available} and the presence mode is
	 * {@link Show#away away}, {@link Show#xa extended away}, or
	 * {@link Show#dnd do not disturb}. False will be returned when the
	 * type or mode is any other value, including when the presence type
	 * is unavailable (offline). This is a convenience method equivalent
	 * to
	 * <tt>type == Type.available && (mode == Mode.away || mode == Mode.xa || mode == Mode.dnd)</tt>.
	 * 
	 * @return true if the presence type is available and the presence
	 *         mode is away, xa, or dnd.
	 */
    public boolean isAway() {
        return type == Type.available && (show == Show.away || show == Show.xa || show == Show.dnd);
    }

    /**
	 * Returns the type of this presence packet.
	 * 
	 * @return the type of the presence packet.
	 */
    public Type getType() {
        return type;
    }

    /**
	 * Sets the type of the presence packet.
	 * 
	 * @param type
	 *                  the type of the presence packet.
	 */
    public void setType(Type type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be null");
        }
        this.type = type;
    }

    /**
	 * Returns the status message of the presence update, or <tt>null</tt>
	 * if there is not a status. The status is free-form text describing a
	 * user's presence (i.e., "gone to lunch").
	 * 
	 * @return the status message.
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * Sets the status message of the presence update.
	 * 
	 * @param status
	 *                  the status message.
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * Returns the priority of the presence, or Integer.MIN_VALUE if no
	 * priority has been set.
	 * 
	 * @return the priority.
	 */
    public int getPriority() {
        return priority;
    }

    /**
	 * Sets the priority of the presence. The valid range is -128 through
	 * 128.
	 * 
	 * @param priority
	 *                  the priority of the presence.
	 * @throws IllegalArgumentException
	 *                   if the priority is outside the valid range.
	 */
    public void setPriority(int priority) {
        if (priority < -128 || priority > 128) {
            throw new IllegalArgumentException("Priority value " + priority + " is not valid. Valid range is -128 through 128.");
        }
        this.priority = priority;
    }

    /**
	 * Returns the mode of the presence update, or <tt>null</tt> if the
	 * mode is not set. A null presence mode value is interpreted to be
	 * the same thing as {@link Presence.Show#available}.
	 * 
	 * @return the show.
	 */
    public Show getShow() {
        return show;
    }

    /**
	 * Sets the mode of the presence update. A null presence mode value is
	 * interpreted to be the same thing as {@link Presence.Show#available}.
	 * 
	 * @param mode
	 *                  the mode.
	 */
    public void setShow(Show show) {
        this.show = show;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<presence");
        if (getLanguage() != null) {
            buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
        }
        if (getStanzaID() != null) {
            buf.append(" id=\"").append(getStanzaID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo().toFullJID())).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom().toFullJID())).append("\"");
        }
        if (type != Type.available) {
            buf.append(" type=\"").append(type).append("\"");
        }
        buf.append(">");
        if (status != null) {
            buf.append("<status>").append(StringUtils.escapeForXML(status)).append("</status>");
        }
        if (priority != Integer.MIN_VALUE) {
            buf.append("<priority>").append(priority).append("</priority>");
        }
        if (show != null && show != Show.available) {
            buf.append("<show>").append(show).append("</show>");
        }
        buf.append(this.getExtensionsXML());
        XMPPError error = getError();
        if (error != null) {
            buf.append(error.toXML());
        }
        buf.append("</presence>");
        return buf.toString();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(type);
        if (show != null) {
            buf.append(": ").append(show);
        }
        if (getStatus() != null) {
            buf.append(" (").append(getStatus()).append(")");
        }
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Presence presence = (Presence) super.clone();
        presence.type = this.type;
        presence.status = this.status;
        presence.priority = this.priority;
        presence.show = this.show;
        return presence;
    }

    /**
	 * A enum to represent the presecence type.
	 */
    public enum Type {

        /**
		 * The user is available to receive messages (default).
		 */
        available, /**
		 * The user is unavailable to receive messages.
		 */
        unavailable, /**
		 * Request subscription to recipient's presence.
		 */
        subscribe, /**
		 * Grant subscription to sender's presence.
		 */
        subscribed, /**
		 * Request removal of subscription to sender's presence.
		 */
        unsubscribe, /**
		 * Grant removal of subscription to sender's presence.
		 */
        unsubscribed, /**
		 * A request for an entity's current presence.

		 */
        probe, /**
		 * The presence packet contains an error message.
		 */
        error;

        public static Type fromString(String name) {
            try {
                return Type.valueOf(name);
            } catch (Exception e) {
                return available;
            }
        }
    }

    /**
	 * An enum to represent the presence show.
	 */
    public enum Show {

        /**
		 * Free to chat.
		 */
        chat, /**
		 * Available (the default).
		 */
        available, /**
		 * Away.
		 */
        away, /**
		 * Away for an extended period of time.
		 */
        xa, /**
		 * Do not disturb.
		 */
        dnd;

        public static Show fromString(String name) {
            try {
                return Show.valueOf(name);
            } catch (Exception e) {
                return available;
            }
        }
    }
}
