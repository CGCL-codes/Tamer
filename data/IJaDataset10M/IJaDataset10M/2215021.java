package xades4j.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for streams.
 * @author Luís
 */
public class StreamUtils {

    private StreamUtils() {
    }

    /**
     * Reads the content of an input stream and writes it into an output stream.
     * The copy is made in chunks of 1 KB.
     * @param is the input
     * @param os the output
     * @throws IOException thrown by the {@code read} and {@code write} methods of the streams
     */
    public static void readWrite(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int nRead;
        while ((nRead = is.read(buf)) != -1) {
            os.write(buf, 0, nRead);
        }
    }
}
