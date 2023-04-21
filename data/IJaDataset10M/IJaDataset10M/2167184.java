package net.sf.alc.connection;

/**
 * Generic connection interface. This interface provides most of the method
 * calls required to perform operations on the connection.
 * <p/>
 */
public interface Connection {

    /**
    * Sends a aMessage to the other end of the connection. If notification is
    * required, the messageSent method of the registered connection listeners is
    * invoked.
    * <p/>
    * <p/>
    * Implementation of this interface are free to keep a reference to the
    * aMessage given as argument until it is being sent. So it is important not
    * to change this aMessage until the sent notification is received by the
    * listeners.
    * 
    * @param aMessage
    *           The message
    * @param aListener
    *           If not null, the method messageSent(Connection, aEvtData) is
    *           inovked on the specified aListener.
    * @param aEvtData
    *           passed to the aListener messageSent method. with this object as
    *           an argument.
    * @throws ConnectionSendException
    */
    public void sendMessage(Object aMessage, SendNotificationListener aListener, Object aEvtData) throws ConnectionSendException;

    /**
    * Used to indicates that this connection will not be sending any more
    * messages.
    */
    public void shutdownOutput();

    /**
    * Opens the connection. No operation can be performed until this method is
    * invoked.
    * 
    * @throws ConnectionException
    */
    public void open() throws ConnectionException;

    /**
    * Closes the connection. No more operation can be performed after this
    * method is invoked.
    */
    public void close();

    /**
    * Activates reading. Reading is initially disabled (after <code>open</code>)
    * and must be explicitely enabled in order to start receiving messges. It is
    * also disabled every time a message is received. Therefore, at least one of
    * the registered listeners must invoke this method on
    * <code>ConnectionListener.messageReceived</code> handler.
    */
    public void activateReading();

    /**
    * Registers a connection aListener. A aListener can be registered several
    * times, and needs to unregistered as many times as it was added.
    * 
    * @param aListener
    */
    void registerListener(ConnectionListener aListener);

    /**
    * Unregisters a aListener.
    * 
    * @param aListener
    */
    boolean unregisterListener(ConnectionListener aListener);

    /**
    * Secures the connection. If the connection is already secure, does not do
    * anything. If the connection is not secure, an attempt is made to make it
    * secure. If the attempt fails, a ConnectionException is thrown.
    * 
    * @param aIsClient
    * @throws ConnectionException
    *            if the attempt to secure the connection fails.
    */
    void setSecure(boolean aIsClient) throws ConnectionException;

    /**
    * Returns whether the connection is secure or not.
    * 
    * @return whether the connection is secure or not.
    */
    boolean isSecure();
}
