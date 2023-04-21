package org.grailrtls.libworldmodel.client.protocol.codec;

import java.util.ArrayList;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginAliasMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.OriginAliasMessage.OriginAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OriginAliasDecoder implements MessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(OriginAliasDecoder.class);

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer buffer) {
        if (buffer.prefixedDataAvailable(4, 65536)) {
            buffer.mark();
            int messageLength = buffer.getInt();
            if (messageLength < 1) {
                buffer.reset();
                return MessageDecoderResult.NOT_OK;
            }
            byte messageType = buffer.get();
            buffer.reset();
            if (messageType == OriginAliasMessage.MESSAGE_TYPE) {
                return MessageDecoderResult.OK;
            }
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
        OriginAliasMessage message = new OriginAliasMessage();
        int messageLength = buffer.getInt();
        byte messageType = buffer.get();
        --messageLength;
        int numAliases = buffer.getInt();
        messageLength -= 4;
        if (numAliases > 0) {
            ArrayList<OriginAlias> aliases = new ArrayList<OriginAlias>();
            log.debug("Decoding {} aliases.", numAliases);
            for (int i = 0; i < numAliases; ++i) {
                int aliasNumber = buffer.getInt();
                log.debug("Next origin number is {}.", aliasNumber);
                int nameLength = buffer.getInt();
                log.debug("Next origin name is {} bytes.", nameLength);
                messageLength -= 8;
                String name = "";
                if (nameLength != 0) {
                    byte[] nameBytes = new byte[nameLength];
                    buffer.get(nameBytes);
                    messageLength -= nameLength;
                    name = new String(nameBytes, "UTF-16BE");
                } else {
                    log.warn("World Model sent an empty origin name for alias number {}.", aliasNumber);
                }
                aliases.add(new OriginAlias(aliasNumber, name));
                log.debug("Alias[{}] {}->" + name, Integer.valueOf(i), Integer.valueOf(aliasNumber));
            }
            message.setAliases(aliases.toArray(new OriginAlias[] {}));
        }
        out.write(message);
        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
    }
}
