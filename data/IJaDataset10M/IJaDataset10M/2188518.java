package shared.network;

import java.nio.ByteBuffer;
import java.util.HashMap;

public final class ObjectRemovalPacket {

    private static byte enumEnum = 0;

    public static enum ObjectRemovalEffects {

        PROJECTILE_EXPLODE, SHURIKEN_EXPLODE;

        private static HashMap<Byte, ObjectRemovalEffects> valueMap = new HashMap<Byte, ObjectRemovalEffects>();

        static {
            for (ObjectRemovalEffects se : ObjectRemovalEffects.values()) valueMap.put(se.val, se);
        }

        public final byte val;

        private ObjectRemovalEffects() {
            val = enumEnum++;
        }

        public static ObjectRemovalEffects getRemovalEffectByValue(byte b) {
            return valueMap.get(b);
        }
    }

    public static final byte type = NetworkProtocol.PacketType.OBJECT_REMOVAL.val;

    /**
	 * 
	 * @param oid
	 * 		The ID number of the object that is being removed
	 * @return
	 * 		Returns a ByteBuffer of the format
	 * 		| Header | OID
	 * 		where header is the type of packet and OID id the
	 * 		object's ID number
	 */
    public static ByteBuffer create(int oid) {
        return NetworkProtocol.createSingleArgumentPacket(type, oid);
    }

    /**
	 * 
	 * @param oid
	 * 		The ID number of the object that is being removed
	 * @param additionalBytes
	 * 		The number of additional bytes to be added to the
	 * 		size of the ByteBuffer
	 * @return
	 * 		Returns a ByteBuffer of the format
	 * 		| Header | OID | ...
	 * 		where header is the type of packet and OID id the
	 * 		object's ID number. The ... represents the additional
	 * 		space created in the ByteBuffer from the additionalBytes
	 * 		parameter
	 */
    public static ByteBuffer partialCreate(int oid, int additionalBytes) {
        ByteBuffer ret = ByteBuffer.allocate(5 + additionalBytes);
        ret.put(type);
        ret.putInt(oid);
        return ret;
    }

    /**
	 * 
	 * @param packet
	 * 		A ByteBuffer of the format
	 * 		| Header | OID
	 * 		where header is the type of packet and OID id the
	 * 		object's ID number
	 * @return
	 * 		Returns an int which is the object's ID number
	 */
    public static int decode(ByteBuffer packet) {
        packet.rewind();
        if (packet.get() != type) throw new IllegalArgumentException();
        return packet.getInt();
    }

    private ObjectRemovalPacket() {
        throw new AssertionError();
    }
}
