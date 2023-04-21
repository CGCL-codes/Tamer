package com.sun.ebxml.registry.util;

import java.io.Serializable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A universally unique identifier (UUID).
 * A UUID is a 128-bit value. <p>
 *
 * The most significant long can be decomposed into the following
 * unsigned fields:
 * <pre>
 * 0xFFFFFFFF00000000 time_low
 * 0x00000000FFFF0000 time_mid
 * 0x000000000000F000 version
 * 0x0000000000000FFF time_hi
 * </pre>
 * The least significant long can be decomposed into the following
 * unsigned fields:
 * <pre>
 * 0xC000000000000000 variant
 * 0x3FFF000000000000 clock_seq
 * 0x0000FFFFFFFFFFFF node
 * </pre>
 * The variant field must be 0x2. The version field must be either 0x1 or 0x4.
 * If the version field is 0x4, then the most significant bit of the node
 * field must be set to 1, and the remaining fields are set to values
 * produced by a cryptographically strong pseudo-random number generator.
 * If the version field is 0x1, then the node field is set to an IEEE 802
 * address, the clock_seq field is set to a 14-bit random number, and the
 * time_low, time_mid, and time_hi fields are set to the least, middle and
 * most significant bits (respectively) of a 60-bit timestamp measured in
 * 100-nanosecond units since midnight, October 15, 1582 UTC.
 */
public final class UUID implements Serializable {

    /**
     * The most significant 64 bits.
     *
     * @serial
     */
    private long mostSig;

    /**
     * The least significant 64 bits.
     *
     * @serial
     */
    private long leastSig;

    /**
     * Simple constructor.
     *
     * @param mostSig the most significant 64 bits
     * @param leastSig the lease significant 64 bits
     */
    public UUID(long mostSig, long leastSig) {
        this.mostSig = mostSig;
        this.leastSig = leastSig;
    }

    /**
     * Reads in 16 bytes in standard network byte order.
     *
     * @param in the input stream to read 16 bytes from
     */
    public UUID(DataInput in) throws IOException {
        this.mostSig = in.readLong();
        this.leastSig = in.readLong();
    }

    /** Returns the most significant 64 bits of the service ID. */
    public long getMostSignificantBits() {
        return mostSig;
    }

    /** Returns the least significant 64 bits of the service ID. */
    public long getLeastSignificantBits() {
        return leastSig;
    }

    /**
     * Writes out 16 bytes in standard network byte order.
     *
     * @param out the output stream to write 16 bytes to
     */
    public void writeBytes(DataOutput out) throws IOException {
        out.writeLong(mostSig);
        out.writeLong(leastSig);
    }

    public int hashCode() {
        return (int) ((mostSig >> 32) ^ mostSig ^ (leastSig >> 32) ^ leastSig);
    }

    /**
     * UU IDs are equal if they represent the same 128-bit value.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof UUID)) return false;
        UUID sid = (UUID) obj;
        return (mostSig == sid.mostSig && leastSig == sid.leastSig);
    }

    /**
     * Returns a 36-character string of six fields separated by hyphens,
     * with each field represented in lowercase hexadecimal with the same
     * number of digits as in the field. The order of fields is: time_low,
     * time_mid, version and time_hi treated as a single field, variant and
     * clock_seq treated as a single field, and node.
     */
    public String toString() {
        return (digits(mostSig >> 32, 8) + "-" + digits(mostSig >> 16, 4) + "-" + digits(mostSig, 4) + "-" + digits(leastSig >> 48, 4) + "-" + digits(leastSig, 12));
    }

    /** Returns val represented by the specified number of hex digits. */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
