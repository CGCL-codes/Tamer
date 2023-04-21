package net.redlightning.jbittorrent.interfaces;

/**
 * Represent the general structure of a protocol message. It must have a type.
 * 
 * @author Baptiste Dubuis (baptiste.dubuis@gmail.com)
 * @version 0.1
 */
public abstract class AbstractMessage {

    protected transient int type;

    private transient int priority = 0;

    public AbstractMessage() {
    }

    public AbstractMessage(final int type, final int priority) {
        this.type = type;
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getType() {
        return this.type;
    }

    public abstract byte[] generate();
}
