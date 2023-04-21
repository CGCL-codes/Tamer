package org.systemsbiology.apps.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionUtil {

    public static byte[] compress(String str) throws IOException {
        int size = 1024;
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(str.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(baos);
        byte[] buffer = new byte[size];
        int len;
        while ((len = bis.read(buffer, 0, size)) != -1) {
            gzip.write(buffer, 0, len);
        }
        gzip.finish();
        bis.close();
        gzip.close();
        return baos.toByteArray();
    }

    public static String uncompress(byte[] data) throws IOException {
        int size = 1024;
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[size];
        int len;
        while ((len = gzip.read(buffer, 0, size)) != -1) {
            baos.write(buffer, 0, len);
        }
        gzip.close();
        baos.close();
        return new String(baos.toByteArray());
    }
}
