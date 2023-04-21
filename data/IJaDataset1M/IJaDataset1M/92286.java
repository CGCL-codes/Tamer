package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GroupAccountDetailsRequestPacket extends Packet {

    public class MoneyDataBlock {

        public LLUUID RequestID = null;

        public int IntervalDays = 0;

        public int CurrentInterval = 0;

        public int getLength() {
            return 24;
        }

        public MoneyDataBlock() {
        }

        public MoneyDataBlock(ByteBuffer bytes) {
            RequestID = new LLUUID(bytes);
            IntervalDays = bytes.getInt();
            CurrentInterval = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            RequestID.GetBytes(bytes);
            bytes.putInt(IntervalDays);
            bytes.putInt(CurrentInterval);
        }

        public String toString() {
            String output = "-- MoneyData --\n";
            try {
                output += "RequestID: " + Helpers.toString(RequestID) + "\n";
                output += "IntervalDays: " + Helpers.toString(IntervalDays) + "\n";
                output += "CurrentInterval: " + Helpers.toString(CurrentInterval) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public MoneyDataBlock createMoneyDataBlock() {
        return new MoneyDataBlock();
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
        return PacketType.GroupAccountDetailsRequest;
    }

    public MoneyDataBlock MoneyData;

    public AgentDataBlock AgentData;

    public GroupAccountDetailsRequestPacket() {
        header = new LowHeader();
        header.setID((short) 430);
        header.setReliable(true);
        header.setZerocoded(true);
        MoneyData = new MoneyDataBlock();
        AgentData = new AgentDataBlock();
    }

    public GroupAccountDetailsRequestPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        MoneyData = new MoneyDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public GroupAccountDetailsRequestPacket(Header head, ByteBuffer bytes) {
        header = head;
        MoneyData = new MoneyDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += MoneyData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        MoneyData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- GroupAccountDetailsRequest ---\n";
        output += MoneyData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
