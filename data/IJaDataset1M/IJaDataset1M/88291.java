package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ParcelAccessListUpdatePacket extends Packet {

    public class DataBlock {

        public int LocalID = 0;

        public int Sections = 0;

        public int SequenceID = 0;

        public int Flags = 0;

        public LLUUID TransactionID = null;

        public int getLength() {
            return 32;
        }

        public DataBlock() {
        }

        public DataBlock(ByteBuffer bytes) {
            LocalID = bytes.getInt();
            Sections = bytes.getInt();
            SequenceID = bytes.getInt();
            Flags = bytes.getInt();
            TransactionID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putInt(LocalID);
            bytes.putInt(Sections);
            bytes.putInt(SequenceID);
            bytes.putInt(Flags);
            TransactionID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- Data --\n";
            try {
                output += "LocalID: " + Helpers.toString(LocalID) + "\n";
                output += "Sections: " + Helpers.toString(Sections) + "\n";
                output += "SequenceID: " + Helpers.toString(SequenceID) + "\n";
                output += "Flags: " + Helpers.toString(Flags) + "\n";
                output += "TransactionID: " + Helpers.toString(TransactionID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public DataBlock createDataBlock() {
        return new DataBlock();
    }

    public class ListBlock {

        public LLUUID ID = null;

        public int Time = 0;

        public int Flags = 0;

        public int getLength() {
            return 24;
        }

        public ListBlock() {
        }

        public ListBlock(ByteBuffer bytes) {
            ID = new LLUUID(bytes);
            Time = bytes.getInt();
            Flags = bytes.getInt();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ID.GetBytes(bytes);
            bytes.putInt(Time);
            bytes.putInt(Flags);
        }

        public String toString() {
            String output = "-- List --\n";
            try {
                output += "ID: " + Helpers.toString(ID) + "\n";
                output += "Time: " + Helpers.toString(Time) + "\n";
                output += "Flags: " + Helpers.toString(Flags) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ListBlock createListBlock() {
        return new ListBlock();
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
        return PacketType.ParcelAccessListUpdate;
    }

    public DataBlock Data;

    public ListBlock[] List;

    public AgentDataBlock AgentData;

    public ParcelAccessListUpdatePacket() {
        header = new LowHeader();
        header.setID((short) 262);
        header.setReliable(true);
        header.setZerocoded(true);
        Data = new DataBlock();
        List = new ListBlock[0];
        AgentData = new AgentDataBlock();
    }

    public ParcelAccessListUpdatePacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        Data = new DataBlock(bytes);
        int count = (int) bytes.get() & 0xFF;
        List = new ListBlock[count];
        for (int j = 0; j < count; j++) {
            List[j] = new ListBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public ParcelAccessListUpdatePacket(Header head, ByteBuffer bytes) {
        header = head;
        Data = new DataBlock(bytes);
        int count = (int) bytes.get() & 0xFF;
        List = new ListBlock[count];
        for (int j = 0; j < count; j++) {
            List[j] = new ListBlock(bytes);
        }
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += Data.getLength();
        length += AgentData.getLength();
        ;
        length++;
        for (int j = 0; j < List.length; j++) {
            length += List[j].getLength();
        }
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        Data.ToBytes(bytes);
        bytes.put((byte) List.length);
        for (int j = 0; j < List.length; j++) {
            List[j].ToBytes(bytes);
        }
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ParcelAccessListUpdate ---\n";
        output += Data.toString() + "\n";
        for (int j = 0; j < List.length; j++) {
            output += List[j].toString() + "\n";
        }
        output += AgentData.toString() + "\n";
        return output;
    }
}
