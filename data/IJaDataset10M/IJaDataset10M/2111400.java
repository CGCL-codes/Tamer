package org.mitre.scap.xccdf;

public class ItemNotFoundException extends Exception {

    /** the serial version UID */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of <code>ItemNotFoundException</code> without detail message.
     */
    public ItemNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ItemNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ItemNotFoundException(String msg) {
        super(msg);
    }
}
