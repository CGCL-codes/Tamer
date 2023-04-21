package cu.ftpd.events;

import cu.ftpd.Connection;

/**
 * Implement this interface to handle "before" events.
 *
 * @author Markus Jevring <markus@jevring.net>
 * @since 2008-jan-25 - 17:41:25
 * @version $Id: BeforeEventHandler.java 258 2008-10-26 12:47:23Z jevring $
 */
public interface BeforeEventHandler {

    /**
     * This method is invoked before an event is executed in the ftpd.<br>
     * These event handlers need to be registered with the central event handler
     * to be runnable. See data/cuftpd.xml
     *
     * To write information to the control connection, use <code>connection.respond("XXX message");</code>
     * where XXX is the response code in question.
     *
     * Excution can be halted by returning <code>false</code> from this method.
     *
     * @param event the event containing the information about what happened.
     * @param connection the connection that caused the event. This can be used to write information to the user.
     * @return false if further processing of this event and command should <b>not</b> take place, true otherwise.
     */
    public boolean handleEvent(Event event, Connection connection);
}
