package org.commsuite.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

/**
 * @since 1.0
 * @author Agnieszka Wisniewska
 */
public final class IOUtil {

    private IOUtil() {
    }

    public static byte[] convertIS2Bytes(InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024 * 10];
        int num;
        while ((num = is.read(buffer)) >= 0) {
            baos.write(buffer, 0, num);
        }
        return baos.toByteArray();
    }

    public static byte[] readFile(final String path) {
        try {
            final File file = new File(path);
            final FileInputStream fis = new FileInputStream(file);
            return convertIS2Bytes(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
