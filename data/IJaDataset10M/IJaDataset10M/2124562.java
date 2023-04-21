package org.catacombae.dmg.udif;

import org.catacombae.dmgextractor.Util;

public class UDIFBlock implements Comparable<UDIFBlock> {

    /** This blocktype means the data is compressed using some "ADC" algorithm that I have no idea how to decompress... */
    public static final int BT_ADC = 0x80000004;

    /** This blocktype means the data is compressed with zlib. */
    public static final int BT_ZLIB = 0x80000005;

    /** This blocktype means the data is compressed with the bzip2 compression algorithm. These blocktypes are unsupported,
	as I haven't found a GPL-compatible bzip2 decompressor written in Java yet. */
    public static final int BT_BZIP2 = 0x80000006;

    /** This blocktype means the data is uncompressed and can simply be copied. */
    public static final int BT_COPY = 0x00000001;

    /** This blocktype represents a fill of zeroes. */
    public static final int BT_ZERO = 0x00000002;

    /** This blocktype represents a fill of zeroes (the difference between this blocktype and BT_ZERO is not documented, 
	and parsing of these blocks is experimental). */
    public static final int BT_ZERO2 = 0x00000000;

    /** This blocktype indicates the end of the partition. */
    public static final int BT_END = 0xffffffff;

    /** This blocktype has been observed, but its purpose is currently unknown. In all the observed cases the outSize was
	equal to 0, so it's probably some marker, like BT_END. */
    public static final int BT_UNKNOWN = 0x7ffffffe;

    private static final String BT_ADC_STRING = "BT_ADC";

    private static final String BT_ZLIB_STRING = "BT_ZLIB";

    private static final String BT_BZIP2_STRING = "BT_BZIP2";

    private static final String BT_COPY_STRING = "BT_COPY";

    private static final String BT_ZERO_STRING = "BT_ZERO";

    private static final String BT_ZERO2_STRING = "BT_ZERO2";

    private static final String BT_END_STRING = "BT_END";

    private static final String BT_UNKNOWN_STRING = "BT_UNKNOWN";

    private final int blockType;

    private final int reserved;

    private final long outOffset;

    private final long outSize;

    private final long inOffset;

    private final long inSize;

    private final long outOffsetComp;

    private final long inOffsetComp;

    public UDIFBlock(byte[] data, int offset, long outOffsetComp, long inOffsetComp) {
        this(Util.readIntBE(data, offset + 0), Util.readIntBE(data, offset + 4), Util.readLongBE(data, offset + 8) * 0x200, Util.readLongBE(data, offset + 16) * 0x200, Util.readLongBE(data, offset + 24), Util.readLongBE(data, offset + 32), outOffsetComp, inOffsetComp);
    }

    public UDIFBlock(int blockType, int reserved, long outOffset, long outSize, long inOffset, long inSize, long outOffsetComp, long inOffsetComp) {
        this.blockType = blockType;
        this.reserved = reserved;
        this.outOffset = outOffset;
        this.outSize = outSize;
        this.inOffset = inOffset;
        this.inSize = inSize;
        this.outOffsetComp = outOffsetComp;
        this.inOffsetComp = inOffsetComp;
    }

    public static int structSize() {
        return 40;
    }

    public int getBlockType() {
        return blockType;
    }

    public int getReserved() {
        return reserved;
    }

    public long getOutOffset() {
        return outOffset;
    }

    public long getOutSize() {
        return outSize;
    }

    public long getInOffset() {
        return inOffset;
    }

    public long getInSize() {
        return inSize;
    }

    public String getBlockTypeAsString() {
        switch(blockType) {
            case BT_ADC:
                return BT_ADC_STRING;
            case BT_ZLIB:
                return BT_ZLIB_STRING;
            case BT_BZIP2:
                return BT_BZIP2_STRING;
            case BT_COPY:
                return BT_COPY_STRING;
            case BT_ZERO:
                return BT_ZERO_STRING;
            case BT_ZERO2:
                return BT_ZERO2_STRING;
            case BT_END:
                return BT_END_STRING;
            case BT_UNKNOWN:
                return BT_UNKNOWN_STRING;
            default:
                return "[Unknown block type! ID=0x" + Integer.toHexString(blockType) + "]";
        }
    }

    /**
     * This field is not part of the structure itself. It is metadata used to
     * determine the actual byte position of the out offset.
     */
    public long getOutOffsetCompensation() {
        return outOffsetComp;
    }

    /**
     * This field is not part of the structure itself. It is metadata used to
     * determine the actual byte position of the in offset.
     */
    public long getInOffsetCompensation() {
        return inOffsetComp;
    }

    /** Convenience method for determining the actual compensated out offset. This is what you should use. */
    public long getTrueOutOffset() {
        return outOffset + outOffsetComp;
    }

    /** Convenience method for determining the actual compensated in offset. This is what you should use. */
    public long getTrueInOffset() {
        return inOffset + inOffsetComp;
    }

    @Override
    public String toString() {
        return getBlockTypeAsString() + "(reserved=0x" + Integer.toHexString(reserved) + ",outOffset=" + outOffset + ",outSize=" + outSize + ",inOffset=" + inOffset + ",inSize=" + inSize + ",outOffsetComp=" + outOffsetComp + ",inOffsetComp=" + inOffsetComp + ")";
    }

    /**
     * Reads the inOffset field from <code>data</code> at <code>offset</code>
     * which is supposed to be a valid raw UDIF block structure at 40 bytes.
     */
    public static long peekInOffset(byte[] data, int offset) {
        return Util.readLongBE(data, offset + 24);
    }

    /**
     * Reads the inSize field from <code>data</code> at <code>offset</code>
     * which is supposed to be a valid raw UDIF block structure at 40 bytes.
     */
    public static long peekInSize(byte[] data, int offset) {
        return Util.readLongBE(data, offset + 32);
    }

    /** Orders blocks according to the "true" InOffset. */
    public int compareTo(UDIFBlock db) {
        long res = getTrueInOffset() - db.getTrueInOffset();
        if (res > Integer.MAX_VALUE) return Integer.MAX_VALUE; else if (res < Integer.MIN_VALUE) return Integer.MIN_VALUE; else return (int) res;
    }
}
