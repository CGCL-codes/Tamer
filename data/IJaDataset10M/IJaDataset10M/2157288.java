package com.aionemu.chatserver.network.netty.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

/**
 * @author ATracer
 */
public class PacketFrameDecoder extends LengthFieldBasedFrameDecoder {

    private static final int MAX_PACKET_LENGTH = 8192 * 2;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 2;

    private static final int LENGTH_FIELD_ADJUSTMENT = -2;

    private static final int INITIAL_BYTES_TO_STRIP = 2;

    public PacketFrameDecoder() {
        super(MAX_PACKET_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT, INITIAL_BYTES_TO_STRIP);
    }

    @Override
    protected Object decode(ChannelHandlerContext arg0, Channel arg1, ChannelBuffer arg2) throws Exception {
        return super.decode(arg0, arg1, arg2);
    }
}
