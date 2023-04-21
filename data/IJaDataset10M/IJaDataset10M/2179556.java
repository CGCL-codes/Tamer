package pl.edu.pjwstk.p2pp.superpeer.messages.requests;

import pl.edu.pjwstk.p2pp.messages.P2PPMessage;
import pl.edu.pjwstk.p2pp.messages.requests.Request;
import pl.edu.pjwstk.p2pp.objects.*;
import pl.edu.pjwstk.p2pp.superpeer.messages.responses.IndexResponse;
import pl.edu.pjwstk.p2pp.util.ByteUtils;
import java.util.Vector;

public class IndexRequest extends Request {

    /**
     * RequestOptions object describing this request.
     */
    private RequestOptions requestOptions;

    /**
     * PeerInfo object describing
     */
    private PeerInfo publisherPeerInfo;

    private ResourceObject resourceObject;

    /**
     * Creates empty object. To be filled later. Convenient when creating object basing on received data.
     */
    public IndexRequest() {
    }

    /**
     * @param protocolVersion
     * @param isAcknowledgment
     * @param isSentByPeer
     * @param isRecursive
     * @param ttl
     * @param transactionID
     * @param sourceID
     * @param isOverReliable
     * @param isEncrypted
     * @param requestOptions    May be null.
     * @param publisherPeerInfo
     * @param resourceObject
     */
    public IndexRequest(boolean[] protocolVersion, boolean isAcknowledgment, boolean isSentByPeer, boolean isRecursive, byte ttl, byte[] transactionID, byte[] sourceID, boolean isOverReliable, boolean isEncrypted, RequestOptions requestOptions, PeerInfo publisherPeerInfo, ResourceObject resourceObject) {
        super(protocolVersion, isAcknowledgment, isSentByPeer, isRecursive, P2PPMessage.INDEX_MESSAGE_TYPE, ttl, transactionID, sourceID, isOverReliable, isEncrypted);
        this.requestOptions = requestOptions;
        this.publisherPeerInfo = publisherPeerInfo;
        this.resourceObject = resourceObject;
    }

    @Override
    public RequestOptions getRequestOptions() {
        return requestOptions;
    }

    @Override
    public void addObject(GeneralObject object) throws UnsupportedGeneralObjectException {
        if (object instanceof PeerInfo) {
            if (publisherPeerInfo == null) {
                publisherPeerInfo = (PeerInfo) object;
            } else {
                throw new UnsupportedGeneralObjectException("IndexRequest can't contain more than one PeerInfo object.");
            }
        } else if (object instanceof RequestOptions) {
            if (requestOptions == null) {
                requestOptions = (RequestOptions) object;
            } else {
                throw new UnsupportedGeneralObjectException("IndexRequest can't contain more than one RequestOptions object.");
            }
        } else if (object instanceof ResourceObject) {
            if (resourceObject == null) {
                this.resourceObject = (ResourceObject) object;
            } else {
                throw new UnsupportedGeneralObjectException("Request can't contain more than one ResourceObject object.");
            }
        } else {
            throw new UnsupportedGeneralObjectException("Request can't contain " + object.getClass().getName() + " object.");
        }
    }

    @Override
    public PeerInfo getPeerInfo() {
        return publisherPeerInfo;
    }

    @Override
    public byte[] asBytes() {
        return asBytes(getBitsCount());
    }

    public ResourceObject getResourceObject() {
        return this.resourceObject;
    }

    public void setResourceObject(ResourceObject resourceObject) {
        this.resourceObject = resourceObject;
    }

    public void setRequestOptions(RequestOptions requestOptions) {
        this.requestOptions = requestOptions;
    }

    @Override
    public boolean verify() {
        boolean result = true;
        if (publisherPeerInfo == null || this.resourceObject == null) {
            result = false;
        } else {
            PeerID peerID = publisherPeerInfo.getPeerID();
            if (peerID == null) {
                result = false;
            } else {
                if (peerID.getPeerIDBytes() == null) {
                    result = false;
                }
            }
            if (result) {
                UnhashedID unhashedID = publisherPeerInfo.getUnhashedID();
                if (unhashedID == null) {
                    result = false;
                } else {
                    if (unhashedID.getUnhashedIDValue() == null) {
                        result = false;
                    }
                }
            }
            if (result) {
                Vector<AddressInfo> addressInfos = publisherPeerInfo.getAddressInfos();
                if (addressInfos == null) {
                    result = false;
                } else if (addressInfos.size() <= 0) {
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    protected byte[] asBytes(int bitsCount) {
        byte[] bytes = super.asBytes(bitsCount);
        int byteIndex = super.getBitsCount() / 8;
        if (requestOptions != null) {
            ByteUtils.addByteArrayToArrayAtByteIndex(requestOptions.asBytes(), bytes, byteIndex);
            byteIndex += requestOptions.getBitsCount() / 8;
        }
        ByteUtils.addByteArrayToArrayAtByteIndex(publisherPeerInfo.asBytes(), bytes, byteIndex);
        byteIndex += publisherPeerInfo.getBitsCount() / 8;
        ByteUtils.addByteArrayToArrayAtByteIndex(this.resourceObject.asBytes(), bytes, byteIndex);
        return bytes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("IndexRequest=[message=[" + super.toString() + ", header=[" + getHeader() + "], publisherPeerInfo=[");
        if (publisherPeerInfo != null) {
            builder.append(publisherPeerInfo.toString());
        } else {
            builder.append(publisherPeerInfo);
        }
        builder.append("], requestOptions=[");
        if (requestOptions != null) {
            builder.append(requestOptions.toString());
        } else {
            builder.append(requestOptions);
        }
        builder.append("], resourceObject=[");
        if (this.resourceObject != null) {
            builder.append(this.resourceObject.toString());
        } else {
            builder.append(this.resourceObject);
        }
        builder.append("]]");
        return builder.toString();
    }

    @Override
    public int getBitsCount() {
        int additionalBits = 0;
        if (requestOptions != null) {
            additionalBits += requestOptions.getBitsCount();
        }
        return super.getBitsCount() + additionalBits + publisherPeerInfo.getBitsCount() + resourceObject.getBitsCount();
    }

    /**
     * <p>
     * Creates response for this message. Response is responseACK (not acknowledgment). Response contains given response
     * code. PeerInfo given as argument is added to response as PeerInfo of peer that consumed this request. Response is
     * for iterative routing.
     * </p>
     * <p>
     * Response contains some copies of this request: protocolVersion, transactionID, sourceID, overReliable, encrypted.
     * </p>
     *
     * @param responseCode
     * @param ownPeerInfo
     * @param expiresInSeconds
     * @return
     */
    public IndexResponse createResponse(boolean[] responseCode, PeerInfo ownPeerInfo, int expiresInSeconds) {
        return new IndexResponse(protocolVersion, P2PPMessage.RESPONSE_ACK_MESSAGE_TYPE, false, true, false, responseCode, (byte) 255, transactionID, sourceID, ownPeerInfo.getPeerID().getPeerIDBytes(), overReliable, encrypted, ownPeerInfo);
    }
}
