package com.jme3.network.kernel;

import java.nio.ByteBuffer;

/**
 *  An abstract endpoint in a Kernel that can be used for
 *  sending/receiving messages within the kernel space.
 *
 *  @version   $Revision: 7049 $
 *  @author    Paul Speed
 */
public interface Endpoint {

    /**
     *  Returns an ID that is unique for this endpoint within its
     *  Kernel instance.
     */
    public long getId();

    /**
     *  Returns the transport specific remote address of this endpoint
     *  as a string.  This may or may not be unique per endpoint depending
     *  on the type of transport. 
     */
    public String getAddress();

    /**
     *  Returns the kernel to which this endpoint belongs.
     */
    public Kernel getKernel();

    /**
     *  Returns true if this endpoint is currently connected.
     */
    public boolean isConnected();

    /**
     *  Sends data to the other end of the connection represented
     *  by this endpoint.
     */
    public void send(ByteBuffer data);

    /**
     *  Closes this endpoint without flushing any of its
     *  currently enqueued outbound data.
     */
    public void close();

    /**
     *  Closes this endpoint, optionally flushing any queued
     *  data before closing.  As soon as this method is called,
     *  ne send() calls will fail with an exception... even while
     *  close() is still flushing the earlier queued messages.
     */
    public void close(boolean flushData);
}
