package net.sourceforge.freejava.sio;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteOutNativeImpl extends AbstractByteOut {

    private final IByteOut base;

    public ByteOutNativeImpl(IByteOut base) {
        if (base == null) throw new NullPointerException("base");
        this.base = base;
    }

    @Override
    public void write(int b) throws IOException {
        base.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) throws IOException {
        base.write(buf, off, len);
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        base.write(buffer);
    }

    @Override
    public void flush(boolean strict) throws IOException {
        base.flush(strict);
    }

    @Override
    public void close() throws IOException {
        base.close();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ByteOutNativeImpl)) return false;
        ByteOutNativeImpl o = (ByteOutNativeImpl) obj;
        if (!base.equals(o.base)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0x2a8fd2d2;
        hash += base.hashCode();
        return hash;
    }
}
