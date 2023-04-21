package uk101.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class to manipulate input and output streams.
 *
 * @author Baldwin
 */
public class Stream {

    public static final int STREAM_SELECT = 0;

    public static final int STREAM_ASCII = 1;

    public static final int STREAM_BINARY = 2;

    public static OutputStream getOutputStream(File file, int format) {
        OutputStream out = null;
        try {
            if (format == STREAM_ASCII) {
                out = new UK101OutputStream(new FileWriter(file));
            } else if (format == STREAM_BINARY) {
                out = new FileOutputStream(file);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return out;
    }

    public static InputStream getInputStream(File file, int format) {
        InputStream in = null;
        try {
            if (format == STREAM_SELECT) {
                format = checkFormat(file);
            }
            if (format == STREAM_ASCII) {
                in = new UK101InputStream(new FileReader(file));
            } else if (format == STREAM_BINARY) {
                in = new FileInputStream(file);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return in;
    }

    public static int checkFormat(File file) throws IOException {
        int format = STREAM_ASCII;
        byte[] b = new byte[128];
        InputStream in = new FileInputStream(file);
        int size = in.read(b);
        for (int i = 0; i < size && format == STREAM_ASCII; i++) {
            if ((b[i] < 32 || b[i] > 126) && b[i] != '\r' && b[i] != '\n' && b[i] != '\t') format = STREAM_BINARY; else if ((b[i] == '\r' && i + 1 < size && b[i + 1] != '\n')) format = STREAM_BINARY;
        }
        in.close();
        return format;
    }
}
