package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClosestSimulatorPacket extends Packet {

    public class SimulatorBlockBlock {

        public int IP = 0;

        public short Port = 0;

        public long Handle = 0;

        public int getLength() {
            return 14;
        }

        public SimulatorBlockBlock() {
        }

        public SimulatorBlockBlock(ByteBuffer bytes) {
            IP = bytes.getInt();
            Port = (short) ((bytes.get() << 8) + bytes.get());
            Handle = bytes.getLong();
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putInt(IP);
            bytes.put((byte) ((Port >> 8) % 256));
            bytes.put((byte) (Port % 256));
            bytes.putLong(Handle);
        }

        public String toString() {
            String output = "-- SimulatorBlock --\n";
            try {
                output += "IP: " + Helpers.toString(IP) + "\n";
                output += "Port: " + Helpers.toString(Port) + "\n";
                output += "Handle: " + Helpers.toString(Handle) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public SimulatorBlockBlock createSimulatorBlockBlock() {
        return new SimulatorBlockBlock();
    }

    public class ViewerBlock {

        public LLUUID ID = null;

        public int getLength() {
            return 16;
        }

        public ViewerBlock() {
        }

        public ViewerBlock(ByteBuffer bytes) {
            ID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            ID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- Viewer --\n";
            try {
                output += "ID: " + Helpers.toString(ID) + "\n";
                output = output.trim();
            } catch (Exception e) {
            }
            return output;
        }
    }

    public ViewerBlock createViewerBlock() {
        return new ViewerBlock();
    }

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header value) {
        header = value;
    }

    public int getType() {
        return PacketType.ClosestSimulator;
    }

    public SimulatorBlockBlock SimulatorBlock;

    public ViewerBlock Viewer;

    public ClosestSimulatorPacket() {
        header = new LowHeader();
        header.setID((short) 1);
        header.setReliable(true);
        SimulatorBlock = new SimulatorBlockBlock();
        Viewer = new ViewerBlock();
    }

    public ClosestSimulatorPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        SimulatorBlock = new SimulatorBlockBlock(bytes);
        Viewer = new ViewerBlock(bytes);
    }

    public ClosestSimulatorPacket(Header head, ByteBuffer bytes) {
        header = head;
        SimulatorBlock = new SimulatorBlockBlock(bytes);
        Viewer = new ViewerBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += SimulatorBlock.getLength();
        length += Viewer.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        SimulatorBlock.ToBytes(bytes);
        Viewer.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ClosestSimulator ---\n";
        output += SimulatorBlock.toString() + "\n";
        output += Viewer.toString() + "\n";
        return output;
    }
}
