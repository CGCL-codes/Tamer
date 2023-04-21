package org.nexopenframework.ide.eclipse.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.nexopenframework.ide.eclipse.commons.log.Logger;

/**
 * <p> NexOpen Framework</p>
 * 
 * <p>Utility class for Input/Output functionalities</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public abstract class IOUtils {

    /**
     * The default buffer size to use.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * 
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since Commons IO 1.1
     */
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Unconditionally close an <code>InputStream</code>.
     * <p>
     * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param input  the InputStream to close, may be null or already closed
     */
    public static void closeQuietly(final InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (final IOException ioe) {
            Logger.getLog().debug("I/O exception closing resource", ioe);
        }
    }

    /**
     * Unconditionally close an <code>OutputStream</code>.
     * <p>
     * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored.
     * This is typically used in finally blocks.
     *
     * @param output  the OutputStream to close, may be null or already closed
     */
    public static void closeQuietly(final OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (final IOException ioe) {
            Logger.getLog().debug("I/O exception closing resource", ioe);
        }
    }
}
