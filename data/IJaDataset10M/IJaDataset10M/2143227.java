package org.apache.ws.commons.tcpmon.core.filter;

import java.io.UnsupportedEncodingException;

/**
 * Class containing utility methods to work with streams.
 */
public class StreamUtil {

    private StreamUtil() {
    }

    /**
     * Search the available data in the stream for the first occurrence of
     * the end of line sequence (\r\n).
     * 
     * @param stream the stream to search in
     * @return the offset relative to the current position in the stream
     *         where the first occurrence of the EOL sequence has been found,
     *         or -1 if there is no EOL sequence in the available part of the
     *         stream
     */
    public static int searchEndOfLine(Stream stream) {
        for (int i = 0; i < stream.available() - 1; i++) {
            if (stream.get(i) == '\r' && stream.get(i + 1) == '\n') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert a part of a stream to a string, using the ASCII charset encoding.
     */
    public static String getAsciiString(Stream stream, int begin, int end) {
        StringBuilder buffer = new StringBuilder(end - begin);
        for (int i = begin; i < end; i++) {
            buffer.append((char) stream.get(i));
        }
        return buffer.toString();
    }

    public static void insertAsciiString(Stream stream, String s) {
        byte[] b;
        try {
            b = s.getBytes("ascii");
        } catch (UnsupportedEncodingException ex) {
            throw new StreamException(ex);
        }
        stream.insert(b, 0, b.length);
    }

    /**
     * Search the stream for occurrences of given patterns.
     * If an occurrence is found, the method will skip all content in the stream
     * before the position where the pattern was found. Otherwise it will skip
     * as much content as possible.
     * 
     * @param stream the stream to search
     * @param patterns the patterns to search for
     * @return the index of the pattern that matches, or -1 if no occurrence has been found
     */
    public static int search(Stream stream, byte[][] patterns) {
        int[] matchLengths = new int[patterns.length];
        for (int i = 0; i < stream.available(); i++) {
            byte b = (byte) stream.get(i);
            for (int j = 0; j < patterns.length; j++) {
                byte[] pattern = patterns[j];
                int matchLength = matchLengths[j];
                if (pattern[matchLength] == b) {
                    matchLength++;
                    if (matchLength == pattern.length) {
                        stream.skip(i + 1 - matchLength);
                        return j;
                    }
                } else {
                    matchLength = 0;
                }
                matchLengths[j] = matchLength;
            }
        }
        int maxMatchLength = 0;
        for (int j = 0; j < patterns.length; j++) {
            maxMatchLength = Math.max(maxMatchLength, matchLengths[j]);
        }
        stream.skip(stream.available() - maxMatchLength);
        return -1;
    }

    /**
     * Reads unsigned short in little endian byte order.
     * 
     * @param stream
     * @return
     */
    public static int readUShort(Stream stream) {
        return stream.get(1) << 8 | stream.get(0);
    }

    /**
     * Insert an integer in little endian byte order.
     * 
     * @param stream
     * @param i
     */
    public static void insertInt(Stream stream, int i) {
        insertShort(stream, i & 0xffff);
        insertShort(stream, (i >> 16) & 0xffff);
    }

    /**
     * Insert a short integer in little endian byte order.
     * 
     * @param stream
     * @param s
     */
    public static void insertShort(Stream stream, int s) {
        stream.insert((byte) (s & 0xff));
        stream.insert((byte) ((s >> 8) & 0xff));
    }
}
