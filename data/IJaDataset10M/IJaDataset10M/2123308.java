package org.localstorm.crypto.rc4;

import org.localstorm.crypto.GammaGenerator;
import java.io.IOException;
import java.io.OutputStream;

/**
 * RC4 stream cipher implementation.
 * @author LocalStorm
 * @version 1.0
 */
public class RC4OutputStream extends OutputStream {

    private final OutputStream cout;

    private final GammaGenerator gg;

    public RC4OutputStream(OutputStream os, String key) {
        cout = os;
        gg = RC4GammaGenerator.getGammaGenerator(key);
    }

    public final void write(int i) throws IOException {
        cout.write(i ^ gg.next());
    }

    public final void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        len = len + off;
        for (int i = off; i < len; i++) {
            cout.write(b[i] ^ gg.next());
        }
    }

    public final void write(byte[] b) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else {
            for (int i = 0; i < b.length; i++) {
                cout.write(b[i] ^ gg.next());
            }
        }
    }

    public void close() throws IOException {
        gg.close();
        cout.close();
    }
}
