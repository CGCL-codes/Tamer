package rtjdds.rtps.messages;

import rtjdds.rtps.messages.elements.EntityId;
import rtjdds.rtps.messages.elements.ParameterList;
import rtjdds.rtps.messages.elements.SequenceNumber;
import rtjdds.rtps.messages.elements.SerializedData;
import rtjdds.rtps.portable.CDROutputPacket;
import rtjdds.rtps.types.NOKEY_DATA;

public class NoKeyData extends Submessage {

    private EntityId readerId;

    private EntityId writerId;

    private SequenceNumber writerSN;

    private ParameterList inlineQoS;

    private SerializedData serializedData;

    /**
	 * Constructs a NoKeyData submessage that contains SerializedData
	 * @param readerId
	 * @param writerId
	 * @param writerSN
	 * @param inlineQoS
	 * @param serializedData
	 */
    public NoKeyData(EntityId readerId, EntityId writerId, SequenceNumber writerSN, ParameterList inlineQoS, SerializedData serializedData) {
        super(NOKEY_DATA.value);
        this.readerId = readerId;
        this.writerId = writerId;
        this.writerSN = writerSN;
        this.inlineQoS = inlineQoS;
        this.serializedData = serializedData;
        if (inlineQoS != null) {
            super.setFlagAt(1, true);
        }
        if (serializedData != null) {
            super.setFlagAt(2, true);
        }
    }

    protected void writeBody(CDROutputPacket os) {
        readerId.write(os);
        writerId.write(os);
        writerSN.write(os);
        if (this.getFlagAt(1)) {
            inlineQoS.write(os);
        }
        if (this.getFlagAt(2)) {
            serializedData.write(os);
        }
    }

    public ParameterList getInlineQoS() {
        return inlineQoS;
    }

    public EntityId getReaderId() {
        return readerId;
    }

    public SerializedData getSerializedData() {
        return serializedData;
    }

    public EntityId getWriterId() {
        return writerId;
    }

    public SequenceNumber getWriterSN() {
        return writerSN;
    }
}
