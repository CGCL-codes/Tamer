package org.doit.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mark Boyns
 */
public class FixedBufferedInputStream extends FilterInputStream {

    private final int BUFSIZE = 8192;

    private int maxBytes = 0;

    private int byteCount = 0;

    private byte bytes[] = null;

    private int blength = 0;

    private int index = 0;

    private boolean eof = false;

    public FixedBufferedInputStream(InputStream in, int maxBytes) {
        super(in);
        this.maxBytes = maxBytes;
    }

    public int read() throws IOException {
        if (eof) {
            return -1;
        }
        if (bytes == null) {
            int n = (maxBytes > 0) ? Math.min(maxBytes - byteCount, BUFSIZE) : BUFSIZE;
            bytes = new byte[n];
            n = read(bytes);
            if (n <= 0) {
                eof = true;
                return -1;
            }
            blength = n;
            byteCount += n;
        }
        int b = bytes[index++];
        if (index == blength) {
            bytes = null;
            index = 0;
            blength = 0;
            if (byteCount == maxBytes) {
                eof = true;
            }
        }
        return b & 0xff;
    }
}
