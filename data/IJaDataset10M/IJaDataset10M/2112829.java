package org.grailrtls.solver.protocol.codec;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.apache.mina.filter.util.SessionAttributeInitializingFilter;
import org.grailrtls.solver.protocol.messages.SampleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleEncoder implements MessageEncoder<SampleMessage> {

    private static final Logger log = LoggerFactory.getLogger(SampleEncoder.class);

    public void dispose(IoSession arg0) throws Exception {
    }

    public void encode(IoSession session, SampleMessage message, ProtocolEncoderOutput out) throws Exception {
        if (message.getLengthPrefix() < 0) {
            throw new IOException("Message length is negative.");
        }
        IoBuffer buffer = IoBuffer.allocate(message.getLengthPrefix() + 4);
        buffer.putInt(message.getLengthPrefix());
        buffer.put(message.getMessageType());
        buffer.put(message.getPhysicalLayer());
        buffer.put(message.getDeviceId());
        buffer.put(message.getReceiverId());
        buffer.putLong(message.getReceiverTimeStamp());
        buffer.putFloat(message.getRssi());
        if (message.getSensedData() != null) {
            buffer.put(message.getSensedData());
        }
        buffer.flip();
        out.write(buffer);
        buffer.free();
    }
}
