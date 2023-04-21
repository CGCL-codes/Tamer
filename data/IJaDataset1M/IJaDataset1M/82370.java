package libsecondlife.packets;

import libsecondlife.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ObjectAddPacket extends Packet {

    public class ObjectDataBlock {

        public int AddFlags = 0;

        public byte PathTwistBegin = 0;

        public byte PathEnd = 0;

        public byte ProfileBegin = 0;

        public byte PathRadiusOffset = 0;

        public byte PathSkew = 0;

        public LLVector3 RayStart = null;

        public byte ProfileCurve = 0;

        public byte PathScaleX = 0;

        public byte PathScaleY = 0;

        public byte Material = 0;

        public byte PathShearX = 0;

        public byte PathShearY = 0;

        public byte PathTaperX = 0;

        public byte PathTaperY = 0;

        public byte RayEndIsIntersection = 0;

        public LLVector3 RayEnd = null;

        public byte ProfileEnd = 0;

        public byte PathBegin = 0;

        public byte BypassRaycast = 0;

        public byte PCode = 0;

        public byte PathCurve = 0;

        public LLVector3 Scale = null;

        public byte State = 0;

        public byte PathTwist = 0;

        private byte[] _textureentry;

        public byte[] getTextureEntry() {
            return _textureentry;
        }

        public void setTextureEntry(byte[] value) throws Exception {
            if (value == null) {
                _textureentry = null;
            }
            if (value.length > 1024) {
                throw new OverflowException("Value exceeds 1024 characters");
            } else {
                _textureentry = new byte[value.length];
                Array.Copy(value, _textureentry, value.length);
            }
        }

        public byte ProfileHollow = 0;

        public byte PathRevolutions = 0;

        public LLQuaternion Rotation = null;

        public LLUUID RayTargetID = null;

        public int getLength() {
            int length = 91;
            if (getTextureEntry() != null) {
                length += 2 + getTextureEntry().length;
            }
            return length;
        }

        public ObjectDataBlock() {
        }

        public ObjectDataBlock(ByteBuffer bytes) {
            int length;
            AddFlags = bytes.getInt();
            PathTwistBegin = bytes.get();
            PathEnd = bytes.get();
            ProfileBegin = bytes.get();
            PathRadiusOffset = bytes.get();
            PathSkew = bytes.get();
            RayStart = new LLVector3(bytes);
            ProfileCurve = bytes.get();
            PathScaleX = bytes.get();
            PathScaleY = bytes.get();
            Material = bytes.get();
            PathShearX = bytes.get();
            PathShearY = bytes.get();
            PathTaperX = bytes.get();
            PathTaperY = bytes.get();
            RayEndIsIntersection = bytes.get();
            RayEnd = new LLVector3(bytes);
            ProfileEnd = bytes.get();
            PathBegin = bytes.get();
            BypassRaycast = bytes.get();
            PCode = bytes.get();
            PathCurve = bytes.get();
            Scale = new LLVector3(bytes);
            State = bytes.get();
            PathTwist = bytes.get();
            length = (int) (bytes.get()) & 0xFFFF;
            _textureentry = new byte[length];
            bytes.get(_textureentry);
            ProfileHollow = bytes.get();
            PathRevolutions = bytes.get();
            Rotation = new LLQuaternion(bytes, true);
            RayTargetID = new LLUUID(bytes);
        }

        public void ToBytes(ByteBuffer bytes) throws Exception {
            bytes.putInt(AddFlags);
            bytes.put(PathTwistBegin);
            bytes.put(PathEnd);
            bytes.put(ProfileBegin);
            bytes.put(PathRadiusOffset);
            bytes.put(PathSkew);
            RayStart.GetBytes(bytes);
            bytes.put(ProfileCurve);
            bytes.put(PathScaleX);
            bytes.put(PathScaleY);
            bytes.put(Material);
            bytes.put(PathShearX);
            bytes.put(PathShearY);
            bytes.put(PathTaperX);
            bytes.put(PathTaperY);
            bytes.put(RayEndIsIntersection);
            RayEnd.GetBytes(bytes);
            bytes.put(ProfileEnd);
            bytes.put(PathBegin);
            bytes.put(BypassRaycast);
            bytes.put(PCode);
            bytes.put(PathCurve);
            Scale.GetBytes(bytes);
            bytes.put(State);
            bytes.put(PathTwist);
            bytes.putShort((short) _textureentry.length);
            bytes.put(_textureentry);
            bytes.put(ProfileHollow);
            bytes.put(PathRevolutions);
            Rotation.GetBytes(bytes);
            RayTargetID.GetBytes(bytes);
        }

        public String toString() {
            String output = "-- ObjectData --\n";
            try {
                output += "AddFlags: " + Helpers.toString(AddFlags) + "\n";
                output += "PathTwistBegin: " + Helpers.toString(PathTwistBegin) + "\n";
                output += "PathEnd: " + Helpers.toString(PathEnd) + "\n";
                output += "ProfileBegin: " + Helpers.toString(ProfileBegin) + "\n";
                output += "PathRadiusOffset: " + Helpers.toString(PathRadiusOffset) + "\n";
                output += "PathSkew: " + Helpers.toString(PathSkew) + "\n";
                output += "RayStart: " + Helpers.toString(RayStart) + "\n";
                output += "ProfileCurve: " + Helpers.toString(ProfileCurve) + "\n";
                output += "PathScaleX: " + Helpers.toString(PathScaleX) + "\n";
                output += "PathScaleY: " + Helpers.toString(PathScaleY) + "\n";
                output += "Material: " + Helpers.toString(Material) + "\n";
                output += "PathShearX: " + Helpers.toString(PathShearX) + "\n";
                output += "PathShearY: " + Helpers.toString(PathShearY) + "\n";
                output += "PathTaperX: " + Helpers.toString(PathTaperX) + "\n";
                output += "PathTaperY: " + Helpers.toString(PathTaperY) + "\n";
                output += "RayEndIsIntersection: " + Helpers.toString(RayEndIsIntersection) + "\n";
                output += "RayEnd: " + Helpers.toString(RayEnd) + "\n";
                output += "ProfileEnd: " + Helpers.toString(ProfileEnd) + "\n";
                output += "PathBegin: " + Helpers.toString(PathBegin) + "\n";
                output += "BypassRaycast: " + Helpers.toString(BypassRaycast) + "\n";
                output += "PCode: " + Helpers.toString(PCode) + "\n";
                output += "PathCurve: " + Helpers.toString(PathCurve) + "\n";
                output += "Scale: " + Helpers.toString(Scale) + "\n";
                output += "State: " + Helpers.toString(State) + "\n";
                output += "PathTwist: " + Helpers.toString(PathTwist) + "\n";
                output += Helpers.FieldToString(_textureentry, "TextureEntry") + "\n";
                output += "ProfileHollow: " + Helpers.toString(ProfileHollow) + "\n";
                output += "PathRevolutions: " + Helpers.toString(PathRevolutions) + "\n";
                output += "Rotation: " + Helpers.toString(Rotation) + "\n";
                output += "RayTargetID: " + Helpers.toString(RayTargetID) + "\n";
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
        return PacketType.ObjectAdd;
    }

    public ObjectDataBlock ObjectData;

    public AgentDataBlock AgentData;

    public ObjectAddPacket() {
        header = new LowHeader();
        header.setID((short) 2);
        header.setReliable(true);
        header.setZerocoded(true);
        ObjectData = new ObjectDataBlock();
        AgentData = new AgentDataBlock();
    }

    public ObjectAddPacket(ByteBuffer bytes) throws Exception {
        int[] a_packetEnd = new int[] { bytes.position() - 1 };
        header = new LowHeader(bytes, a_packetEnd);
        ObjectData = new ObjectDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ObjectAddPacket(Header head, ByteBuffer bytes) {
        header = head;
        ObjectData = new ObjectDataBlock(bytes);
        AgentData = new AgentDataBlock(bytes);
    }

    public ByteBuffer ToBytes() throws Exception {
        int length = 8;
        length += ObjectData.getLength();
        length += AgentData.getLength();
        ;
        if (header.AckList.length > 0) {
            length += header.AckList.length * 4 + 1;
        }
        ByteBuffer bytes = ByteBuffer.allocate(length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        header.ToBytes(bytes);
        ObjectData.ToBytes(bytes);
        AgentData.ToBytes(bytes);
        if (header.AckList.length > 0) {
            header.AcksToBytes(bytes);
        }
        return bytes;
    }

    public String toString() {
        String output = "--- ObjectAdd ---\n";
        output += ObjectData.toString() + "\n";
        output += AgentData.toString() + "\n";
        return output;
    }
}
