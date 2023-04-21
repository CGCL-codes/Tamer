package jlib.misc;

/**
 *
 * @author Pierre-Jean Ditscheid, Consultas SA
 * @version $Id$
 */
public class LittleEndingUnsignBinaryBufferStorage {

    public static final int readByte(byte buf[], int nPos) {
        int n = buf[nPos];
        if (n < 0) n += 256;
        return n;
    }

    public static final int readShort(byte buf[], int nPos) {
        int h = buf[nPos];
        if (h < 0) h += 256;
        int l = buf[nPos + 1];
        if (l < 0) l += 256;
        int n = (h << 8) + l;
        return n;
    }

    public static final void writeShort(byte buf[], short s, int nPos) {
        buf[nPos + 1] = (byte) (s >>> 0 & 0xff);
        buf[nPos] = (byte) (s >>> 8 & 0xff);
    }

    public static final void writeUnsignedShort(byte buf[], int n, int nPos) {
        buf[nPos + 1] = (byte) (n >>> 0 & 0xff);
        buf[nPos] = (byte) (n >>> 8 & 0xff);
    }

    public static final long readInt(byte buf[], int nPos) {
        long h = buf[nPos];
        if (h < 0) h += 256;
        long hm = buf[nPos + 1];
        if (hm < 0) hm += 256;
        long lm = buf[nPos + 2];
        if (lm < 0) lm += 256;
        long l = buf[nPos + 3];
        if (l < 0) l += 256;
        long lValue = (h << 24) + (hm << 16) + (lm << 8) + (l << 0);
        return lValue;
    }

    public static final void writeInt(byte buf[], int i, int j) {
        buf[j + 3] = (byte) (i >>> 0 & 0xff);
        buf[j + 2] = (byte) (i >>> 8 & 0xff);
        buf[j + 1] = (byte) (i >>> 16 & 0xff);
        buf[j] = (byte) (i >>> 24 & 0xff);
    }

    public static final long readLong(byte buf[], int i) {
        long l = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l1 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l2 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l3 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l4 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l5 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l6 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        i++;
        long l7 = buf[i] >= 0 ? buf[i] : buf[i] + 256;
        return (l << 56) + (l1 << 48) + (l2 << 40) + (l3 << 32) + (l4 << 24) + (l5 << 16) + (l6 << 8) + (l7 << 0);
    }

    public static final void writeLong(byte buf[], long l, int i) {
        buf[i + 7] = (byte) (int) (l >>> 0 & 255L);
        buf[i + 6] = (byte) (int) (l >>> 8 & 255L);
        buf[i + 5] = (byte) (int) (l >>> 16 & 255L);
        buf[i + 4] = (byte) (int) (l >>> 24 & 255L);
        buf[i + 3] = (byte) (int) (l >>> 32 & 255L);
        buf[i + 2] = (byte) (int) (l >>> 40 & 255L);
        buf[i + 1] = (byte) (int) (l >>> 48 & 255L);
        buf[i] = (byte) (int) (l >>> 56 & 255L);
    }
}
