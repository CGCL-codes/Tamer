package pl.edu.pjwstk.net.message.TURN.attribute;

import pl.edu.pjwstk.net.message.TURN.TURNAttribute;
import pl.edu.pjwstk.net.message.TURN.TURNAttributeType;
import pl.edu.pjwstk.net.message.TURN.TURNMessage;
import pl.edu.pjwstk.net.proto.SupportedProtocols;
import pl.edu.pjwstk.types.ExtendedBitSet;

/**
 * @author Robert Strzelecki rstrzele@gmail.com
 * package pl.edu.pjwstk.net.message.TURN.attribute
 */
public class TURNRequestedTransport extends TURNAttribute {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TURNRequestedTransport.class);

    protected SupportedProtocols protocol = SupportedProtocols.UDP;

    public TURNRequestedTransport(TURNMessage turnMessage, ExtendedBitSet valueEBS, int messagePosition) {
        super(turnMessage, TURNAttributeType.REQUESTED_TRANSPORT, valueEBS, messagePosition);
    }

    public TURNRequestedTransport(TURNMessage turnMessage, int messagePosition) {
        super(turnMessage, TURNAttributeType.REQUESTED_TRANSPORT, messagePosition);
        this.setTransportType(protocol);
    }

    public void setTransportType(SupportedProtocols proto) {
        if (proto.isActive()) {
            protocol = proto;
            attribute.set(32, protocol.getProtocolCode());
            this.setLength(protocol.getProtocolCode().getFixedLength() / 8);
        } else {
            logger.debug("Unsupported protocol");
        }
    }

    public String toString() {
        if (protocol != null) {
            return "(" + protocol.toString() + ")";
        } else {
            return "(unspecified)";
        }
    }
}
