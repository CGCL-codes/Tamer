package net.sf.mxlosgi.muc;

import java.util.HashSet;
import java.util.Set;
import net.sf.mxlosgi.xmpp.JID;
import net.sf.mxlosgi.xmpp.PacketExtension;
import net.sf.mxlosgi.xmpp.XmlStanza;

/**
 * 
 * @author noah
 * 
 */
public class MucUserExtension implements PacketExtension {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9159302710188716565L;

    public static final String ELEMENTNAME = "x";

    public static final String NAMESPACE = "http://jabber.org/protocol/muc#user";

    private Invite invite;

    private Decline decline;

    private Item item;

    private String password;

    private Set<String> statusCodes = new HashSet<String>();

    private Destroy destroy;

    public String getElementName() {
        return ELEMENTNAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
        if (getInvite() != null) {
            buf.append(getInvite().toXML());
        }
        if (getDecline() != null) {
            buf.append(getDecline().toXML());
        }
        if (getItem() != null) {
            buf.append(getItem().toXML());
        }
        if (getPassword() != null) {
            buf.append("<password>").append(getPassword()).append("</password>");
        }
        if (!getStatusCodes().isEmpty()) {
            for (String code : getStatusCodes()) {
                buf.append("<status code=\"").append(code).append("\"/>");
            }
        }
        if (getDestroy() != null) {
            buf.append(getDestroy().toXML());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MucUserExtension extension = (MucUserExtension) super.clone();
        if (this.invite != null) {
            extension.invite = (Invite) this.invite.clone();
        }
        if (this.decline != null) {
            extension.decline = (Decline) this.decline.clone();
        }
        if (this.item != null) {
            extension.item = (Item) this.item.clone();
        }
        extension.password = this.password;
        extension.statusCodes = new HashSet<String>();
        for (String statusCode : this.statusCodes) {
            extension.statusCodes.add(statusCode);
        }
        if (this.destroy != null) {
            extension.destroy = (Destroy) this.destroy.clone();
        }
        return extension;
    }

    /**
	 * Returns the invitation for another user to a room. The sender of
	 * the invitation must be an occupant of the room. The invitation will
	 * be sent to the room which in turn will forward the invitation to
	 * the invitee.
	 * 
	 * @return an invitation for another user to a room.
	 */
    public Invite getInvite() {
        return invite;
    }

    /**
	 * Returns the rejection to an invitation from another user to a room.
	 * The rejection will be sent to the room which in turn will forward
	 * the refusal to the inviter.
	 * 
	 * @return a rejection to an invitation from another user to a room.
	 */
    public Decline getDecline() {
        return decline;
    }

    /**
	 * Returns the item child that holds information about roles,
	 * affiliation, jids and nicks.
	 * 
	 * @return an item child that holds information about roles,
	 *         affiliation, jids and nicks.
	 */
    public Item getItem() {
        return item;
    }

    /**
	 * Returns the password to use to enter Password-Protected Room. A
	 * Password-Protected Room is a room that a user cannot enter without
	 * first providing the correct password.
	 * 
	 * @return the password to use to enter Password-Protected Room.
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * Returns the status which holds a code that assists in presenting
	 * notification messages.
	 * 
	 * @return the status which holds a code that assists in presenting
	 *         notification messages.
	 */
    public Set<String> getStatusCodes() {
        return statusCodes;
    }

    /**
	 * Returns the notification that the room has been destroyed. After a
	 * room has been destroyed, the room occupants will receive a Presence
	 * packet of type 'unavailable' with the reason for the room
	 * destruction if provided by the room owner.
	 * 
	 * @return a notification that the room has been destroyed.
	 */
    public Destroy getDestroy() {
        return destroy;
    }

    /**
	 * Sets the invitation for another user to a room. The sender of the
	 * invitation must be an occupant of the room. The invitation will be
	 * sent to the room which in turn will forward the invitation to the
	 * invitee.
	 * 
	 * @param invite
	 *                  the invitation for another user to a room.
	 */
    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    /**
	 * Sets the rejection to an invitation from another user to a room.
	 * The rejection will be sent to the room which in turn will forward
	 * the refusal to the inviter.
	 * 
	 * @param decline
	 *                  the rejection to an invitation from another user
	 *                  to a room.
	 */
    public void setDecline(Decline decline) {
        this.decline = decline;
    }

    /**
	 * Sets the item child that holds information about roles,
	 * affiliation, jids and nicks.
	 * 
	 * @param item
	 *                  the item child that holds information about roles,
	 *                  affiliation, jids and nicks.
	 */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
	 * Sets the password to use to enter Password-Protected Room. A
	 * Password-Protected Room is a room that a user cannot enter without
	 * first providing the correct password.
	 * 
	 * @param string
	 *                  the password to use to enter Password-Protected
	 *                  Room.
	 */
    public void setPassword(String string) {
        password = string;
    }

    /**
	 * Sets the status which holds a code that assists in presenting
	 * notification messages.
	 * 
	 * @param status
	 *                  the status which holds a code that assists in
	 *                  presenting notification messages.
	 */
    public void setStatusList(Set<String> statusCodes) {
        this.statusCodes = statusCodes;
    }

    /**
	 * Sets the notification that the room has been destroyed. After a
	 * room has been destroyed, the room occupants will receive a Presence
	 * packet of type 'unavailable' with the reason for the room
	 * destruction if provided by the room owner.
	 * 
	 * @param destroy
	 *                  the notification that the room has been destroyed.
	 */
    public void setDestroy(Destroy destroy) {
        this.destroy = destroy;
    }

    public void addStatusCode(String code) {
        statusCodes.add(code);
    }

    /**
	 * 
	 * @author noah
	 *
	 */
    public static class Invite implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = -9071679649270743257L;

        private String reason;

        private JID from;

        private JID to;

        public JID getFrom() {
            return from;
        }

        /**
		 * Returns the message explaining the invitation.
		 * 
		 * @return the message explaining the invitation.
		 */
        public String getReason() {
            return reason;
        }

        /**
		 * Returns the bare JID of the invitee.
		 * 
		 * @return the bare JID of the invitee.
		 */
        public JID getTo() {
            return to;
        }

        /**
		 * Sets the bare JID of the inviter or, optionally, the room
		 * JID.
		 * 
		 * @param from
		 *                  the bare JID of the inviter or,
		 *                  optionally, the room JID.
		 */
        public void setFrom(JID from) {
            this.from = from;
        }

        /**
		 * Sets the message explaining the invitation.
		 * 
		 * @param reason
		 *                  the message explaining the invitation.
		 */
        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
		 * Sets the bare JID of the invitee.
		 * 
		 * @param to
		 *                  the bare JID of the invitee.
		 */
        public void setTo(JID to) {
            this.to = to;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<invite ");
            if (getTo() != null) {
                buf.append(" to=\"").append(getTo().toFullJID()).append("\"");
            }
            if (getFrom() != null) {
                buf.append(" from=\"").append(getFrom().toFullJID()).append("\"");
            }
            buf.append(">");
            if (getReason() != null) {
                buf.append("<reason>").append(getReason()).append("</reason>");
            }
            buf.append("</invite>");
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Invite invite = (Invite) super.clone();
            invite.reason = this.reason;
            invite.from = this.from;
            invite.to = this.to;
            return invite;
        }
    }

