package net.sourceforge.freejava.mem.types;

import net.sourceforge.freejava.mem.AccessException;
import net.sourceforge.freejava.mem.Memory;
import net.sourceforge.freejava.mem._Type;
import net.sourceforge.freejava.primitive.IntMath;

public class Int16LE extends _Type {

    @Override
    public Short get(Memory memory, int offset) throws AccessException {
        byte[] mem = new byte[2];
        memory.read(offset, mem);
        return read(mem);
    }

    @Override
    public void put(Memory memory, int offset, Object value) throws AccessException {
        byte[] mem = new byte[2];
        write(mem, (Short) value);
        memory.write(offset, mem);
    }

    public static short read(byte[] b) {
        int value = IntMath.unsign(b[1]);
        value <<= 8;
        value |= IntMath.unsign(b[0]);
        return (short) value;
    }

    public static void write(byte[] b, short value) {
        b[0] = (byte) value;
        value >>= 8;
        b[1] = (byte) value;
    }
}
