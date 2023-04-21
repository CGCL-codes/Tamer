package org.grailrtls.libworldmodel.client.protocol.codec;

import java.util.ArrayList;
import java.util.Date;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.grailrtls.libworldmodel.client.protocol.messages.AttributeAliasMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.DataResponseMessage;
import org.grailrtls.libworldmodel.client.protocol.messages.DataResponseMessage.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataResponseDecoder implements MessageDecoder {

    /**
	 * Logging facility for this class.
	 */
    private static final Logger log = LoggerFactory.getLogger(DataResponseDecoder.class);

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
            if (messageType == DataResponseMessage.MESSAGE_TYPE) {
                return MessageDecoderResult.OK;
            }
            return MessageDecoderResult.NOT_OK;
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
        DataResponseMessage message = new DataResponseMessage();
        int messageLength = buffer.getInt();
        byte messageType = buffer.get();
        --messageLength;
        int uriLength = buffer.getInt();
        messageLength -= 4;
        if (uriLength == 0) {
            log.error("URI length is 0!");
            return MessageDecoderResult.NOT_OK;
        }
        byte[] uriBytes = new byte[uriLength];
        buffer.get(uriBytes);
        messageLength -= uriLength;
        String uri = new String(uriBytes, "UTF-16BE");
        message.setUri(uri);
        int ticketNumber = buffer.getInt();
        messageLength -= 4;
        message.setTicketNumber(ticketNumber);
        int numAttributes = buffer.getInt();
        messageLength -= 4;
        if (messageLength > 0) {
            ArrayList<Attribute> attributes = new ArrayList<Attribute>();
            while (messageLength > 0) {
                Attribute attrib = new Attribute();
                int attribNameAlias = buffer.getInt();
                messageLength -= 4;
                attrib.setAttributeNameAlias(attribNameAlias);
                long creationDate = buffer.getLong();
                messageLength -= 8;
                attrib.setCreationDate(creationDate);
                long expirationDate = buffer.getLong();
                messageLength -= 8;
                attrib.setExpirationDate(expirationDate);
                int originNameAlias = buffer.getInt();
                messageLength -= 4;
                attrib.setOriginNameAlias(originNameAlias);
                int dataLength = buffer.getInt();
                messageLength -= 4;
                if (dataLength > 0) {
                    byte[] data = new byte[dataLength];
                    buffer.get(data);
                    messageLength -= dataLength;
                    attrib.setData(data);
                }
                attributes.add(attrib);
            }
            if (attributes.size() > 0) {
                message.setAttributes(attributes.toArray(new Attribute[attributes.size()]));
            }
        }
        out.write(message);
        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
    }
}
