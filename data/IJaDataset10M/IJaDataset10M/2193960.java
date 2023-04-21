package gralej.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author Martin Lazarov
 */
public final class Streams {

    public static String toString(InputStream is) {
        return toString(is, Charset.defaultCharset());
    }

    public static String toString(InputStream is, String charsetName) {
        return toString(is, Charset.forName(charsetName));
    }

    public static String toString(InputStream is, Charset charset) {
        try {
            StringBuilder s = new StringBuilder();
            char[] buffer = new char[0x1000];
            InputStreamReader isr = new InputStreamReader(is, charset);
            int c;
            while ((c = isr.read(buffer)) != -1) {
                s.append(buffer, 0, c);
            }
            return s.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
