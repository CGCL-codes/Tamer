package jazzlib;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream that computes a checksum of the data being read using a
 * supplied Checksum object.
 *
 * @see Checksum
 *
 * @author Tom Tromey
 * @date May 17, 1999
 */
public class CheckedInputStream extends FilterInputStream {

    /**
   * Creates a new CheckInputStream on top of the supplied OutputStream
   * using the supplied Checksum.
   */
    public CheckedInputStream(InputStream in, Checksum sum) {
        super(in);
        this.sum = sum;
    }

    /**
   * Returns the Checksum object used. To get the data checksum computed so
   * far call <code>getChecksum.getValue()</code>.
   */
    public Checksum getChecksum() {
        return sum;
    }

    /**
   * Reads one byte, updates the checksum and returns the read byte
   * (or -1 when the end of file was reached).
   */
    public int read() throws IOException {
        int x = in.read();
        if (x != -1) sum.update(x);
        return x;
    }

    /**
   * Reads at most len bytes in the supplied buffer and updates the checksum
   * with it. Returns the number of bytes actually read or -1 when the end
   * of file was reached.
   */
    public int read(byte[] buf, int off, int len) throws IOException {
        int r = in.read(buf, off, len);
        if (r != -1) sum.update(buf, off, r);
        return r;
    }

    /**
   * Skips n bytes by reading them in a temporary buffer and updating the
   * the checksum with that buffer. Returns the actual number of bytes skiped
   * which can be less then requested when the end of file is reached.
   */
    public long skip(long n) throws IOException {
        if (n == 0) return 0;
        int min = (int) Math.min(n, 1024);
        byte[] buf = new byte[min];
        long s = 0;
        while (n > 0) {
            int r = in.read(buf, 0, min);
            if (r == -1) break;
            n -= r;
            s += r;
            min = (int) Math.min(n, 1024);
            sum.update(buf, 0, r);
        }
        return s;
    }

    /** The checksum object. */
    private Checksum sum;
}
