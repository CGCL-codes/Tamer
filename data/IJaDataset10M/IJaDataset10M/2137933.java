package gov.lanl.util.uuid;

/**
 * UUID.java<br>
 * This class implements UUID version 4 The values for the various fields are
 * crypto random values set by the factory class UUIDFactory
 */
public final class UUID implements java.io.Serializable {

    private long hi;

    private long lo;

    /**
     * Construct a Version 4 UUID object form another UUID object
     */
    public UUID(UUID uuid) {
        this.hi = uuid.hi;
        this.lo = uuid.lo;
    }

    /**
     * Construct a Version 4 UUID object form the two given long values. These
     * values are (pseudo)random numbers (best if crypto quality)
     */
    public UUID(long _hi, long _lo) {
        this.hi = _hi;
        this.lo = _lo;
        lo &= 0x3FFFFFFFFFFFFFFFL;
        lo |= 0x8000000000000000L;
        lo |= 0x0000800000000000L;
        hi &= 0xFFFFFFFFFFFF0FFFL;
        hi |= 0x0000000000004000L;
    }

    /**
     * Returns true if equal
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof UUID) return equals((UUID) obj);
        return false;
    }

    /**
     * Returns true if equal
     */
    public boolean equals(UUID uuid) {
        return (hi == uuid.hi && lo == uuid.lo);
    }

    public int hashCode() {
        return new Long(hi ^ lo).hashCode();
    }

    /**
     * Returns the string representation of this UUID
     */
    public String toString() {
        return ("urn:uuid:" + getNudeId());
    }

    public String getNudeId() {
        return (hexDigits(hi >> 32, 4) + "-" + hexDigits(hi >> 16, 2) + "-" + hexDigits(hi, 2) + "-" + hexDigits(lo >> 48, 2) + "-" + hexDigits(lo, 6));
    }

    /**
     * Returns the Hex value of the nHexOctets lest significant octets from the
     * long value lVal as a String
     */
    private static String hexDigits(long lVal, int nHexOctets) {
        long tmp = 1L << (nHexOctets * 2 * 4);
        long result = lVal & (tmp - 1);
        result = tmp | result;
        return Long.toHexString(result).substring(1);
    }
}
