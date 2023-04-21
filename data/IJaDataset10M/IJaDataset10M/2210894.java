package it.crx.software.yajngi.utils;

import java.io.*;
import com.sun.mail.util.LineInputStream;

/**
 * This class implements a UUDecoder. It is implemented as a FilterInputStream,
 * so one can just wrap this class around any input stream and read bytes from
 * this filter. The decoding is done as the bytes are read out.
 * 
 * @author John Mani
 * @author Bill Shannon
 */
public class UUDecoderStream extends FilterInputStream {

    private String fileName;

    private int modeBits;

    private byte[] buffer;

    private int bufferSize = 0;

    private int index = 0;

    private boolean haveBegin = false;

    private boolean haveEnd = false;

    private LineInputStream lineInputStream;

    /**
	 * Create a UUdecoder that decodes the specified input stream
	 * 
	 * @param in
	 *            the input stream
	 */
    public UUDecoderStream(InputStream in) {
        super(in);
        lineInputStream = new LineInputStream(in);
        buffer = new byte[45];
    }

    /**
	 * Read the next decoded byte from this input stream. The byte is returned
	 * as an <code>int</code> in the range <code>0</code> to <code>255</code>.
	 * If no byte is available because the end of the stream has been reached,
	 * the value <code>-1</code> is returned. This method blocks until input
	 * data is available, the end of the stream is detected, or an exception is
	 * thrown.
	 * 
	 * @return next byte of data, or <code>-1</code> if the end of stream is
	 *         reached.
	 * @exception IOException
	 *                if an I/O error occurs.
	 * @see java.io.FilterInputStream#in
	 */
    public int read() throws IOException {
        if (index >= bufferSize) {
            readBegin();
            if (!decode()) return -1;
            index = 0;
        }
        return buffer[index++] & 0xff;
    }

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

    public boolean markSupported() {
        return false;
    }

    public int available() throws IOException {
        return ((in.available() * 3) / 4 + (bufferSize - index));
    }

    /**
	 * Get the "name" field from the prefix. This is meant to be the pathname of
	 * the decoded file
	 * 
	 * @return name of decoded file
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
    public String getFileName() throws IOException {
        readBegin();
        return fileName;
    }

    /**
	 * Get the "mode" field from the prefix. This is the permission mode of the
	 * source file.
	 * 
	 * @return permission mode of source file
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
    public int getModeBits() throws IOException {
        readBegin();
        return modeBits;
    }

    /**
	 * UUencoded streams start off with the line: "begin <mode> <filename>"
	 * Search for this prefix and gobble it up.
	 */
    private void readBegin() throws IOException {
        if (haveBegin) {
            return;
        }
        String line;
        while (true) {
            line = lineInputStream.readLine();
            if (line == null) throw new IOException("UUDecoder error: No Begin");
            if (line.regionMatches(true, 0, "begin", 0, 5)) {
                try {
                    modeBits = Integer.parseInt(line.substring(6, 9));
                } catch (NumberFormatException ex) {
                    throw new IOException("UUDecoder error: " + ex.toString());
                }
                fileName = line.substring(10);
                haveBegin = true;
                return;
            }
        }
    }

    private boolean decode() throws IOException {
        if (haveEnd) {
            return false;
        }
        bufferSize = 0;
        String line;
        do {
            line = lineInputStream.readLine();
            if (line == null) throw new IOException("Missing End");
            if (line.regionMatches(true, 0, "end", 0, 3)) {
                haveEnd = true;
                return false;
            }
        } while (line.length() == 0);
        int count = line.charAt(0);
        if (count < ' ') throw new IOException("Buffer format error");
        count = (count - ' ') & 0x3f;
        if (count == 0) {
            line = lineInputStream.readLine();
            if (line == null || !line.regionMatches(true, 0, "end", 0, 3)) throw new IOException("Missing End");
            haveEnd = true;
            return false;
        }
        int need = ((count * 8) + 5) / 6;
        if (line.length() < need + 1) throw new IOException("Short buffer error");
        int i = 1;
        byte a, b;
        while (bufferSize < count) {
            a = (byte) ((line.charAt(i++) - ' ') & 0x3f);
            b = (byte) ((line.charAt(i++) - ' ') & 0x3f);
            buffer[bufferSize++] = (byte) (((a << 2) & 0xfc) | ((b >>> 4) & 3));
            if (bufferSize < count) {
                a = b;
                b = (byte) ((line.charAt(i++) - ' ') & 0x3f);
                buffer[bufferSize++] = (byte) (((a << 4) & 0xf0) | ((b >>> 2) & 0xf));
            }
            if (bufferSize < count) {
                a = b;
                b = (byte) ((line.charAt(i++) - ' ') & 0x3f);
                buffer[bufferSize++] = (byte) (((a << 6) & 0xc0) | (b & 0x3f));
            }
        }
        return true;
    }
}
