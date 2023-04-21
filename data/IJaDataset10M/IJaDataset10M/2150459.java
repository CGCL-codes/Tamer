package contacttransmut;

import java.io.*;

/**
 * This class implements a QP Decoder. It is implemented as
 * a FilterInputStream, so one can just wrap this class around
 * any input stream and read bytes from this filter. The decoding
 * is done as the bytes are read out.
 *
 * @author John Mani
 */
public class QPDecoderStream extends FilterInputStream {

    protected byte[] ba = new byte[2];

    protected int spaces = 0;

    /**
     * Create a Quoted Printable decoder that decodes the specified
     * input stream.
     * @param in        the input stream
     */
    public QPDecoderStream(InputStream in) {
        super(new PushbackInputStream(in, 2));
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
        if (spaces > 0) {
            spaces--;
            return ' ';
        }
        int c = in.read();
        if (c == ' ') {
            while ((c = in.read()) == ' ') spaces++;
            if (c == '\r' || c == '\n' || c == -1) spaces = 0; else {
                ((PushbackInputStream) in).unread(c);
                c = ' ';
            }
            return c;
        } else if (c == '=') {
            int a = in.read();
            if (a == '\n') {
                return read();
            } else if (a == '\r') {
                int b = in.read();
                if (b != '\n') ((PushbackInputStream) in).unread(b);
                return read();
            } else if (a == -1) {
                return -1;
            } else {
                ba[0] = (byte) a;
                ba[1] = (byte) in.read();
                try {
                    return ASCIIUtility.parseInt(ba, 0, 2, 16);
                } catch (NumberFormatException nex) {
                    ((PushbackInputStream) in).unread(ba);
                    return c;
                }
            }
        }
        return c;
    }

    /**
     * Reads up to <code>len</code> decoded bytes of data from this input stream
     * into an array of bytes. This method blocks until some input is
     * available.
     * <p>
     *
     * @param      buf   the buffer into which the data is read.
     * @param      off   the start offset of the data.
     * @param      len   the maximum number of bytes read.
     * @return     the total number of bytes read into the buffer, or
     *             <code>-1</code> if there is no more data because the end of
     *             the stream has been reached.
     * @exception  IOException  if an I/O error occurs.
     */
    public int read(byte[] buf, int off, int len) throws IOException {
        int i, c;
        for (i = 0; i < len; i++) {
            if ((c = read()) == -1) {
                if (i == 0) i = -1;
                break;
            }
            buf[off + i] = (byte) c;
        }
        return i;
    }

    /**
     * Tests if this input stream supports marks. Currently this class
     * does not support marks
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * Returns the number of bytes that can be read from this input
     * stream without blocking. The QP algorithm does not permit
     * a priori knowledge of the number of bytes after decoding, so
     * this method just invokes the <code>available</code> method
     * of the original input stream.
     */
    public int available() throws IOException {
        return in.available();
    }
}
