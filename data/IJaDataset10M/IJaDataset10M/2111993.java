package org.speakmon.coffeehouse.protocol;

/**
 * This class models a server message.
 *
 * Created: Sun Oct 27 23:00:38 2002
 *
 * @author <a href="mailto:speakmon@localhost.localdomain">Ben Speakmon</a>
 * @version
 */
class ServerMessage extends IRCMessage {

    /**
     * Creates a new <code>ServerMessage</code> instance.
     *
     * @param source a <code>String</code> value
     * @param replyCode an <code>int</code> value
     * @param target a <code>String</code> value
     * @param parameters a <code>String</code> value
     */
    protected ServerMessage(String source, int replyCode, String target, String parameters) {
        this.source = source;
        this.replyCode = replyCode;
        this.target = target;
        this.parameters = parameters;
        destination = source;
    }

    /**
     * Returns a string for this message that's ready to send to a user.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        return parameters;
    }

    /**
     * Returns true, since this message contains a reply code.
     *
     * @return true
     */
    public boolean isNumeric() {
        return true;
    }
}
