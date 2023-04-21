package org.dcm4che2.net.codec;

import java.util.IdentityHashMap;
import java.util.Map;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.protocol.ProtocolEncoder;
import org.apache.mina.protocol.ProtocolEncoderOutput;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.ProtocolViolationException;
import org.dcm4che2.net.pdu.AAbort;
import org.dcm4che2.net.pdu.AAssociateAC;
import org.dcm4che2.net.pdu.AAssociateRQ;
import org.dcm4che2.net.pdu.AReleaseRP;
import org.dcm4che2.net.pdu.AReleaseRQ;
import org.dcm4che2.net.pdu.PDU;
import org.dcm4che2.net.pdu.PDataTF;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2005-10-05 15:40:23 -0400 (Wed, 05 Oct 2005) $
 * @since Sep 20, 2005
 */
public class DULProtocolEncoder implements ProtocolEncoder {

    private final Map encoders = new IdentityHashMap(6);

    public DULProtocolEncoder() {
        encoders.put(AAssociateRQ.class, new AAssociateRQEncoder());
        encoders.put(AAssociateAC.class, new AAssociateACEncoder());
        encoders.put(PDataTF.class, new PDataTFEncoder());
        encoders.put(AReleaseRQ.class, new AReleaseRQEncoder());
        encoders.put(AReleaseRP.class, new AReleaseRPEncoder());
        encoders.put(AAbort.class, new AAbortEncoder());
    }

    public void encode(ProtocolSession session, Object message, ProtocolEncoderOutput out) throws ProtocolViolationException {
        Class type = message.getClass();
        PDUEncoder encoder = (PDUEncoder) encoders.get(type);
        if (encoder == null) {
            throw new ProtocolViolationException("Unexpected message type: " + type);
        }
        ByteBuffer buf = encoder.encodePDU(session, (PDU) message);
        buf.flip();
        out.write(buf);
    }
}
