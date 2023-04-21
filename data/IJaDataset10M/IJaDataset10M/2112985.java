package com.ericdaugherty.mail.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * This class adds to another InputStream the ability to read a line of bytes
 * off the underlying stream source. The assumption is that the source bytes
 * correspond to a sequence of characters. It is desirable in certain situations
 * to be able to read a delimited series of bytes rather than have them converted
 * to a stream of characters first, since it might be possible that the character
 * set is unknown at a stage of the process or that the overhead needed to use
 * the proper one is unacceptable or that it simply is not neccessary to have the
 * bytes converted to characters. One such case is when JES reads a message from
 * an I/O source where the EOL is platform-dependent.
 *
 * @author Andreas Kyrmegalos
 */
public class DelimitedInputStream extends PushbackInputStream {

    /** A byte array corresponding to an EOL */
    protected final byte[] delimiter;

    /** A flag to indicate the size of the EOL */
    protected final boolean twoByteDelimiter;

    /** The size of the pushback buffer */
    protected final int maxBufferSize;

    /** A constructor using a fixed buffer size */
    public DelimitedInputStream(InputStream in) throws IOException {
        this(in, 16);
    }

    /** A constructor using an application defined delimiter and a fixed buffer size */
    public DelimitedInputStream(InputStream in, byte[] delimiter) throws IOException {
        this(in, 16, delimiter);
    }

    /** A constructor using an application defined buffer size */
    public DelimitedInputStream(InputStream in, int maxBufferSize) throws IOException {
        this(in, maxBufferSize, System.getProperty("line.separator").getBytes("US-ASCII"));
    }

    /** A constructor using an application defined buffer size and delimiter */
    public DelimitedInputStream(InputStream in, int maxBufferSize, byte[] delimiter) throws IOException {
        super(in, maxBufferSize);
        this.maxBufferSize = maxBufferSize;
        this.delimiter = delimiter;
        this.twoByteDelimiter = delimiter.length > 1;
    }

    /**
    * The method to extract a single line of bytes
    *
    * @return byte[] a line of bytes without a trailing EOL
    *
    */
    public byte[] readLine() throws IOException {
        byte[] buffer = new byte[maxBufferSize];
        int currentRead = read(buffer, 0, maxBufferSize);
        if (currentRead == -1) return null;
        for (int i = 0; i < currentRead; i++) {
            if (buffer[i] == delimiter[0]) {
                if (twoByteDelimiter) {
                    if (i + 1 == currentRead) {
                        int nextByte = read();
                        if ((byte) nextByte == delimiter[1]) {
                            byte[] returnBuffer = new byte[i];
                            System.arraycopy(buffer, 0, returnBuffer, 0, i);
                            i++;
                            return returnBuffer;
                        } else if (nextByte != -1) {
                            unread((byte) nextByte);
                        }
                    } else if (buffer[i + 1] == delimiter[1]) {
                        byte[] returnBuffer = new byte[i];
                        System.arraycopy(buffer, 0, returnBuffer, 0, i);
                        i++;
                        unread(buffer, i + 1, currentRead - i - 1);
                        return returnBuffer;
                    }
                } else {
                    byte[] returnBuffer = new byte[i];
                    System.arraycopy(buffer, 0, returnBuffer, 0, i);
                    unread(buffer, i + 1, currentRead - i - 1);
                    return returnBuffer;
                }
            }
            if (i == currentRead - 1) {
                if (currentRead == buffer.length) {
                    byte[] moreBuffer = readLine();
                    if (moreBuffer == null) {
                        return buffer;
                    } else {
                        byte[] returnBuffer = new byte[moreBuffer.length + currentRead];
                        System.arraycopy(buffer, 0, returnBuffer, 0, currentRead);
                        System.arraycopy(moreBuffer, 0, returnBuffer, currentRead, moreBuffer.length);
                        return returnBuffer;
                    }
                } else {
                    byte[] returnBuffer = new byte[currentRead];
                    System.arraycopy(buffer, 0, returnBuffer, 0, currentRead);
                    return returnBuffer;
                }
            }
        }
        return null;
    }
}
