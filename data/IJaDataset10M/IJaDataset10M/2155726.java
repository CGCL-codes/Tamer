package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ChatFromViewerPacket extends Packet {

    public class ChatDataBlock {

        public int Channel = 0;

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

        public byte Type = 0;

        public int getLength() {
            int length = 5;
            if (getMessage() != null) {
                length += 2 + getMessage().length;
            }
            return length;
        }

        public ChatDataBlock() {
        }

        public ChatDataBlock(ByteBuffer bytes) {
            int length;
            Channel = bytes.getInt();
            length = (int) (bytes.get()) & 0xFFFF;
            _message = new byte[length];
            bytes.get(_message);
            Type = bytes.get();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putInt(Channel);
            bytes.putShort((short) _message.length);
            bytes.put(_message);
            bytes.put(Type);
        }

        public String toString() {
            String output = "-- ChatData --\n";
            try {
                output += "Channel: " + Helpers.toString(Channel) + "\n";
                output += Helpers.FieldToString(_message, "Message") + "\n";
                output += "Type: " + Helpers.toString(Type) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ChatDataBlock createChatDataBlock() {
        return new ChatDataBlock();
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
        return PacketType.ChatFromViewer;
    }

    public ChatDataBlock ChatData;

    public AgentDataBlock AgentData;

    public ChatFromViewerPacket() {
        header = new LowHeader();
        header.setID((short) 111);
        header.setReliable(true);
        header.setZerocoded(true);
        ChatData = new ChatDataBlock();
        AgentData = new AgentDataBlock();
    }

    public ChatFromViewerPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        ChatData = new ChatDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ChatFromViewerPacket(Header head, ByteBuffer bytes) {
        header = head;
        ChatData = new ChatDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += ChatData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        ChatData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ChatFromViewer ---\n";
        output += ChatData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
