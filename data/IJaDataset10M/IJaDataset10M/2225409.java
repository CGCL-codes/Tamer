package org.snipsnap.messaging;

/**
 * Simple Message to be sent with MessageService
 *
 * @author Stephan J. Schmidt
 * @version $Id: Message.java 1706 2004-07-08 08:53:20Z stephan $
 */
public class Message {

    public static final String SNIP_CREATE = "SNIP_CREATE";

    public static final String SNIP_MODIFIED = "SNIP_MODIFIED";

    public static final String SNIP_REMOVE = "SNIP_REMOVE";

    private String type;

    private Object value;

    public Message(String serialized) {
        String[] args = serialized.split("//");
        type = args[0];
        value = args[1];
    }

    public Message(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        return type + "//" + value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        final Message message = (Message) o;
        if (type != null ? !type.equals(message.type) : message.type != null) return false;
        if (value != null ? !value.equals(message.value) : message.value != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (type != null ? type.hashCode() : 0);
        result = 29 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
