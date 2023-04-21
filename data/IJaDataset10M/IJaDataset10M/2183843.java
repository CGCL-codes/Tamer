package com.shoplifes.ant.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class IOUtils {

    private IOUtils() {
    }

    public static String toString(InputStream is) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            return new String(bos.toByteArray());
        } catch (Exception e) {
        } finally {
            try {
                bos.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
