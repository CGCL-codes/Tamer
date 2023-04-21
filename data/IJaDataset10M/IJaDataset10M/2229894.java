package net.sf.btw.btlib;

/**
 * Local peer listener.
 * <p>
 * Defines methods that get called for individual events appearing on the local
 * peer side of a connection, like
 * <ul>
 * <li>new connection establishment.</li>
 * <li>data reception from the remote peer</li>
 * <li>connection termination</li>
 * </ul>
 * </p>
 * <p>
 * This listener is used for both client and server roles of a local peer. It is
 * up to the author, whether decides to
 * <ul>
 * <li>use two separate listeners, one to be passed to
 * {@link LocalPeer#configureAsClient(RemotePeer, ILocalPeerListener)}, the
 * other to {@link LocalPeer#configureAsServer(byte, ILocalPeerListener)}, or</li>
 * <li>use single listener (passed to both methods mentioned above), handling
 * events for both client and server part of an application, distinguishing
 * current role by calling {@link LocalPeer#isServer} and
 * {@link LocalPeer#isClient} in each method.</li>
 * </ul>
 * </p>
 * <p>
 * <strong>Warning:</strong> the listener should not let any exception
 * propagate from event methods, otherwise an unwanted effect may occur, such as
 * connections being closed and/or abandoned.
 * </p>
 * 
 * @author Jan Tomka, Martin Vysny
 */
public interface ILocalPeerListener {

    /**
	 * Notifies local peer about the connection establishment.
	 * <p>
	 * In case local peer acts as a server, this method is called for each new
	 * client accepted.
	 * </p>
	 * <p>
	 * In case local peer acts as client, this method is called after connection
	 * is successfully established to a server.
	 * </p>
	 * <p>
	 * The connection is guaranteed to be established before this method is
	 * called, so that {@link RemotePeer#send} or
	 * {@link RemotePeer#disconnect()} methods may be called directly from this
	 * method.
	 * </p>
	 * <p>
	 * Note, however, calling {@link RemotePeer#disconnect()} from this method
	 * invalidates the remotePeer referenced, which should not be used
	 * afterwards.
	 * </p>
	 * 
	 * @param remotePeer
	 *            Remote peer the connection has been established with.
	 */
    void connected(final RemotePeer remotePeer);

    /**
	 * Notifies local peer about data received.
	 * <p>
	 * This method is called anytime the data got read from the connection to
	 * the remote peer, no matter it is a server or client.
	 * </p>
	 * <p>
	 * Data is guaranteed to be successfully copied into the data buffer before
	 * this method is called. Though, it may be zero bytes in length in case of
	 * an empty packet got received.
	 * </p>
	 * 
	 * @param remotePeer
	 *            Remote peer the data came from.
	 * @param data
	 *            Byte array buffer with data received from the remote peer.
	 * @param dataLength
	 *            number of valid bytes in the data buffer.
	 */
    void dataReceived(final RemotePeer remotePeer, final byte[] data, final int dataLength);

    /**
	 * Notifies local peer about the connection termination.
	 * <p>
	 * This method is called when a connection with a remote peer gets
	 * terminated, either in clean way, or as a result of an error.
	 * </p>
	 * <p>
	 * This method may be called without connected being called before. In this
	 * case, remotePeer value of <code>null</code> and non-null ex indicates
	 * the error occured on the local side even before the connection has been
	 * established.
	 * </p>
	 * 
	 * @param remotePeer
	 *            Remote peer the connection with got terminated.
	 * @param ex
	 *            If non-null, an exception thrown while communicating with the
	 *            remote peer, that caused the connection termination.
	 */
    void disconnected(final RemotePeer remotePeer, Exception ex);
}
