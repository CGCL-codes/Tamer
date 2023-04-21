package dev;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

public class IOUtil {

    public static void pipe(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buff = new byte[16 * 1024];
            while (true) {
                int bytesRead = in.read(buff);
                if (bytesRead == -1) break;
                out.write(buff, 0, bytesRead);
            }
        } finally {
            out.close();
        }
    }

    public static void pipe(Reader in, Writer out) throws IOException {
        try {
            char[] buff = new char[16 * 1024];
            while (true) {
                int bytesRead = in.read(buff);
                if (bytesRead == -1) break;
                out.write(buff, 0, bytesRead);
            }
        } finally {
            out.close();
        }
    }

    public static synchronized byte[] inputStreamToBytes(InputStream is) throws IOException {
        return inputStreamToBytes(is, 32);
    }

    public static synchronized byte[] inputStreamToBytes(InputStream is, int size) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        pipe(is, baos);
        return baos.toByteArray();
    }

    public static void stringToWriter(String s, Writer w) throws IOException {
        pipe(new StringReader(s), w);
    }

    public static String readerToString(Reader r) throws IOException {
        CharArrayWriter w = new CharArrayWriter();
        pipe(r, w);
        return w.toString();
    }

    public static String readerToString(Reader r, int length) throws IOException {
        CharArrayWriter w = new CharArrayWriter(length);
        pipe(r, w);
        return w.toString();
    }

    public static void stringToFile(String s, File f, String charset) throws IOException {
        FileOutputStream out = new FileOutputStream(f);
        ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes(charset));
        pipe(in, out);
    }

    public static void stringToFile(String s, File f) throws IOException {
        stringToFile(s, f, "UTF-8");
    }

    public static String fileToString(File f) throws IOException {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pipe(in, out);
        return new String(out.toByteArray(), "UTF-8");
    }

    public static boolean deleteDir(File dir) {
        if (!dir.exists()) return true;
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
