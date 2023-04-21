package nl.huub.van.amelsvoort.qcommon;

import nl.huub.van.amelsvoort.util.Lib;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class texinfo_t {

    public texinfo_t(byte[] cmod_base, int o, int len) {
        this(ByteBuffer.wrap(cmod_base, o, len).order(ByteOrder.LITTLE_ENDIAN));
    }

    public texinfo_t(ByteBuffer bb) {
        byte str[] = new byte[32];
        vecs[0] = new float[] { bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat() };
        vecs[1] = new float[] { bb.getFloat(), bb.getFloat(), bb.getFloat(), bb.getFloat() };
        flags = bb.getInt();
        value = bb.getInt();
        bb.get(str);
        texture = new String(str, 0, Lib.strlen(str));
        nexttexinfo = bb.getInt();
    }

    public static final int SIZE = 32 + 4 + 4 + 32 + 4;

    public float vecs[][] = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };

    public int flags;

    public int value;

    public String texture = "";

    public int nexttexinfo;
}
