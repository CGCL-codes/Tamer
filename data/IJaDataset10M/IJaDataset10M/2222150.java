package pl.edu.pjwstk.p2pp.messages.responses;

import java.util.Vector;
import pl.edu.pjwstk.p2pp.messages.P2PPMessage;
import pl.edu.pjwstk.p2pp.objects.AddressInfo;
import pl.edu.pjwstk.p2pp.objects.GeneralObject;
import pl.edu.pjwstk.p2pp.objects.PeerID;
import pl.edu.pjwstk.p2pp.objects.PeerInfo;
import pl.edu.pjwstk.p2pp.objects.UnhashedID;
import pl.edu.pjwstk.p2pp.objects.UnsupportedGeneralObjectException;
import pl.edu.pjwstk.p2pp.util.ByteUtils;

/**
 * NextHop response as defined in P2PP specification (draft 01).
 * 
 * @author Maciej Skorupka s3874@pjwstk.edu.pl
 * 
 */
public class NextHopResponse extends Response {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NextHopResponse=[message=[" + super.toString() + "header=[" + getHeader() + "], senderPeerInfo=[");
        if (senderPeerInfo != null) {
            builder.append(senderPeerInfo.toString());
        } else {
            builder.append(senderPeerInfo);
        }
        builder.append("], nextHopPeerInfo=[");
        if (nextHopPeerInfo != null) {
            builder.append(nextHopPeerInfo.toString());
        } else {
            builder.append(nextHopPeerInfo);
        }
        return builder.toString() + "]]";
    }

    /**
	 * PeerInfo describing a peer that has sent this response.
	 */
    private PeerInfo senderPeerInfo;

    /**
	 * PeerInfo object describing next hop.
	 */
    private PeerInfo nextHopPeerInfo;

    /**
	 * Creates empty response object.
	 */
    public NextHopResponse() {
    }

    /**
	 * 
	 * @param protocolVersion
	 * @param isSentByPeer
	 * @param isRecursive
	 * @param requestOrResponseType
	 * @param ttl
	 * @param transactionID
	 * @param sourceID
	 * @param responseID
	 * @param isOverReliable
	 * @param isEncrypted
	 * @param senderPeerInfo
	 * @param nextHop
	 */
    public NextHopResponse(boolean[] protocolVersion, boolean isSentByPeer, boolean isRecursive, byte requestOrResponseType, byte ttl, byte[] transactionID, byte[] sourceID, byte[] responseID, boolean isOverReliable, boolean isEncrypted, PeerInfo senderPeerInfo, PeerInfo nextHop) {
        super(protocolVersion, P2PPMessage.RESPONSE_ACK_MESSAGE_TYPE, false, isSentByPeer, isRecursive, RESPONSE_CODE_NEXT_HOP_BITS_ARRAY, requestOrResponseType, ttl, transactionID, sourceID, responseID, isOverReliable, isEncrypted);
        this.senderPeerInfo = senderPeerInfo;
        this.nextHopPeerInfo = nextHop;
    }

    @Override
    public void addObject(GeneralObject object) throws UnsupportedGeneralObjectException {
        if (object instanceof PeerInfo) {
            if (senderPeerInfo == null) {
                senderPeerInfo = (PeerInfo) object;
            } else if (nextHopPeerInfo == null) {
                nextHopPeerInfo = (PeerInfo) object;
            } else {
                throw new UnsupportedGeneralObjectException("NextHopResponse can't contain more PeerInfo subobjects.");
            }
        } else {
            throw new UnsupportedGeneralObjectException("NextHopResponse can't contain " + object.getClass().getName() + " as subobject.");
        }
    }

    @Override
    public PeerInfo getPeerInfo() {
        return senderPeerInfo;
    }

    /**
	 * Returns a PeerInfo object describing next hop.
	 * 
	 * @return
	 */
    public PeerInfo getNextHopPeerInfo() {
        return nextHopPeerInfo;
    }

    @Override
    public byte[] asBytes() {
        return asBytes(getBitsCount());
    }

    @Override
    protected byte[] asBytes(int bitsCount) {
        byte[] bytes = super.asBytes(bitsCount);
        int currentIndex = super.getBitsCount();
        ByteUtils.addByteArrayToArrayAtByteIndex(senderPeerInfo.asBytes(), bytes, currentIndex / 8);
        currentIndex += senderPeerInfo.getBitsCount();
        ByteUtils.addByteArrayToArrayAtByteIndex(nextHopPeerInfo.asBytes(), bytes, currentIndex / 8);
        return bytes;
    }

    @Override
    public int getBitsCount() {
        return super.getBitsCount() + senderPeerInfo.getBitsCount() + nextHopPeerInfo.getBitsCount();
    }

    @Override
    public boolean verify() {
        boolean result = true;
        if (senderPeerInfo == null || nextHopPeerInfo == null) {
            result = false;
        } else {
            PeerID peerID = senderPeerInfo.getPeerID();
            if (peerID == null) {
                result = false;
            } else {
                if (peerID.getPeerIDBytes() == null) {
                    result = false;
                }
            }
            if (result) {
                UnhashedID unhashedID = senderPeerInfo.getUnhashedID();
                if (unhashedID == null) {
                    result = false;
                } else {
                    if (unhashedID.getUnhashedIDValue() == null) {
                        result = false;
                    }
                }
            }
            if (result) {
                Vector<AddressInfo> addressInfos = senderPeerInfo.getAddressInfos();
                if (addressInfos == null) {
                    result = false;
                } else if (addressInfos.size() <= 0) {
                    result = false;
                }
            }
            peerID = nextHopPeerInfo.getPeerID();
            if (peerID == null) {
                result = false;
            } else {
                if (peerID.getPeerIDBytes() == null) {
                    result = false;
                }
            }
            if (result) {
                UnhashedID unhashedID = nextHopPeerInfo.getUnhashedID();
                if (unhashedID == null) {
                    result = false;
                } else {
                    if (unhashedID.getUnhashedIDValue() == null) {
                        result = false;
                    }
                }
            }
            if (result) {
                Vector<AddressInfo> addressInfos = nextHopPeerInfo.getAddressInfos();
                if (addressInfos == null) {
                    result = false;
                } else if (addressInfos.size() <= 0) {
                    result = false;
                }
            }
        }
        return result;
    }
}
