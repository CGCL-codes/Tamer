package php.java.bridge.http;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import php.java.bridge.Util;

/**
 * An output stream which writes data in HTTP chunks.
 * 
 * @author jostb
 */
public class ChunkedOutputStream extends FilterOutputStream {

    /**
     * Create new new chunked output stream
     * @param out
     */
    public ChunkedOutputStream(OutputStream out) {
        super(new BufferedOutputStream(out));
    }

    private static final byte[] RN0 = "0\r\n\r\n".getBytes();

    private void writeEOF() throws IOException {
        out.write(RN0);
    }

    private byte[] buf = new byte[8];

    /**
     * Write a length as hex digits.
     * @param length the length, must be > 0
     * @throws IOException
     */
    private void writeHex(int length) throws IOException {
        int i = buf.length - 1;
        for (; length > 0; i--) {
            buf[i] = Util.HEX_DIGITS[0xF & length];
            length >>>= 4;
        }
        i++;
        out.write(buf, i, buf.length - i);
    }

    /**{@inheritDoc}*/
    public void write(byte[] buf, int pos, int len) throws IOException {
        if (len == 0) {
            writeEOF();
        } else {
            writeHex(len);
            out.write(Util.RN);
            out.write(buf, pos, len);
            out.write(Util.RN);
        }
        out.flush();
    }

    /**
     * Write trailing 0\r\n and flush the underlying output stream 
     */
    public void eof() throws IOException {
        writeEOF();
        super.flush();
    }
}