    /**
	 * 
	 * @author noah
	 *
	 */
    public static class Decline implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = -5791457809021031626L;

        private String reason;

        private JID from;

        private JID to;

        /**
		 * Returns the bare JID of the invitee that rejected the
		 * invitation.
		 * 
		 * @return the bare JID of the invitee that rejected the
		 *         invitation.
		 */
        public JID getFrom() {
            return from;
        }

        /**
		 * Returns the message explaining why the invitation was
		 * rejected.
		 * 
		 * @return the message explaining the reason for the
		 *         rejection.
		 */
        public String getReason() {
            return reason;
        }

        /**
		 * Returns the bare JID of the inviter. 
		 * 
		 * @return the bare JID of the inviter.
		 */
        public JID getTo() {
            return to;
        }

        /**
		 * Sets the bare JID of the invitee that rejected the
		 * invitation.
		 * 
		 * @param from
		 *                  the bare JID of the invitee that
		 *                  rejected the invitation.
		 */
        public void setFrom(JID from) {
            this.from = from;
        }

        /**
		 * Sets the message explaining why the invitation was
		 * rejected.
		 * 
		 * @param reason
		 *                  the message explaining the reason for
		 *                  the rejection.
		 */
        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
		 * Sets the bare JID of the inviter. (e.g.
		 * 'hecate@shakespeare.lit')
		 * 
		 * @param to
		 *                  the bare JID of the inviter.
		 */
        public void setTo(JID to) {
            this.to = to;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<decline ");
            if (getTo() != null) {
                buf.append(" to=\"").append(getTo().toFullJID()).append("\"");
            }
            if (getFrom() != null) {
                buf.append(" from=\"").append(getFrom().toFullJID()).append("\"");
            }
            buf.append(">");
            if (getReason() != null) {
                buf.append("<reason>").append(getReason()).append("</reason>");
            }
            buf.append("</decline>");
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Decline decline = (Decline) super.clone();
            decline.reason = this.reason;
            decline.from = this.from;
            decline.to = this.to;
            return decline;
        }
    }

    /**
	 * Item child that holds information about roles, affiliation, jids
	 * and nicks.
	 *
	 */
    public static class Item implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = -808860584810588081L;

        private JID actor;

        private String reason;

        private String affiliation;

        private JID jid;

        private String nick;

        private String role;

        /**
		 * Creates a new item child.
		 * 
		 * @param affiliation
		 *                  the actor's affiliation to the room
		 * @param role
		 *                  the privilege level of an occupant
		 *                  within a room.
		 */
        public Item(String affiliation, String role) {
            this.affiliation = affiliation;
            this.role = role;
        }

        /**
		 * Returns the actor (JID of an occupant in the room) that
		 * was kicked or banned.
		 * 
		 * @return the JID of an occupant in the room that was
		 *         kicked or banned.
		 */
        public JID getActor() {
            return actor;
        }

        /**
		 * Returns the reason for the item child. The reason is
		 * optional and could be used to explain the reason why a
		 * user (occupant) was kicked or banned.
		 * 
		 * @return the reason for the item child.
		 */
        public String getReason() {
            return reason;
        }

        /**
		 * Returns the occupant's affiliation to the room. The
		 * affiliation is a semi-permanent association or connection
		 * with a room. The possible affiliations are "owner",
		 * "admin", "member", and "outcast" (naturally it is also
		 * possible to have no affiliation). An affiliation lasts
		 * across a user's visits to a room.
		 * 
		 * @return the actor's affiliation to the room
		 */
        public String getAffiliation() {
            return affiliation;
        }

        /**
		 * Returns the <room@service/nick> by which an occupant is
		 * identified within the context of a room. If the room is
		 * non-anonymous, the JID will be included in the item.
		 * 
		 * @return the room JID by which an occupant is identified
		 *         within the room.
		 */
        public JID getJid() {
            return jid;
        }

        /**
		 * Returns the new nickname of an occupant that is changing
		 * his/her nickname. The new nickname is sent as part of the
		 * unavailable presence.
		 * 
		 * @return the new nickname of an occupant that is changing
		 *         his/her nickname.
		 */
        public String getNick() {
            return nick;
        }

        /**
		 * Returns the temporary position or privilege level of an
		 * occupant within a room. The possible roles are
		 * "moderator", "participant", and "visitor" (it is also
		 * possible to have no defined role). A role lasts only for
		 * the duration of an occupant's visit to a room.
		 * 
		 * @return the privilege level of an occupant within a room.
		 */
        public String getRole() {
            return role;
        }

        /**
		 * Sets the actor (JID of an occupant in the room) that was
		 * kicked or banned.
		 * 
		 * @param actor
		 *                  the actor (JID of an occupant in the
		 *                  room) that was kicked or banned.
		 */
        public void setActor(JID actor) {
            this.actor = actor;
        }

        /**
		 * Sets the reason for the item child. The reason is
		 * optional and could be used to explain the reason why a
		 * user (occupant) was kicked or banned.
		 * 
		 * @param reason
		 *                  the reason why a user (occupant) was
		 *                  kicked or banned.
		 */
        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
		 * Sets the <room@service/nick> by which an occupant is
		 * identified within the context of a room. If the room is
		 * non-anonymous, the JID will be included in the item.
		 * 
		 * @param jid
		 *                  the JID by which an occupant is
		 *                  identified within a room.
		 */
        public void setJid(JID jid) {
            this.jid = jid;
        }

        /**
		 * Sets the new nickname of an occupant that is changing
		 * his/her nickname. The new nickname is sent as part of the
		 * unavailable presence.
		 * 
		 * @param nick
		 *                  the new nickname of an occupant that is
		 *                  changing his/her nickname.
		 */
        public void setNick(String nick) {
            this.nick = nick;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item");
            if (getAffiliation() != null) {
                buf.append(" affiliation=\"").append(getAffiliation()).append("\"");
            }
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid().toFullJID()).append("\"");
            }
            if (getNick() != null) {
                buf.append(" nick=\"").append(getNick()).append("\"");
            }
            if (getRole() != null) {
                buf.append(" role=\"").append(getRole()).append("\"");
            }
            if (getReason() == null && getActor() == null) {
                buf.append("/>");
            } else {
                buf.append(">");
                if (getReason() != null) {
                    buf.append("<reason>").append(getReason()).append("</reason>");
                }
                if (getActor() != null) {
                    buf.append("<actor jid=\"").append(getActor()).append("\"/>");
                }
                buf.append("</item>");
            }
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Item item = (Item) super.clone();
            item.actor = this.actor;
            item.reason = this.reason;
            item.affiliation = this.affiliation;
            item.jid = this.jid;
            item.nick = this.nick;
            item.role = this.role;
            return item;
        }
    }

    public static class Destroy implements XmlStanza {

        /**
		 * 
		 */
        private static final long serialVersionUID = 4520903133459755780L;

        private String reason;

        private JID jid;

        /**
		 * Returns the JID of an alternate location since the
		 * current room is being destroyed.
		 * 
		 * @return the JID of an alternate location.
		 */
        public JID getJid() {
            return jid;
        }

        /**
		 * Returns the reason for the room destruction.
		 * 
		 * @return the reason for the room destruction.
		 */
        public String getReason() {
            return reason;
        }

        /**
		 * Sets the JID of an alternate location since the current
		 * room is being destroyed.
		 * 
		 * @param jid
		 *                  the JID of an alternate location.
		 */
        public void setJid(JID jid) {
            this.jid = jid;
        }

        /**
		 * Sets the reason for the room destruction.
		 * 
		 * @param reason
		 *                  the reason for the room destruction.
		 */
        public void setReason(String reason) {
            this.reason = reason;
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<destroy");
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid().toFullJID()).append("\"");
            }
            if (getReason() == null) {
                buf.append("/>");
            } else {
                buf.append(">");
                if (getReason() != null) {
                    buf.append("<reason>").append(getReason()).append("</reason>");
                }
                buf.append("</destroy>");
            }
            return buf.toString();
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Destroy destory = (Destroy) super.clone();
            destory.reason = this.reason;
            destory.jid = this.jid;
            return destory;
        }
    }
}
