package pl.edu.pjwstk.net.message;

import java.util.Vector;
import pl.edu.pjwstk.net.ProtocolObject;
import pl.edu.pjwstk.net.TransportPacket;
import pl.edu.pjwstk.types.ExtendedBitSet;

/**
 * @author Robert Strzelecki rstrzele@gmail.com
 * package pl.edu.pjwstk.net.message
 */
public abstract class ProtocolMessage extends ProtocolObject implements ProtocolMessageInt {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ProtocolMessage.class);

    protected Vector<ProtocolAttribute> messageAttributes = new Vector<ProtocolAttribute>();

    protected int sizeOfHeader = 160;

    protected int sizeOfProtocolBits = 2;

    protected int sizeOfProtocolMessageType = 14;

    protected int sizeOfProtocolMessageLength = 16;

    protected int sizeOfProtocolMagicCookie = 32;

    protected int sizeOfTransactionID = 96;

    /**
	 * Protocol type (2 bits)
	 */
    protected ExtendedBitSet protocolBits = new ExtendedBitSet(2);

    /**
	 * Protocol Message type (14 bits)
	 */
    protected ExtendedBitSet protocolMessageType = new ExtendedBitSet(14);

    /**
	 * Protocol Message length
	 */
    protected ExtendedBitSet protocolMessageLength = new ExtendedBitSet(16);

    /**
	 * Message Magic Cookie
	 */
    protected ExtendedBitSet protocolMagicCookie = new ExtendedBitSet(32);

    /**
	 * Transaction ID (96 bits)
	 */
    private ExtendedBitSet transactionID = new ExtendedBitSet(96);

    public boolean generateTransactionID(int lengthOfTransactionID) {
        if (lengthOfTransactionID % 32 != 0) return false;
        setTransactionID(new ExtendedBitSet(Integer.toBinaryString((int) (Math.random() * Integer.MAX_VALUE)), lengthOfTransactionID, false, false));
        for (int i = 1; i < lengthOfTransactionID / 32; i++) {
            getTransactionID().set(i * 32, new ExtendedBitSet(Integer.toBinaryString((int) (Math.random() * Integer.MAX_VALUE)), 32, false, true));
        }
        return true;
    }

    public ProtocolMessage() {
        protocolBits.clear();
        protocolMessageType.clear();
        protocolMessageLength = new ExtendedBitSet(Integer.toBinaryString(sizeOfHeader / 8), 16, false, true);
        protocolMagicCookie.clear();
        getTransactionID().clear();
    }

    public ProtocolMessage(TransportPacket transportPacket) {
        ExtendedBitSet header = ExtendedBitSet.fromByteArray(transportPacket.packetBody, false);
        protocolBits = header.get(0, 2);
        protocolMessageType = header.get(2, 16);
        protocolMessageLength = header.get(16, 32);
        protocolMagicCookie = header.get(32, 64);
        setTransactionID(header.get(64, 160));
    }

    public ExtendedBitSet getHeader() {
        ExtendedBitSet header = new ExtendedBitSet(sizeOfHeader);
        header.add(protocolBits);
        header.add(protocolMessageType);
        header.add(protocolMessageLength);
        header.add(protocolMagicCookie);
        header.add(getTransactionID());
        return header;
    }

    public ExtendedBitSet getAttributes() {
        ExtendedBitSet att = new ExtendedBitSet(0, true);
        for (ProtocolAttribute currentAttribute : messageAttributes) {
            att.add(currentAttribute.getAttribute());
        }
        return att;
    }

    /**
	 * @param transactionID the transactionID to set
	 */
    public void setTransactionID(ExtendedBitSet transactionID) {
        logger.debug("Message Transaction ID set to " + transactionID.toString());
        this.transactionID = transactionID;
    }

    /**
	 * @return the transactionID
	 */
    public ExtendedBitSet getTransactionID() {
        return transactionID;
    }

    /**
	 * @return the Magic Cookie
	 */
    public ExtendedBitSet getMagicCookie() {
        return protocolMagicCookie;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        ExtendedBitSet ebs = getHeader();
        for (int i = 0; i < ebs.size(); i++) {
            strb.append((ebs.get(i) ? 1 : 0));
        }
        return strb.toString();
    }

    protected void computeLength() {
        int sizeOfMessage = 0;
        for (ProtocolAttribute currentAttribute : messageAttributes) {
            sizeOfMessage += currentAttribute.attribute.getFixedLength() / 8;
        }
        protocolMessageLength = new ExtendedBitSet(Integer.toBinaryString(sizeOfMessage), 16, false, true);
    }
}
