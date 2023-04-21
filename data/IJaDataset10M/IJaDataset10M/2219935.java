package com.thimbleware.jmemcached;

import org.apache.mina.common.ByteBuffer;
import java.io.Serializable;

/**
 * Represents an outbound response on the network stream.
 */
public class ResponseMessage implements Serializable {

    public ByteBuffer out;

    public ResponseMessage() {
        out = ByteBuffer.allocate(1024);
        out.setAutoExpand(true);
    }
}
