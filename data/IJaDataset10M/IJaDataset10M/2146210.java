package de.knowwe.core.report;

/**
 * 
 * Message tied to Sections in the KDOM.
 * 
 * 
 * @author Jochen
 * @author Albrecht Striffler (denkbares GmbH)
 * 
 */
public final class Message {

    public enum Type {

        INFO, WARNING, ERROR
    }

    private final String text;

    private final Type type;

    public Message(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
	 * Returns the type of this message (error, warning or notice).
	 * 
	 * @created 01.12.2011
	 */
    public Type getType() {
        return this.type;
    }

    /**
	 * Returns the verbalization of this message. Will be rendered into the wiki
	 * page by the given MessageRenderer of the Type of the Section.
	 */
    public String getVerbalization() {
        return this.text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message otherMsg = (Message) obj;
            if (otherMsg.type.equals(this.type) && otherMsg.getVerbalization().equals(this.getVerbalization())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.type.hashCode() + this.getVerbalization().hashCode();
    }

    @Override
    public String toString() {
        return getVerbalization();
    }
}
