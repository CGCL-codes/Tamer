package gnu.java.net.protocol.ftp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A DTP output stream that implements the FTP block transfer mode.
 *
 * @author Chris Burdess (dog@gnu.org)
 */
class BlockOutputStream extends DTPOutputStream {

    static final byte RECORD = -128;

    static final byte EOF = 64;

    BlockOutputStream(DTP dtp, OutputStream out) {
        super(dtp, out);
    }

    public void write(int c) throws IOException {
        if (transferComplete) {
            return;
        }
        byte[] buf = new byte[] { RECORD, 0x00, 0x01, (byte) c };
        out.write(buf, 0, 4);
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (transferComplete) {
            return;
        }
        byte[] buf = new byte[len + 3];
        buf[0] = RECORD;
        buf[1] = (byte) ((len & 0x00ff) >> 8);
        buf[2] = (byte) (len & 0xff00);
        System.arraycopy(b, off, buf, 3, len);
        out.write(buf, 0, len);
    }

    public void close() throws IOException {
        byte[] buf = new byte[] { EOF, 0x00, 0x00 };
        out.write(buf, 0, 3);
        super.close();
    }
}
