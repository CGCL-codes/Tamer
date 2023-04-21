package net.sf.jmyspaceiml.contact;

/**
 * The <code>ContactListener</code> interface is one of the interfaces used to
 * extend the functionality of MySpaceIM library.
 * <p>
 * Implements of this interface are used to be notified of roster changes.
 */
public interface ContactListener {

    /**
     * Invoked by the <code>ContactManager</code> when there has been a chance
     * to the stored roster.
     * 
     * @see ContactManager
     */
    void contactListUpdateReceived();
}
