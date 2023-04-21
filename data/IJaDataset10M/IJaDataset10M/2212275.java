package net.sf.isnake.codec.serverEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.sf.isnake.protocol.Information;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes on the server side, information about the player.
 *
 * @author Jitendra Harlalka  implements.java@gmail.com
 * @version $Id$
 */
public class InformationServerEncoder implements MessageEncoder {

    Logger log = LoggerFactory.getLogger(InformationServerEncoder.class);

    private static Set<Class<?>> TYPE;

    static {
        Set<Class<?>> type = new HashSet<Class<?>>();
        type.add(Information.class);
        TYPE = Collections.unmodifiableSet(type);
    }

    public Set<Class<?>> getMessageTypes() {
        return TYPE;
    }

    /** Encodes information about player*/
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        Information in = (Information) message;
        String str = new String();
        log.debug("Server provided id={}", in.getId());
        str = str + in.getId() + ":" + in.getName() + ":" + in.getColor().getRGB() + ":" + in.getLocation();
        log.debug("Information Server: Received Output={}", (short) str.length() + str);
        ByteBuffer buffer = ByteBuffer.allocate(2 + 2 + str.length());
        buffer.put(new String("IN").getBytes());
        buffer.putShort((short) str.length());
        buffer.put(str.getBytes());
        buffer.flip();
        out.write(buffer);
        out.flush();
    }
}
