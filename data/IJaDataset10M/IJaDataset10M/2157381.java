package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MapItemRequestPacket extends Packet {

    public class RequestDataBlock {

        public long RegionHandle = 0;

        public int ItemType = 0;

        public int getLength() {
            return 12;
        }

        public RequestDataBlock() {
        }

        public RequestDataBlock(ByteBuffer bytes) {
            RegionHandle = bytes.getLong();
            ItemType = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putLong(RegionHandle);
            bytes.putInt(ItemType);
        }

        public String toString() {
            String output = "-- RequestData --\n";
            try {
                output += "RegionHandle: " + Helpers.toString(RegionHandle) + "\n";
                output += "ItemType: " + Helpers.toString(ItemType) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public RequestDataBlock createRequestDataBlock() {
        return new RequestDataBlock();
    }

    public class AgentDataBlock {

        public LLUUID AgentID = null;

        public LLUUID SessionID = null;

        public boolean Godlike = false;

        public int Flags = 0;

        public int EstateID = 0;

        public int getLength() {
            return 41;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
            SessionID = new LLUUID(bytes);
            Godlike = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            Flags = bytes.getInt();
            EstateID = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
            SessionID.GetBytes(bytes);
            bytes.put((byte) ((Godlike) ? 1 : 0));
            bytes.putInt(Flags);
            bytes.putInt(EstateID);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
                output += "SessionID: " + Helpers.toString(SessionID) + "\n";
                output += "Godlike: " + Helpers.toString(Godlike) + "\n";
                output += "Flags: " + Helpers.toString(Flags) + "\n";
                output += "EstateID: " + Helpers.toString(EstateID) + "\n";
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
        return PacketType.MapItemRequest;
    }

    public RequestDataBlock RequestData;

    public AgentDataBlock AgentData;

    public MapItemRequestPacket() {
        header = new LowHeader();
        header.setID((short) 494);
        header.setReliable(true);
        RequestData = new RequestDataBlock();
        AgentData = new AgentDataBlock();
    }

    public MapItemRequestPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        RequestData = new RequestDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public MapItemRequestPacket(Header head, ByteBuffer bytes) {
        header = head;
        RequestData = new RequestDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += RequestData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        RequestData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- MapItemRequest ---\n";
        output += RequestData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
