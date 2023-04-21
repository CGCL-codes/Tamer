package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ObjectMaterialPacket extends Packet {

    public class ObjectDataBlock {

        public byte Material = 0;

        public int ObjectLocalID = 0;

        public int getLength() {
            return 5;
        }

        public ObjectDataBlock() {
        }

        public ObjectDataBlock(ByteBuffer bytes) {
            Material = bytes.get();
            ObjectLocalID = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.put(Material);
            bytes.putInt(ObjectLocalID);
        }

        public String toString() {
            String output = "-- ObjectData --\n";
            try {
                output += "Material: " + Helpers.toString(Material) + "\n";
                output += "ObjectLocalID: " + Helpers.toString(ObjectLocalID) + "\n";
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

        public int getLength() {
            return 32;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
            SessionID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
            SessionID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output += "SessionID: " + Helpers.toString(SessionID) + "\n";
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
        return PacketType.ObjectMaterial;
    }

    public ObjectDataBlock[] ObjectData;

    public AgentDataBlock AgentData;

    public ObjectMaterialPacket() {
        header = new LowHeader();
        header.setID((short) 129);
        header.setReliable(true);
        header.setZerocoded(true);
        ObjectData = new ObjectDataBlock[0];
        AgentData = new AgentDataBlock();
    }

    public ObjectMaterialPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        int count = (int) bytes.get() & 0xFF;
        ObjectData = new ObjectDataBlock[count];
        for (int j = 0; j < count; j++) {
            ObjectData[j] = new ObjectDataBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public ObjectMaterialPacket(Header head, ByteBuffer bytes) {
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
        String output = "--- ObjectMaterial ---\n";
        for (int j = 0; j < ObjectData.length; j++) {
            output += ObjectData[j].toString() + "\n";
        }
        output += AgentData.toString() + "\n";
        return output;
    }
}
