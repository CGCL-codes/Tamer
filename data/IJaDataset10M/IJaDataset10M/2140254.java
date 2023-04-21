package ru.javatalks.net.sc;

/**
 * ConnectionListener - interface to be implemented by any class interested in connection
 * creation/destruction events
 *
 * @author Eugene Matyushkin aka Skipy
 * @version $Id: ConnectionListener.java 4 2011-03-01 15:08:24Z javatalks $
 * @since 01.03.2011
 */
public interface ConnectionListener {

    /**
     * Connection creation event callback
     *
     * @param descriptor created connection descriptor
     */
    public void connectionCreated(ConnectionDescriptor descriptor);

    /**
     * Connection destruction event callback
     *
     * @param descriptor destroyed connection descriptor
     */
    public void connectionDestroyed(ConnectionDescriptor descriptor);
}
