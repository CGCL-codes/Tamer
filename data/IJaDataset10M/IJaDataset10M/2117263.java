package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class StartAuctionPacket extends Packet {

    public class ParcelDataBlock {

        public LLUUID ParcelID = null;

        private byte[] _name;

        public byte[] getName() {
            return _name;
        }

        public void setName(byte[] value) throws Exception {
            if (value == null) {
                _name = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _name = new byte[value.length];
                Array.Copy(value, _name, value.length);
            }
        }

        public LLUUID SnapshotID = null;

        public int getLength() {
            int length = 32;
            if (getName() != null) {
                length += 1 + getName().length;
            }
            return length;
        }

        public ParcelDataBlock() {
        }

        public ParcelDataBlock(ByteBuffer bytes) {
            int length;
            ParcelID = new LLUUID(bytes);
            length = (int) (bytes.get()) & 0xFF;
            _name = new byte[length];
            bytes.get(_name);
            SnapshotID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ParcelID.GetBytes(bytes);
            bytes.put((byte) _name.length);
            bytes.put(_name);
            SnapshotID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- ParcelData --\n";
            try {
                output += "ParcelID: " + Helpers.toString(ParcelID) + "\n";
                output += Helpers.FieldToString(_name, "Name") + "\n";
                output += "SnapshotID: " + Helpers.toString(SnapshotID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ParcelDataBlock createParcelDataBlock() {
        return new ParcelDataBlock();
    }

    public class AgentDataBlock {

        public LLUUID AgentID = null;

        public int getLength() {
            return 16;
        }

        public AgentDataBlock() {
        }

        public AgentDataBlock(ByteBuffer bytes) {
            AgentID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            AgentID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- AgentData --\n";
            try {
                output += "AgentID: " + Helpers.toString(AgentID) + "\n";
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
        return PacketType.StartAuction;
    }

    public ParcelDataBlock ParcelData;

    public AgentDataBlock AgentData;

    public StartAuctionPacket() {
        header = new LowHeader();
        header.setID((short) 275);
        header.setReliable(true);
        ParcelData = new ParcelDataBlock();
        AgentData = new AgentDataBlock();
    }

    public StartAuctionPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        ParcelData = new ParcelDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public StartAuctionPacket(Header head, ByteBuffer bytes) {
        header = head;
        ParcelData = new ParcelDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += ParcelData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        ParcelData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- StartAuction ---\n";
        output += ParcelData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
