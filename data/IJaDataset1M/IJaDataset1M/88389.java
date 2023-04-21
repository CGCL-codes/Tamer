package com.peterhi.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

/**
 *
 * @author YUN TAO
 */
public interface Sender {

    void send(SocketAddress socketAddress, ByteBuffer buf) throws IOException;
}
