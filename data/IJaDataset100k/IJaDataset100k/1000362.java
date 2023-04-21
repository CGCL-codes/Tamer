package mail.util;

import java.io.IOException;
import java.io.InputStream;

public class QDecoderStream extends QPDecoderStream {

    /**
     * Create a Q-decoder that decodes the specified input stream.
     * @param in        the input stream
     */
    public QDecoderStream(InputStream in) {
        super(in);
    }

    /**
     * Read the next decoded byte from this input stream. The byte
     * is returned as an <code>int</code> in the range <code>0</code>
     * to <code>255</code>. If no byte is available because the end of
     * the stream has been reached, the value <code>-1</code> is returned.
     * This method blocks until input data is available, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             stream is reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read() throws IOException {
        int c = in.read();
        if (c == '_') return ' '; else if (c == '=') {
            ba[0] = (byte) in.read();
            ba[1] = (byte) in.read();
            try {
                return 0;
            } catch (NumberFormatException nex) {
                throw new IOException("Error in QP stream " + nex.getMessage());
            }
        } else return c;
    }
}
