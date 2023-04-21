package pl.edu.pjwstk.mteam.pubsub.message.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import pl.edu.pjwstk.mteam.core.NodeInfo;
import pl.edu.pjwstk.mteam.pubsub.core.PubSubConstants;

/**
 * Class representing subscribe request.
 * 
 * @author Paulina Adamska s3529@pjwstk.edu.pl
 */
public class SubscribeRequest extends PubSubRequest {

    /**
	 * Time, after which node will have to resubscribe
	 */
    private long expirationTime;

    /**
	 * Index of last received event from topic history
	 */
    private int eventIndex;

    /**
	 * Distance between subscriber's id and topic id
	 */
    private int distance;

    /**
	 * Creates new subscribe request with default parameters. Used only by PubSubMessage 
	 * class.
	 */
    public SubscribeRequest() {
        super(new NodeInfo("127.0.0.1", 0), new NodeInfo("127.0.0.1", 0), "", PubSubConstants.MSG_SUBSCRIBE, 0);
    }

    /**
	 * Creates new subscribe request with specified parameters.
	 * @param transId Transaction this response is associated with.
	 * @param src Message sender.
	 * @param dest Message destination.
	 * @param topicID ID of the topic this response is associated with.
	 */
    public SubscribeRequest(int transId, NodeInfo src, NodeInfo dest, String topicID, long expTime, int evntIndex, int dist) {
        super(src, dest, topicID, PubSubConstants.MSG_SUBSCRIBE, transId);
        expirationTime = expTime;
        eventIndex = evntIndex;
        distance = dist;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public int getLastEventIndex() {
        return eventIndex;
    }

    public int getDistance() {
        return distance;
    }

    /**
	 * Prepares subscribe request for sending.
	 * @return Bytes to send.
	 */
    public byte[] encode() {
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        DataOutputStream dtstr = new DataOutputStream(ostr);
        try {
            byte[] header = super.encode();
            dtstr.write(header);
            dtstr.writeLong(expirationTime);
            dtstr.writeInt(eventIndex);
            dtstr.writeInt(distance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ostr.toByteArray();
    }

    /**
	 * Parses type-dependent message contents.
	 * @param stream Received byte buffer.
	 * @param offset Number of bytes reserved for headers (they will be skipped while 
	 * 				 parsing).
	 */
    public void parse(byte[] stream, int offset) {
        ByteArrayInputStream istream = new ByteArrayInputStream(stream);
        DataInputStream dtstr = new DataInputStream(istream);
        try {
            istream.skip(offset);
            super.parse(stream, offset);
            istream.skip(super.getByteLength());
            expirationTime = dtstr.readLong();
            eventIndex = dtstr.readInt();
            distance = dtstr.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getEventIndex() {
        return this.eventIndex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscribeRequest");
        sb.append(", topicID: ");
        sb.append(getTopicID());
        sb.append(", transID: ");
        sb.append(getTransactionID());
        sb.append(", sourceName: ");
        sb.append(getSourceInfo().getName());
        return sb.toString();
    }
}
