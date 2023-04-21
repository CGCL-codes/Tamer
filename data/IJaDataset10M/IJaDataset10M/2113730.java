package net.sourceforge.freejava.mem.types;

import net.sourceforge.freejava.mem.AccessException;
import net.sourceforge.freejava.mem.Memory;
import net.sourceforge.freejava.mem._Type;
import net.sourceforge.freejava.meta.codereview.ThreadUnsafe;
import net.sourceforge.freejava.primitive.IntMath;

public class Int64LE extends _Type {

    @ThreadUnsafe
    @Override
    public Long get(Memory memory, int offset) throws AccessException {
        byte[] mem = new byte[8];
        memory.read(offset, mem);
        return read(mem);
    }

    @ThreadUnsafe
    @Override
    public void put(Memory memory, int offset, Object value) throws AccessException {
        byte[] mem = new byte[8];
        write(mem, (Long) value);
        memory.write(offset, mem);
    }

    public static long read(byte[] b) {
        int vh = IntMath.unsign(b[7]);
        vh <<= 8;
        vh |= IntMath.unsign(b[6]);
        vh <<= 8;
        vh |= IntMath.unsign(b[5]);
        vh <<= 8;
        vh |= IntMath.unsign(b[4]);
        int vl = IntMath.unsign(b[3]);
        vl <<= 8;
        vl |= IntMath.unsign(b[2]);
        vl <<= 8;
        vl |= IntMath.unsign(b[1]);
        vl <<= 8;
        vl |= IntMath.unsign(b[0]);
        long value = IntMath.unsign(vh);
        value <<= 32;
        value |= IntMath.unsign(vl);
        return value;
    }

    public static void write(byte[] b, long value) {
        int vl = (int) value;
        b[0] = (byte) vl;
        vl >>= 8;
        b[1] = (byte) vl;
        vl >>= 8;
        b[2] = (byte) vl;
        vl >>= 8;
        b[3] = (byte) vl;
        value >>= 32;
        int vh = (int) value;
        b[4] = (byte) vh;
        vh >>= 8;
        b[5] = (byte) vh;
        vh >>= 8;
        b[6] = (byte) vh;
        vh >>= 8;
        b[7] = (byte) vh;
    }
}
