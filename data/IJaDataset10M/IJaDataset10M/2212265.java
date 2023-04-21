package net.sourceforge.autofeed;

/**
 * Thrown when an item is not unique, when considered with a given key
 * @author miguelinux
 */
public class NotUniqueException extends Exception {

    Object notUniqueItem;

    /**
     * Creates a new instance of <code>NotUniqueException</code> specifying the
     * item that generated the exception by not being unique.
     * @param msg the detail message
     * @param notUniqueItem the object that caused the exception
     */
    public NotUniqueException(String msg, Object notUniqueItem) {
        super(msg);
        this.notUniqueItem = notUniqueItem;
    }

    /**
     * Constructs an instance of <code>NotUniqueException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotUniqueException(String msg) {
        this(msg, null);
    }

    /**
     * @return The object that caused the exception (null if it wasn't specified
     * or specified as null)
     */
    public Object getNotUniqueItem() {
        return notUniqueItem;
    }
}
