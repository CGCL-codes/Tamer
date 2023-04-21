package org.grailrtls.libworldmodel.client.protocol.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginAliasMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginAliasMessage.OriginAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginAliasEncoder implements MessageEncoder<OriginAliasMessage> {

    /**
	 * Logging facility for this class.
	 */
    private static final Logger log = LoggerFactory.getLogger(OriginAliasEncoder.class);

    @Override
    public void encode(IoSession session, OriginAliasMessage message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(message.getMessageLength() + 4);
        buffer.putInt(message.getMessageLength());
        buffer.put(message.getMessageType());
        if (message.getAliases() == null) {
            buffer.putInt(0);
        } else {
            OriginAlias[] aliases = message.getAliases();
            buffer.putInt(aliases.length);
            for (OriginAlias alias : aliases) {
                buffer.putInt(alias.aliasNumber);
                byte[] aliasNameByte = alias.aliasName.getBytes("UTF-16BE");
                buffer.putInt(aliasNameByte.length);
                buffer.put(aliasNameByte);
            }
        }
        buffer.flip();
        out.write(buffer);
        buffer.free();
    }
}
