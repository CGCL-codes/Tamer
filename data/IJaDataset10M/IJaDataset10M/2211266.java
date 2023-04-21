package pl.edu.pjwstk.p2pp.messages;

import pl.edu.pjwstk.p2pp.objects.GeneralObject;
import pl.edu.pjwstk.p2pp.objects.PeerInfo;
import pl.edu.pjwstk.p2pp.objects.UnsupportedGeneralObjectException;

/**
 * Class wrapping data for acknowledgement (as defined in P2PP specification draft 01).
 * 
 * @author Maciej Skorupka s3874@pjwstk.edu.pl
 * 
 */
public class Acknowledgment extends P2PPMessage {

    /**
	 * Creates empty Acknowledgment. Useful when creating this object basing on data received on socket.
	 */
    public Acknowledgment() {
        super();
    }

    /**
	 * Creates acknowledgement with sourceID and responseID.
	 * 
	 * @param protocolVersion
	 * @param messageType
	 * @param isSentByPeer
	 * @param isRecursive
	 * @param reservedOrResponseCode
	 * @param requestOrResponseType
	 * @param ttl
	 * @param transactionID
	 * @param sourceID
	 * @param responseID
	 * @param isOverReliable
	 * @param isEncrypted
	 */
    public Acknowledgment(boolean[] protocolVersion, boolean[] messageType, boolean isSentByPeer, boolean isRecursive, boolean[] reservedOrResponseCode, byte requestOrResponseType, byte ttl, byte[] transactionID, byte[] sourceID, byte[] responseID, boolean isOverReliable, boolean isEncrypted) {
        super(protocolVersion, messageType, true, isSentByPeer, isRecursive, reservedOrResponseCode, requestOrResponseType, ttl, transactionID, sourceID, responseID, isOverReliable, isEncrypted);
    }

    @Override
    public byte[] asBytes() {
        return asBytes(getBitsCount());
    }

    @Override
    protected byte[] asBytes(int bitsCount) {
        byte[] bytes = super.asBytes(bitsCount);
        return bytes;
    }

    @Override
    public int getBitsCount() {
        return super.getBitsCount();
    }

    @Override
    public void addObject(GeneralObject object) throws UnsupportedGeneralObjectException {
        throw new UnsupportedGeneralObjectException("Acknowledgment can't contain any subobject.");
    }

    @Override
    public PeerInfo getPeerInfo() {
        return null;
    }

    @Override
    public boolean verify() {
        return true;
    }

    @Override
    public String toString() {
        return "Acknowledgment=[message=[" + super.toString() + "], header=[" + getHeader() + "]";
    }
}
