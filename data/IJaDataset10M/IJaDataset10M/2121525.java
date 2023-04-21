package net.sf.djdoc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.File;

/**
 * This utility class contains a number of useful static methods related to
 * Stream manipulation.
 */
public class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private IOUtils() {
    }

    /**
   * Reads byte data in chunks of a specified size from one stream and writes
   * it into another stream.
   *
   * @param input The InputStream from which to read the data
   * @param output The OutputStream to which to write the data
   * @throws IOException
   */
    public static void copy(final InputStream input, final OutputStream output) throws IOException {
        if (input == null) {
            return;
        }
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
    }

    /**
   * Reads character data in chunks of a specified size from a Reader and
   * writes it into a Writer.
   *
   * @param input The Reader from which to read the data
   * @param output The Writer to which to write the data
   * @throws IOException
   */
    public static void copy(final Reader input, final Writer output) throws IOException {
        if (input == null) {
            return;
        }
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        int n;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
    }

    /**
   * Returns the first existing file from a list of filenames.
   *
   * @param fileNames A list of names of (existing or non-existing) files
   * @return The first existing file from the specified list
   */
    public static File getFile(String... fileNames) {
        for (String s : fileNames) {
            File retVal = new File(s);
            if (retVal.exists()) {
                return retVal;
            }
        }
        return null;
    }
}
