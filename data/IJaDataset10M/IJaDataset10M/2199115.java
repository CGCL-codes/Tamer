package org.amityregion5.projectx.common.communication.messages;

/**
 * A Message that contains text.
 * 
 * @see ChatMessage
 * @see IntroduceMessage
 * @see AnnounceMessage
 * 
 * @author Joe Stein
 * @author Daniel Centore
 */
public abstract class TextualMessage extends Message {

    private static final long serialVersionUID = 119L;

    private String text;

    /**
     * Creates a textual message
     * @param text The text it will contain
     */
    public TextualMessage(String text) {
        this.text = text;
    }

    /**
     * @return The text of this message
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
