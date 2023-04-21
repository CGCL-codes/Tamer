package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ScriptRunningReplyPacket extends Packet {

    public class ScriptBlock {

        public LLUUID ObjectID = null;

        public boolean Running = false;

        public LLUUID ItemID = null;

        public int getLength() {
            return 33;
        }

        public ScriptBlock() {
        }

        public ScriptBlock(ByteBuffer bytes) {
            ObjectID = new LLUUID(bytes);
            Running = (bytes.get() != 0) ? (boolean) true : (boolean) false;
            ItemID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ObjectID.GetBytes(bytes);
            bytes.put((byte) ((Running) ? 1 : 0));
            ItemID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- Script --\n";
            try {
                output += "ObjectID: " + Helpers.toString(ObjectID) + "\n";
                output += "Running: " + Helpers.toString(Running) + "\n";
                output += "ItemID: " + Helpers.toString(ItemID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ScriptBlock createScriptBlock() {
        return new ScriptBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.ScriptRunningReply;
    }

    public ScriptBlock Script;

    public ScriptRunningReplyPacket() {
        header = new LowHeader();
        header.setID((short) 290);
        header.setReliable(true);
        Script = new ScriptBlock();
    }

    public ScriptRunningReplyPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        Script = new ScriptBlock(bytes);
    }

    public ScriptRunningReplyPacket(Header head, ByteBuffer bytes) {
        header = head;
        Script = new ScriptBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += Script.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        Script.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ScriptRunningReply ---\n";
        output += Script.toString() + "\n";
        return output;
    }
}
