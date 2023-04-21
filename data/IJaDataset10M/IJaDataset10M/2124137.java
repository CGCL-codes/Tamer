package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Do gzip compression and uncompression.
 * 
 * @author Xuefeng
 */
public final class GZipUtil {

    /**
     * Do a gzip operation.
     */
    public static byte[] gzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPOutputStream output = null;
        try {
            output = new GZIPOutputStream(byteOutput);
            output.write(data);
        } catch (IOException e) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
        return byteOutput.toByteArray();
    }

    /**
     * Do a g-unzip operation.
     */
    public static byte[] gunzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPInputStream input = null;
        try {
            input = new GZIPInputStream(new ByteArrayInputStream(data));
            byte[] buffer = new byte[1024];
            int n = 0;
            for (; ; ) {
                n = input.read(buffer);
                if (n <= 0) break;
                byteOutput.write(buffer, 0, n);
            }
        } catch (IOException e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ioe) {
                }
            }
        }
        return byteOutput.toByteArray();
    }
}
