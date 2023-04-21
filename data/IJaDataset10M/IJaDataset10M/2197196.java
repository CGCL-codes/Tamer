package it.unipi.miabot.utils;

import java.io.*;

/**
 * Platform independent safe buffered reader.
 * <p>
 * COPYRIGHT NOTE: this class is taken from the following textbook:
 * <p>
 * Elliotte Rusty Harold
 * <p>
 * Java Network Programming 3rd edition
 * <p>
 * O'Reilly books
 * <p>
 * ISBN 0-596-00721-3
 * <p>
 * Example 4-1. The SafeBufferedReader class
 * <p>
 * pages 97-98
 * <p>
 * 
 * @author Elliotte Rusty Harold
 * @author Luca Benedetti (bug correction).
 */
public class SafeBufferedReader extends BufferedReader {

    private boolean lookingForLineFeed = false;

    /**
   * Creates a buffering character-input stream that uses a default-sized input
   * buffer.
   * 
   * @param in A Reader
   */
    public SafeBufferedReader(final Reader in) {
        this(in, 1024);
    }

    /**
   * Creates a buffering character-input stream that uses an input buffer of the
   * specified size.
   * 
   * @param in A Reader
   * @param bufferSize Input-buffer size
   * @exception IllegalArgumentException If bufferSize is <= 0
   */
    public SafeBufferedReader(final Reader in, final int bufferSize) {
        super(in, bufferSize);
    }

    @Override
    public String readLine() throws IOException {
        final StringBuffer sb = new StringBuffer("");
        while (true) {
            final int c = this.read();
            if (c == -1) {
                final String s = sb.toString();
                if (s.equals("")) {
                    return null;
                }
                return s;
            } else if (c == '\n') {
                if (this.lookingForLineFeed) {
                    this.lookingForLineFeed = false;
                    continue;
                }
                return sb.toString();
            } else if (c == '\r') {
                this.lookingForLineFeed = true;
                return sb.toString();
            } else {
                this.lookingForLineFeed = false;
                sb.append((char) c);
            }
        }
    }
}
