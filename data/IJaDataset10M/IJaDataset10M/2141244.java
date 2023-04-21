package biz.evot.util.uuid;

import java.io.Serializable;

public class UUID implements Serializable, Cloneable, Comparable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 112102158432121213L;

    private static final String kHexChars = "0123456789abcdefABCDEF";

    public static final byte INDEX_CLOCK_HI = 6;

    public static final byte INDEX_CLOCK_MID = 4;

    public static final byte INDEX_CLOCK_LO = 0;

    public static final byte INDEX_TYPE = 6;

    public static final byte INDEX_CLOCK_SEQUENCE = 8;

    public static final byte INDEX_VARIATION = 8;

    public static final byte TYPE_NULL = 0;

    public static final byte TYPE_TIME_BASED = 1;

    public static final byte TYPE_DCE = 2;

    public static final byte TYPE_NAME_BASED = 3;

    public static final byte TYPE_RANDOM_BASED = 4;

    public static final String NAMESPACE_DNS = "6ba7b810-9dad-11d1-80b4-00c04fd430c8";

    public static final String NAMESPACE_URL = "6ba7b811-9dad-11d1-80b4-00c04fd430c8";

    public static final String NAMESPACE_OID = "6ba7b812-9dad-11d1-80b4-00c04fd430c8";

    public static final String NAMESPACE_X500 = "6ba7b814-9dad-11d1-80b4-00c04fd430c8";

    private static boolean sDescCaching = true;

    /**
	 * The shared null UUID. Would be nice to do lazy instantiation, but if the
	 * instance really has to be a singleton, that would mean class-level
	 * locking (synchronized getNullUUID()), which would be some overhead... So
	 * let's just bite the bullet the first time assuming creation of the null
	 * UUID (plus wasted space if it's not needed) can be ignored.
	 */
    private static final UUID sNullUUID = new UUID();

    private final byte[] mId = new byte[16];

    private transient String mDesc = null;

    private transient int mHashCode = 0;

    /**
	 * Default constructor creates a NIL UUID, one that contains all zeroes
	 * 
	 * Note that the clearing of array is actually unnecessary as JVMs are
	 * required to clear up the allocated arrays by default.
	 */
    public UUID() {
    }

    /**
	 * Constructor for cases where you already have the 16-byte binary
	 * representation of the UUID (for example if you save UUIDs binary takes
	 * less than half of space string representation takes).
	 * 
	 * @param data
	 *            array that contains the binary representation of UUID
	 */
    public UUID(byte[] data) {
        for (int i = 0; i < 16; ++i) {
            mId[i] = data[i];
        }
    }

    /**
	 * Constructor for cases where you already have the binary representation of
	 * the UUID (for example if you save UUIDs binary takes less than half of
	 * space string representation takes) in a byte array
	 * 
	 * @param data
	 *            array that contains the binary representation of UUID
	 * @param start
	 *            byte offset where UUID starts
	 */
    public UUID(byte[] data, int start) {
        for (int i = 0; i < 16; ++i) {
            mId[i] = data[start + i];
        }
    }

    /**
	 * Protected constructor used by UUIDGenerator
	 * 
	 * @param type
	 *            UUID type
	 * @param data
	 *            16 byte UUID contents
	 */
    UUID(int type, byte[] data) {
        for (int i = 0; i < 16; ++i) {
            mId[i] = data[i];
        }
        mId[INDEX_TYPE] &= (byte) 0x0F;
        mId[INDEX_TYPE] |= (byte) (type << 4);
        mId[INDEX_VARIATION] &= (byte) 0x3F;
        mId[INDEX_VARIATION] |= (byte) 0x80;
    }

    /**
	 * Constructor for creating UUIDs from the canonical string representation
	 * 
	 * Note that implementation is optimized for speed, not necessarily code
	 * clarity... Also, since what we get might not be 100% canonical (see
	 * below), let's not yet populate mDesc here.
	 * 
	 * @param id
	 *            String that contains the canonical representation of the UUID
	 *            to build; 36-char string (see UUID specs for details).
	 *            Hex-chars may be in upper-case too; UUID class will always
	 *            output them in lowercase.
	 */
    public UUID(String id) throws NumberFormatException {
        if (id == null) {
            throw new NullPointerException();
        }
        if (id.length() != 36) {
            throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
        }
        for (int i = 0, j = 0; i < 36; ++j) {
            switch(i) {
                case 8:
                case 13:
                case 18:
                case 23:
                    if (id.charAt(i) != '-') {
                        throw new NumberFormatException("UUID has to be represented by the standard 36-char representation");
                    }
                    ++i;
            }
            char c = id.charAt(i);
            if (c >= '0' && c <= '9') {
                mId[j] = (byte) ((c - '0') << 4);
            } else if (c >= 'a' && c <= 'f') {
                mId[j] = (byte) ((c - 'a' + 10) << 4);
            } else if (c >= 'A' && c <= 'F') {
                mId[j] = (byte) ((c - 'A' + 10) << 4);
            } else {
                throw new NumberFormatException("Non-hex character '" + c + "'");
            }
            c = id.charAt(++i);
            if (c >= '0' && c <= '9') {
                mId[j] |= (byte) (c - '0');
            } else if (c >= 'a' && c <= 'f') {
                mId[j] |= (byte) (c - 'a' + 10);
            } else if (c >= 'A' && c <= 'F') {
                mId[j] |= (byte) (c - 'A' + 10);
            } else {
                throw new NumberFormatException("Non-hex character '" + c + "'");
            }
            ++i;
        }
    }

    /**
	 * Default cloning behaviour (bitwise copy) is just fine...
	 * 
	 * Could clear out cached string presentation, but there's probably no point
	 * in doing that.
	 */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static void setDescCaching(boolean state) {
        sDescCaching = state;
    }

    /**
	 * Accessor for getting the shared null UUID
	 * 
	 * @return the shared null UUID
	 */
    public static UUID getNullUUID() {
        return sNullUUID;
    }

    public boolean isNullUUID() {
        if (this == sNullUUID) {
            return true;
        }
        byte[] data = mId;
        int i = mId.length;
        byte zero = (byte) 0;
        while (--i >= 0) {
            if (data[i] != zero) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Returns the UUID type code
	 * 
	 * @return UUID type
	 */
    public int getType() {
        return (mId[INDEX_TYPE] & 0xFF) >> 4;
    }

    /**
	 * Returns the UUID as a 16-byte byte array
	 * 
	 * @return 16-byte byte array that contains UUID bytes in the network byte
	 *         order
	 */
    public byte[] asByteArray() {
        byte[] result = new byte[16];
        toByteArray(result);
        return result;
    }

    /**
	 * Fills in the 16 bytes (from index pos) of the specified byte array with
	 * the UUID contents.
	 * 
	 * @param dst
	 *            Byte array to fill
	 * @param pos
	 *            Offset in the array
	 */
    public void toByteArray(byte[] dst, int pos) {
        byte[] src = mId;
        for (int i = 0; i < 16; ++i) {
            dst[pos + i] = src[i];
        }
    }

    public void toByteArray(byte[] dst) {
        toByteArray(dst, 0);
    }

    /**
	 * 'Synonym' for 'asByteArray'
	 */
    public byte[] toByteArray() {
        return asByteArray();
    }

    /**
	 * Could use just the default hash code, but we can probably create a better
	 * identity hash (ie. same contents generate same hash) manually, without
	 * sacrificing speed too much. Although multiplications with modulos would
	 * generate better hashing, let's use just shifts, and do 2 bytes at a time.
	 * <p>
	 * Of course, assuming UUIDs are randomized enough, even simpler approach
	 * might be good enough?
	 * <p>
	 * Is this a good hash? ... one of these days I better read more about basic
	 * hashing techniques I swear!
	 */
    private static final int[] kShifts = { 3, 7, 17, 21, 29, 4, 9 };

    public int hashCode() {
        if (mHashCode == 0) {
            int result = mId[0] & 0xFF;
            result |= (result << 16);
            result |= (result << 8);
            for (int i = 1; i < 15; i += 2) {
                int curr = (mId[i] & 0xFF) << 8 | (mId[i + 1] & 0xFF);
                int shift = kShifts[i >> 1];
                if (shift > 16) {
                    result ^= (curr << shift) | (curr >>> (32 - shift));
                } else {
                    result ^= (curr << shift);
                }
            }
            int last = mId[15] & 0xFF;
            result ^= (last << 3);
            result ^= (last << 13);
            result ^= (last << 27);
            if (result == 0) {
                mHashCode = -1;
            } else {
                mHashCode = result;
            }
        }
        return mHashCode;
    }

    public String toString() {
        if (mDesc == null) {
            StringBuffer b = new StringBuffer(36);
            for (int i = 0; i < 16; ++i) {
                switch(i) {
                    case 4:
                    case 6:
                    case 8:
                    case 10:
                        b.append('-');
                }
                int hex = mId[i] & 0xFF;
                b.append(kHexChars.charAt(hex >> 4));
                b.append(kHexChars.charAt(hex & 0x0f));
            }
            if (!sDescCaching) {
                return b.toString();
            }
            mDesc = b.toString();
        }
        return mDesc;
    }

    private static final int[] sTimeCompare = new int[] { INDEX_CLOCK_HI, INDEX_CLOCK_HI + 1, INDEX_CLOCK_MID, INDEX_CLOCK_MID + 1, INDEX_CLOCK_LO, INDEX_CLOCK_LO + 1, INDEX_CLOCK_LO + 2, INDEX_CLOCK_LO + 3 };

    /**
	 * Let's also make UUIDs sortable. This will mostly/only be useful with
	 * time-based UUIDs; they will sorted by time of creation. The order will be
	 * strictly correct with UUIDs produced over one JVM's lifetime; that is, if
	 * more than one JVMs create UUIDs and/or system is rebooted the order may
	 * not be 100% accurate between UUIDs created under different JVMs.
	 * 
	 * For all UUIDs, type is first compared, and UUIDs of different types are
	 * sorted together (ie. null UUID is before all other UUIDs, then time-based
	 * UUIDs etc). If types are the same, time-based UUIDs' time stamps
	 * (including additional clock counter) are compared, so UUIDs created first
	 * are ordered first. For all other types (and for time-based UUIDs with
	 * same time stamp, which should only occur when comparing a UUID with
	 * itself, or with UUIDs created on different JVMs or external systems)
	 * binary comparison is done over all 16 bytes.
	 * 
	 * @param o
	 *            Object to compare this UUID to; should be a UUID
	 * 
	 * @return -1 if this UUID should be ordered before the one passed, 1 if
	 *         after, and 0 if they are the same
	 * 
	 * @throws ClassCastException
	 *             if o is not a UUID.
	 */
    public int compareTo(Object o) {
        UUID other = (UUID) o;
        int thisType = getType();
        int thatType = other.getType();
        if (thisType > thatType) {
            return 1;
        } else if (thisType < thatType) {
            return -1;
        }
        byte[] thisId = mId;
        byte[] thatId = other.mId;
        int i = 0;
        if (thisType == TYPE_TIME_BASED) {
            for (; i < 8; ++i) {
                int index = sTimeCompare[i];
                int cmp = (((int) thisId[index]) & 0xFF) - (((int) thatId[index]) & 0xFF);
                if (cmp != 0) {
                    return cmp;
                }
            }
        }
        for (; i < 16; ++i) {
            int cmp = (((int) thisId[i]) & 0xFF) - (((int) thatId[i]) & 0xFF);
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }

    /**
	 * Checking equality of UUIDs is easy; just compare the 128-bit number.
	 */
    public boolean equals(Object o) {
        if (!(o instanceof UUID)) {
            return false;
        }
        byte[] otherId = ((UUID) o).mId;
        byte[] thisId = mId;
        for (int i = 0; i < 16; ++i) {
            if (otherId[i] != thisId[i]) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Constructs a new UUID instance given the canonical string representation
	 * of an UUID.
	 * 
	 * Note that calling this method returns the same result as would using the
	 * matching (1 string arg) constructor.
	 * 
	 * @param id
	 *            Canonical string representation used for constructing an UUID
	 *            instance
	 * 
	 * @throws NumberFormatException
	 *             if 'id' is invalid UUID
	 */
    public static UUID valueOf(String id) throws NumberFormatException {
        return new UUID(id);
    }

    /**
	 * Constructs a new UUID instance given a byte array that contains the (16
	 * byte) binary representation.
	 * 
	 * Note that calling this method returns the same result as would using the
	 * matching constructor
	 * 
	 * @param src
	 *            Byte array that contains the UUID definition
	 * @param start
	 *            Offset in the array where the UUID starts
	 */
    public static UUID valueOf(byte[] src, int start) {
        return new UUID(src, start);
    }

    /**
	 * Constructs a new UUID instance given a byte array that contains the (16
	 * byte) binary representation.
	 * 
	 * Note that calling this method returns the same result as would using the
	 * matching constructor
	 * 
	 * @param src
	 *            Byte array that contains the UUID definition
	 */
    public static UUID valueOf(byte[] src) {
        return new UUID(src);
    }

    public static void main(String[] args) {
    }
}
