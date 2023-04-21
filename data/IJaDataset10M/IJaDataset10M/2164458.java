package eu.pisolutions.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link eu.pisolutions.io.InputStreamWrapper} that detects whether the content is ASCII-compatible.
 *
 * @author Laurent Pireyn
 */
public final class AsciiDetectingInputStream extends InputStreamWrapper {

    private static boolean isAscii(byte b) {
        return (b & 0x80) == 0;
    }

    private boolean ascii = true;

    public AsciiDetectingInputStream(InputStream in) {
        super(in);
    }

    public boolean isAscii() {
        return this.ascii;
    }

    @Override
    public int read() throws IOException {
        final int b = this.in.read();
        if (this.ascii && b != -1 && !AsciiDetectingInputStream.isAscii((byte) b)) {
            this.ascii = false;
        }
        return b;
    }

    @Override
    public int read(byte[] array, int offset, int length) throws IOException {
        final int count = super.read(array, offset, length);
        for (int i = 0; this.ascii && i < count; ++i) {
            if (!AsciiDetectingInputStream.isAscii(array[offset + i])) {
                this.ascii = false;
            }
        }
        return count;
    }
}
