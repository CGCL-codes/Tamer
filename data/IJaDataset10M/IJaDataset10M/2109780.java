package net.sourceforge.jtds.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that will continue to read until the expected number of
 * bytes has been read.
 *
 * @author Rob Worsnop
 * @version $Id: KnownLengthInputStream.java,v 1.1 2005-01-04 17:13:04 alin_sinpalean Exp $
 */
public class KnownLengthInputStream extends FilterInputStream {

    /**
     * Constructs an object around an existing stream.
     *
     * @param in the existing stream
     */
    public KnownLengthInputStream(InputStream in) {
        super(in);
    }

    /**
     * Reads exactly <code>len</code> bytes from the stream.
     *
     * @param b   the buffer to hold the read bytes
     * @param off the offset within the buffer
     * @param len the number of bytes to read
     * @return the number of bytes read (always same as <code>len</code>)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int read = 0;
        while (read < len) {
            int res = in.read(b, off + read, len - read);
            if (res == -1) {
                throw new IOException("The end of stream has been reached.");
            }
            read += res;
        }
        return len;
    }
}
