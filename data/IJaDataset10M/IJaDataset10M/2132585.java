package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AgentAlertMessagePacket extends Packet {

    public class AlertDataBlock {

        private byte[] _message;

        public byte[] getMessage() {
            return _message;
        }

        public void setMessage(byte[] value) throws Exception {
            if (value == null) {
                _message = null;
            }
            if (value.length > 255) {
                throw new OverflowException("Value exceeds 255 characters");
            } else {
                _message = new byte[value.length];
                Array.Copy(value, _message, value.length);
            }
        }

        public boolean Modal = false;

        public int getLength() {
            int length = 1;
            if (getMessage() != null) {
                length += 1 + getMessage().length;
            }
            return length;
        }

        public AlertDataBlock() {
        }

        public AlertDataBlock(ByteBuffer bytes) {
            int length;
            length = (int) (bytes.get()) & 0xFF;
            _message = new byte[length];
            bytes.get(_message);
            Modal = (bytes.get() != 0) ? (boolean) true : (boolean) false;
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.put((byte) _message.length);
            bytes.put(_message);
            bytes.put((byte) ((Modal) ? 1 : 0));
        }

        public String toString() {
            String output = "-- AlertData --\n";
            try {
                output += Helpers.FieldToString(_message, "Message") + "\n";
                output += "Modal: " + Helpers.toString(Modal) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public AlertDataBlock createAlertDataBlock() {
        return new AlertDataBlock();
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
        return PacketType.AgentAlertMessage;
    }

    public AlertDataBlock AlertData;

    public AgentDataBlock AgentData;

    public AgentAlertMessagePacket() {
        header = new LowHeader();
        header.setID((short) 176);
        header.setReliable(true);
        AlertData = new AlertDataBlock();
        AgentData = new AgentDataBlock();
    }

    public AgentAlertMessagePacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        AlertData = new AlertDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public AgentAlertMessagePacket(Header head, ByteBuffer bytes) {
        header = head;
        AlertData = new AlertDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += AlertData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        AlertData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- AgentAlertMessage ---\n";
        output += AlertData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
