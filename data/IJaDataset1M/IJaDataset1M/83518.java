package org.simpleframework.http.transport;

import java.nio.channels.SocketChannel;

/**
 * The <code>Coalescer</code> object represents a means to coalesce
 * packets at a single point before being written to the socket. It
 * is used to ensure all packets are queued in order of sequence
 * number. Any packets that are partially written to the underlying
 * socket can be coalesced in to a single packet so that a larger
 * packet can be delivered over the network.
 * 
 * @author Niall Gallagher
 * 
 * @see org.simpleframework.http.transport.Flusher
 */
interface Coalescer {

    /**
    * This provides the socket for the coalescer. Providing this 
    * enables a <code>Reactor</code> to be used to determine when
    * the coalescer is write ready and thus when the coalescer can
    * be flushed if it contains packets that have not been written.
    * 
    * @return this returns the socket associated with this
    */
    public SocketChannel getSocket();

    /**
    * This is used to write the packets to the coalescer which will
    * be either written to the underlying socket or queued until
    * such time as the socket is write ready. This will return true
    * if the packet has been written to the underlying transport.
    * 
    * @param packet this is the packet that is the be written
    * 
    * @return true if the packet has been written to the transport
    */
    public boolean write(Packet packet) throws Exception;

    /**
    * This is used to flush all queued packets to the underlying
    * socket. If all of the queued packets have been fully written
    * then this returns true, otherwise this will return false.
    * 
    * @return true if all queued packets are flushed to the socket
    */
    public boolean flush() throws Exception;

    /**
    * This is used to close the coalescer and the underlying socket.
    * If a close is performed on the coalescer then no more bytes 
    * can be read from or written to the coalescer and the client 
    * will receive a connection close on their side.
    */
    public void close() throws Exception;
}
