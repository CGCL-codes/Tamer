package org.exolab.jms.net.multiplexer;

/**
 * Protocol constants for the multiplexer
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.3 $ $Date: 2005/05/30 13:34:49 $
 */
interface Constants {

    /**
     * Magic no. used in stream verification.
     */
    int MAGIC = 0xBADDECAF;

    /**
     * Indicates that the packet contains protocol version.
     */
    int VERSION = 0x00000001;

    /**
     * Indicates that a packet is a request to open a new channel.
     */
    byte OPEN = 0x20;

    /**
     * Indicates that a packet is a request to close an existing channel.
     */
    byte CLOSE = 0x21;

    /**
     * Indicates the start of an invocation request.
     */
    byte REQUEST = 0x30;

    /**
     * Indicates the start of an invocation return.
     */
    byte RESPONSE = 0x31;

    /**
     * Indicates the continuation of a stream of data packets.
     */
    byte DATA = 0x32;

    /**
     * Indicates that the client is supplying user/password authentication
     * details.
     */
    byte AUTH_BASIC = 0x40;

    /**
     * Indicates that a client is supplying no authentication details.
     */
    byte AUTH_NONE = 0x41;

    /**
     * Indicates that connection has been accepted.
     */
    byte AUTH_OK = 0x4E;

    /**
     * Indicates that connection has been refused.
     */
    byte AUTH_DENIED = 0x4F;

    /**
     * Indicates that a packet is a ping request.
     */
    byte PING_REQUEST = 0x50;

    /**
     * Indicates that a packet is a ping response.
     */
    byte PING_RESPONSE = 0x51;

    /**
     * Indicates the no. of bytes read from the stream.
     */
    byte FLOW_READ = 0x60;

    /**
     * Indicates to close the connection.
     */
    byte SHUTDOWN = 0x70;
}
