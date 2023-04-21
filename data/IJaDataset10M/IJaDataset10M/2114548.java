package com.itbs.aimcer.commune;

import com.itbs.aimcer.bean.Contact;
import com.itbs.aimcer.bean.Message;
import com.itbs.aimcer.bean.Nameable;
import com.itbs.aimcer.bean.Status;

/**
 * @author Alex Rass
 * @since Sep 22, 2004
 */
public interface ConnectionEventListener {

    /**
     * Sent before connection is attempted
     * @param connection in context
     */
    void connectionInitiated(Connection connection);

    /**
     * Recevied a message.
     * Called with incoming and outgoing messages from any connection.
     * @param connection connection
     * @param message message
     * @return false if noone else needs to see this.
     * @throws Exception on error
     */
    boolean messageReceived(MessageSupport connection, Message message) throws Exception;

    boolean emailReceived(MessageSupport connection, Message message) throws Exception;

    /**
     * Tells that other side is typing a message.
     * @param connection on which the notification is sent
     * @param contact contact reference
     */
    void typingNotificationReceived(MessageSupport connection, Nameable contact);

    /**
     * Connection with server was interrupted.
     * This is a failure to maintain a connection.
     * @param connection itself
     */
    void connectionLost(Connection connection);

    /**
     * Connection with server has failed.
     * This is a Failure to connect.
     * @param connection itself
     * @param message to display
     */
    void connectionFailed(Connection connection, String message);

    /**
     * Indicates we are now logged in and can start using the connection.
     * Place to run connection-related properties etc.
     * @param connection that has finished stabilizing
     */
    void connectionEstablished(Connection connection);

    /**
     * Nameable's status changed.
     * @param connection connection
     * @param contact contact with updated status
     * @param oldStatus status of the contact before this event happened.
     */
    void statusChanged(Connection connection, Contact contact, Status oldStatus);

    /**
     * Statuses for contacts that belong to this connection have changed.
     * @param connection connection
     */
    void statusChanged(Connection connection);

    /**
     * A previously requested icon has arrived.
     * Icon will be a part of the contact.
     *
     * @param connection connection
     * @param contact contact
     */
    void pictureReceived(IconSupport connection, Contact contact);

    /**
     * Other side requested a file transfer.
     * @param connection connection
     * @param contact who initiated msg
     * @param filename proposed name of file
     * @param description of the file
     * @param connectionInfo  your private object used to store protocol specific data
     */
    void fileReceiveRequested(FileTransferSupport connection, Contact contact, String filename, String description, Object connectionInfo);

    /**
     * Gets called when an assynchronous error occurs.
     * @param message to display
     * @param exception exception for tracing
     */
    void errorOccured(String message, Exception exception);

    /**
     * Determines if the contact is ok to be added.
     * @param user requesting
     * @param connection on which this happened.
     * @return true if you have no objections to this contact adding you.
     */
    public boolean contactRequestReceived(final String user, final MessageSupport connection);
}
