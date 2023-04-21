package org.mandiwala.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Utilities for handling Streams.
 */
public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * Reads the input stream and returns the contents as a string.
     * 
     * @param is input stream to read
     * 
     * @return the contents of the input stream
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readAndCloseStream(InputStream is) throws IOException {
        try {
            return IOUtils.toString(is);
        } finally {
            is.close();
        }
    }

    /**
     * Copies input stream to an output stream. This is being done in 1KB chunks. Closes both after
     * copying.
     * 
     * @param is the input stream to read
     * @param os the output stream to write to
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copyAndCloseStream(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } finally {
            try {
                os.close();
            } finally {
                is.close();
            }
        }
    }
}
