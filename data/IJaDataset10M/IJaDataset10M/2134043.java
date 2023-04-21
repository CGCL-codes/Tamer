package gnu.inet.nntp;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A stream to which article contents should be written.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public final class PostStream extends FilterOutputStream {

    NNTPConnection connection;

    boolean isTakethis;

    byte last;

    PostStream(NNTPConnection connection, boolean isTakethis) {
        super(connection.out);
        this.connection = connection;
        this.isTakethis = isTakethis;
    }

    public void write(int c) throws IOException {
        super.write(c);
        last = (byte) c;
    }

    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);
    }

    public void write(byte[] bytes, int pos, int len) throws IOException {
        super.write(bytes, pos, len);
        if (len > 0) {
            last = bytes[pos + len - 1];
        }
    }

    /**
   * Close the stream.
   * This calls NNTPConnection.postComplete().
   */
    public void close() throws IOException {
        if (last != 0x0d) {
            write(0x0d);
        }
        if (isTakethis) {
            connection.takethisComplete();
        } else {
            connection.postComplete();
        }
    }
}
