package net.oesterholt.jndbm.streams;

import java.io.ByteArrayInputStream;

public class NDbmByteArrayInputStream extends ByteArrayInputStream {

    public void reset(byte[] b) {
        buf = b;
        count = b.length;
        mark = 0;
        pos = 0;
    }

    public NDbmByteArrayInputStream(byte[] b) {
        super(b);
    }

    public NDbmByteArrayInputStream() {
        super(new byte[1]);
    }
}
