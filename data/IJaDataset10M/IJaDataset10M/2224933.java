package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImprovedInstantMessagePacket extends Packet {

    public class MessageBlockBlock {

        public LLUUID ID = null;

        public LLUUID ToAgentID = null;

        public byte Offline = 0;

        public int Timestamp = 0;

        private byte[] _message;

        public byte[] getMessage() {
            return _message;
        }

        public void setMessage(byte[] value) throws Exception {
            if (value == null) {
                _message = null;
            }
            if (value.length > 1024) {
                throw new OverflowException("Value exceeds 1024 characters");
            } else {
                _message = new byte[value.length];
                Array.Copy(value, _message, value.length);
            }
        }

        public LLUUID RegionID = null;

        public byte Dialog = 0;

        public boolean FromGroup = false;

        private byte[] _binarybucket;

        public byte[] getBinaryBucket() {
            return _binarybucket;
        }

        public void setBinaryBucket(byte[] value) throws Exception {
            if (value == null) {
                _binarybucket = null;
            }
            if (value.length > 1024) {
                throw new OverflowException("Value exceeds 1024 characters");
            } else {
                _binarybucket = new byte[value.length];
                Array.Copy(value, _binarybucket, value.length);
            }
        }

        public int ParentEstateID = 0;

        private byte[] _fromagentname;

        public byte[] getFromAgentName() {
            return _fromagentname;
        }

        public void setFromAgentName(byte[] value) throws Exception {
            if (value == null) {
                _fromagentname = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _fromagentname = new byte[value.length];
                Array.Copy(value, _fromagentname, value.length);
            }
        }

        public LLVector3 Position = null;

        public int getLength() {
            int length = 71;
            if (getMessage() != null) {
                length += 2 + getMessage().length;
            }
            if (getBinaryBucket() != null) {
                length += 2 + getBinaryBucket().length;
            }
            if (getFromAgentName() != null) {
                length += 1 + getFromAgentName().length;
            }
            return length;
        }

        public MessageBlockBlock() {
        }

        public MessageBlockBlock(ByteBuffer bytes) {
            int length;
            ID = new LLUUID(bytes);
            ToAgentID = new LLUUID(bytes);
            Offline = bytes.get();
            Timestamp = bytes.getInt();
            length = (int) (bytes.get()) & 0xFFFF;
            _message = new byte[length];
            bytes.get(_message);
            RegionID = new LLUUID(bytes);
            Dialog = bytes.get();
            FromGroup = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            length = (int) (bytes.get()) & 0xFFFF;
            _binarybucket = new byte[length];
            bytes.get(_binarybucket);
            ParentEstateID = bytes.getInt();
            length = (int) (bytes.get()) & 0xFF;
            _fromagentname = new byte[length];
            bytes.get(_fromagentname);
            Position = new LLVector3(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ID.GetBytes(bytes);
            ToAgentID.GetBytes(bytes);
            bytes.put(Offline);
            bytes.putInt(Timestamp);
            bytes.putShort((short) _message.length);
            bytes.put(_message);
            RegionID.GetBytes(bytes);
            bytes.put(Dialog);
            bytes.put((byte) ((FromGroup) ? 1 : 0));
            bytes.putShort((short) _binarybucket.length);
            bytes.put(_binarybucket);
            bytes.putInt(ParentEstateID);
            bytes.put((byte) _fromagentname.length);
            bytes.put(_fromagentname);
            Position.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- MessageBlock --\n";
            try {
                output += "ID: " + Helpers.toString(ID) + "\n";
                output += "ToAgentID: " + Helpers.toString(ToAgentID) + "\n";
                output += "Offline: " + Helpers.toString(Offline) + "\n";
                output += "Timestamp: " + Helpers.toString(Timestamp) + "\n";
                output += Helpers.FieldToString(_message, "Message") + "\n";
                output += "RegionID: " + Helpers.toString(RegionID) + "\n";
                output += "Dialog: " + Helpers.toString(Dialog) + "\n";
                output += "FromGroup: " + Helpers.toString(FromGroup) + "\n";
                output += Helpers.FieldToString(_binarybucket, "BinaryBucket") + "\n";
                output += "ParentEstateID: " + Helpers.toString(ParentEstateID) + "\n";
                output += Helpers.FieldToString(_fromagentname, "FromAgentName") + "\n";
                output += "Position: " + Helpers.toString(Position) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public MessageBlockBlock createMessageBlockBlock() {
        return new MessageBlockBlock();
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
        return PacketType.ImprovedInstantMessage;
    }

    public MessageBlockBlock MessageBlock;

    public AgentDataBlock AgentData;

    public ImprovedInstantMessagePacket() {
        header = new LowHeader();
        header.setID((short) 305);
        header.setReliable(true);
        header.setZerocoded(true);
        MessageBlock = new MessageBlockBlock();
        AgentData = new AgentDataBlock();
    }

    public ImprovedInstantMessagePacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        MessageBlock = new MessageBlockBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ImprovedInstantMessagePacket(Header head, ByteBuffer bytes) {
        header = head;
        MessageBlock = new MessageBlockBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += MessageBlock.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        MessageBlock.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ImprovedInstantMessage ---\n";
        output += MessageBlock.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
