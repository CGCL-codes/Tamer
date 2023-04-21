package net.sf.mxlosgi.xmpp;

import net.sf.mxlosgi.utils.StringUtils;

/**
 * @author noah
 * 
 */
public class PrivacyItem implements XmlStanza {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8403984968542763162L;

    private Type type;

    private String value;

    private boolean action = false;

    private int order;

    /** blocks incoming IQ stanzas. */
    private boolean filterIQ = false;

    /** filterMessage blocks incoming message stanzas. */
    private boolean filterMessage = false;

    /** blocks incoming presence notifications. */
    private boolean filterPresence_in = false;

    /** blocks outgoing presence notifications. */
    private boolean filterPresence_out = false;

    /**
	 * @param action
	 * @param order
	 */
    public PrivacyItem(boolean action, int order) {
        setAction(action);
        setOrder(order);
    }

    public PrivacyItem(Type type, String value, boolean action, int order) {
        setType(type);
        setValue(value);
        setAction(action);
        setOrder(order);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (type == Type.subscription) {
            if (!(value.equalsIgnoreCase(Subscription.both.name()) || value.equalsIgnoreCase(Subscription.from.name()) || value.equalsIgnoreCase(Subscription.none.name()) || value.equalsIgnoreCase(Subscription.to.name()))) {
                throw new IllegalArgumentException("value must be set both,to,non or from");
            } else {
                value = value.toLowerCase();
            }
        }
        this.value = value;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        if (order < 0) {
            throw new IllegalArgumentException("order must be unsigned");
        }
        this.order = order;
    }

    public boolean isFilterIQ() {
        return filterIQ;
    }

    public void setFilterIQ(boolean filterIQ) {
        this.filterIQ = filterIQ;
    }

    public boolean isFilterMessage() {
        return filterMessage;
    }

    public void setFilterMessage(boolean filterMessage) {
        this.filterMessage = filterMessage;
    }

    public boolean isFilterPresence_in() {
        return filterPresence_in;
    }

    public void setFilterPresence_in(boolean filterPresence_in) {
        this.filterPresence_in = filterPresence_in;
    }

    public boolean isFilterPresence_out() {
        return filterPresence_out;
    }

    public void setFilterPresence_out(boolean filterPresence_out) {
        this.filterPresence_out = filterPresence_out;
    }

    public boolean isFilterEverything() {
        return !(this.isFilterIQ() || this.isFilterMessage() || this.isFilterPresence_in() || this.isFilterPresence_out());
    }

    public boolean isFilterEmpty() {
        return !this.isFilterIQ() && !this.isFilterMessage() && !this.isFilterPresence_in() && !this.isFilterPresence_out();
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<item");
        if (isAction()) {
            buf.append(" action=\"allow\"");
        } else {
            buf.append(" action=\"deny\"");
        }
        buf.append(" order=\"").append(getOrder()).append("\"");
        if (getType() != null) {
            buf.append(" type=\"").append(getType()).append("\"");
        }
        if (getValue() != null) {
            buf.append(" value=\"").append(StringUtils.escapeForXML(getValue())).append("\"");
        }
        if (isFilterEverything()) {
            buf.append("/>");
        } else {
            buf.append(">");
            if (isFilterIQ()) {
                buf.append("<iq/>");
            }
            if (isFilterMessage()) {
                buf.append("<message/>");
            }
            if (isFilterPresence_in()) {
                buf.append("<presence-in/>");
            }
            if (isFilterPresence_out()) {
                buf.append("<presence-out/>");
            }
            buf.append("</item>");
        }
        return buf.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        PrivacyItem item = (PrivacyItem) super.clone();
        item.type = this.type;
        item.value = this.value;
        item.action = this.action;
        item.order = this.order;
        item.filterIQ = this.filterIQ;
        item.filterMessage = this.filterMessage;
        item.filterPresence_in = this.filterPresence_in;
        item.filterPresence_out = this.filterPresence_out;
        return item;
    }

    /**
	 * If the type is "subscription", then the 'value' attribute MUST be
	 * one of "both", "to", "from", or "none"
	 */
    public static enum Subscription {

        both, to, from, none
    }

    /**
	 * Type defines if the rule is based on JIDs, roster groups or
	 * presence subscription types.
	 */
    public static enum Type {

        /**
		 * JID being analyzed should belong to a roster group of the
		 * list's owner.
		 */
        group, /**
		 * JID being analyzed should have a resource match, domain
		 * match or bare JID match.
		 */
        jid, /**
		 * JID being analyzed should belong to a contact present in
		 * the owner's roster with the specified subscription
		 * status.
		 */
        subscription
    }
}
