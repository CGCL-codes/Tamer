package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RedoPacket extends Packet {

    public class ObjectDataBlock {

        public LLUUID ObjectID = null;

        public int getLength() {
            return 16;
        }

        public ObjectDataBlock() {
        }

        public ObjectDataBlock(ByteBuffer bytes) {
            ObjectID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ObjectID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- ObjectData --\n";
            try {
                output += "ObjectID: " + Helpers.toString(ObjectID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ObjectDataBlock createObjectDataBlock() {
        return new ObjectDataBlock();
    }

    public class AgentDataBlock {

        public LLUUID AgentID = null;

        public LLUUID SessionID = null;

        public LLUUID GroupID = null;

        public int getLength() {
            return 48;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
            SessionID = new LLUUID(bytes);
            GroupID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
            SessionID.GetBytes(bytes);
            GroupID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output += "SessionID: " + Helpers.toString(SessionID) + "\n";
                output += "GroupID: " + Helpers.toString(GroupID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public AgentDataBlock createAgentDataBlock() {
        return new AgentDataBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.Redo;
    }

    public ObjectDataBlock[] ObjectData;

    public AgentDataBlock AgentData;

    public RedoPacket() {
        header = new LowHeader();
        header.setID((short) 106);
        header.setReliable(true);
        ObjectData = new ObjectDataBlock[0];
        AgentData = new AgentDataBlock();
    }

    public RedoPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        int count = (int) bytes.get() & 0xFF;
        ObjectData = new ObjectDataBlock[count];
        for (int j = 0; j < count; j++) {
            ObjectData[j] = new ObjectDataBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public RedoPacket(Header head, ByteBuffer bytes) {
        header = head;
        int count = (int) bytes.get() & 0xFF;
        ObjectData = new ObjectDataBlock[count];
        for (int j = 0; j < count; j++) {
            ObjectData[j] = new ObjectDataBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += AgentData.getLength();
        ;
        length++;
        for (int j = 0; j < ObjectData.length; j++) {
            length += ObjectData[j].getLength();
        }
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        bytes.put((byte) ObjectData.length);
        for (int j = 0; j < ObjectData.length; j++) {
            ObjectData[j].ToBytes(bytes);
        }
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- Redo ---\n";
        for (int j = 0; j < ObjectData.length; j++) {
            output += ObjectData[j].toString() + "\n";
        }
        output += AgentData.toString() + "\n";
        return output;
    }
}
