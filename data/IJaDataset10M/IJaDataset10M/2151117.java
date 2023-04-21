package net.sf.isnake.codec.clientDecoder;

import net.sf.isnake.protocol.Information;
import java.awt.Color;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InformationClientDecoder implements MessageDecoder {

    private Logger log = LoggerFactory.getLogger(InformationClientDecoder.class);

    /** Creates a new instance of InformationClientDecoder */
    public MessageDecoderResult decodable(IoSession session, ByteBuffer in) {
        if (in.remaining() < 2) return MessageDecoderResult.NEED_DATA;
        byte by[] = new byte[2];
        in.get(by);
        String str = new String(by);
        if (str.equals(new String("IN"))) return MessageDecoderResult.OK; else return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
        Boolean headerRead = (Boolean) session.getAttachment();
        if (headerRead == null && in.remaining() >= 2) {
            in.getShort();
            headerRead = new Boolean(Boolean.TRUE);
            session.setAttachment(headerRead);
        }
        if (headerRead.booleanValue() && in.prefixedDataAvailable(2)) {
            Information info = new Information();
            short len = in.getShort();
            byte[] inf = new byte[len];
            in.get(inf);
            String[] str = (new String(inf)).split(":");
            log.debug("Id at client id={}\n", str[0]);
            info.setId((byte) (Integer.parseInt(str[0])));
            info.setName(str[1]);
            info.setColor(new Color(Integer.parseInt(str[2])));
            info.setLocation(str[3]);
            session.setAttachment(null);
            out.write(info);
            return MessageDecoderResult.OK;
        }
        return MessageDecoderResult.NEED_DATA;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        session.setAttachment(null);
    }
}
