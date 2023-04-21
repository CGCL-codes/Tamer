package gnu.inet.ftp;

import java.io.*;

/**
 * The <code>AsciiInputStream</code> class acts as a filter to convert incoming
 * ASCII FTP streams to the system's local ASCII format by filtering and
 * converting line termination strings. Note that the class as currently
 * written will only handle <code>\r\n</code> or single character line
 * termination strings; a more technically correct implementation would be
 * preferable.
 */
public class AsciiInputStream extends FilterInputStream {

    protected boolean active;

    protected int eol;

    protected int buffer;

    protected int markBuffer;

    /**
    * Creates an AsciiInputStream by passing <code>in</code> to its superclass'
    * constructor and determining the system-specific line termination string.
    *
    * @param  in  the underlying input stream.
    * 
    * @throws  Exception  if system line separator is longer than 1 char and is
    *                     not <code>\r\n</code>
    */
    public AsciiInputStream(InputStream in) throws Exception {
        super(in);
        String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator.equals("\r\n")) {
            active = false;
        } else if (lineSeparator.length() > 1) {
            throw new Exception("System line separator longer than 1 char");
        } else {
            active = true;
            eol = lineSeparator.charAt(0);
            buffer = -1;
        }
    }

    /**
    * Reads the next byte of data.
    *
    * @return  the next byte of data, or -1 for EOF
    * @throws  IOException  if an I/O error occurs.
    */
    public int read() throws IOException {
        if (!active) return in.read();
        int cur;
        if (buffer < 0) {
            cur = in.read();
        } else {
            cur = buffer;
            buffer = -1;
        }
        if (cur != '\r') {
            return cur;
        } else if ((buffer = in.read()) != '\n') {
            return cur;
        } else {
            buffer = -1;
            return eol;
        }
    }

    /**
    * Reads up to <code>byte.length</code> bytes of data from this input stream
    * into an array of bytes.
    *
    * @param   b  the buffer into which the data is read.
    * 
    * @return  the total number of bytes read into the buffer, or -1 for EOF.
    * @throws  IOException  if an I/O error occurs.
    */
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
    * Reads up to <code>len</code> bytes of data from this input stream into an
    * array of bytes.
    * 
    * @param  b    the buffer into which the data is read.
    * @param  off  the start offset of the data.
    * @param  len  the maximum number of bytes read.
    * 
    * @return  the total number of bytes read into the buffer, or -1 for EOF.
    * @throws  IOException  if an I/O error occurs.
    */
    public int read(byte[] b, int off, int len) throws IOException {
        if (!active) return in.read(b, off, len);
        if (off < 0 || len < 0 || off + len > b.length) throw new IndexOutOfBoundsException();
        if (len == 0) return 0;
        int cur = read();
        if (cur < 0) return 0;
        b[off] = (byte) cur;
        int count = 1;
        while (count < len) {
            try {
                cur = read();
            } catch (IOException e) {
                cur = -1;
            }
            if (cur < 0) return count;
            b[off + count] = (byte) cur;
            count++;
        }
        return count;
    }

    /**
    * Skips over and discards n bytes of data from the input stream.
    *
    * @param  n  the number of bytes to be skipped.
    *
    * @return  the actual number of bytes skipped.
    * @throws  IOException  if an I/O error occurs.
    */
    public long skip(long n) throws IOException {
        if (n <= 0L) return 0;
        if (!active || buffer < 0) return in.skip(n);
        buffer = -1;
        return 1L + in.skip(n - 1L);
    }

    /**
    * Returns the number of bytes that can be read from this input stream
    * without blocking.
    * 
    * @return  the number of bytes that can be read from the input stream
    *          without blocking.
    * @throws  IOException  if an I/O error occurs.
    */
    public int available() throws IOException {
        if (!active || buffer < 0) return in.available();
        return 1 + in.available();
    }

    /**
    * Marks the current position in this input stream.
    *
    * @param  readlimit  the maximum limit of bytes that can be read before the
    *                    mark position becomes invalid.
    */
    public void mark(int readlimit) {
        if (readlimit <= 0) return;
        if (!active) {
            in.mark(readlimit);
            return;
        }
        markBuffer = buffer;
        in.mark(readlimit - 1);
    }

    /**
    * Repositions this stream to the position at the time the mark method was
    * last called on this input stream.
    * 
    * @throws  IOException  if the stream has not been or cannot be marked or
    *                       if the mark has been invalidated.
    */
    public void reset() throws IOException {
        if (!active) {
            in.reset();
            return;
        }
        if (!in.markSupported()) throw new IOException();
        buffer = markBuffer;
        in.reset();
    }
}
