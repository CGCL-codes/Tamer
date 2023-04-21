package net.sf.isnake.codec.serverEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sf.isnake.protocol.ChatMessage;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes the chat message received from the client at server side.
 *
 * @author Jitendra Harlalka  implements.java@gmail.com
 * @version $Id$
 */
public class ChatServerEncoder implements MessageEncoder {

    Logger log = LoggerFactory.getLogger(ChatServerEncoder.class);

    private static Set<Class<?>> TYPE;

    static {
        Set<Class<?>> type = new HashSet<Class<?>>();
        type.add(ChatMessage.class);
        TYPE = Collections.unmodifiableSet(type);
    }

    public Set<Class<?>> getMessageTypes() {
        return TYPE;
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        ChatMessage cm = (ChatMessage) message;
        ByteBuffer buffer = ByteBuffer.allocate(2 + 1 + 1 + 2 + cm.getMessage().length());
        buffer.setAutoExpand(true);
        buffer.put(new String("CH").getBytes());
        buffer.put(cm.getFrom());
        buffer.put(cm.getTo());
        buffer.putShort((short) cm.getMessage().length());
        buffer.put(cm.getMessage().getBytes());
        buffer.flip();
        log.debug("Chat buffer={}", buffer);
        out.write(buffer);
        out.flush();
    }
}
