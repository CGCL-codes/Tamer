package net.sf.refactorit.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * We need this class because FileUtil can not be used inside RefactorIT Updater's
 * update.jar due to its dependencies to other classes
 * (update.jar would get ClassNotFoundExceptions).
 */
public class FileReadWriteUtil {

    private static boolean mockFailureInWriteStringToFile = false;

    private static final Vector lockedFiles = new Vector();

    public static String read(final File file) {
        try {
            return read(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString() + " " + file.getAbsolutePath());
        }
    }

    public static String read(final Reader reader) throws IOException {
        Reader br = reader;
        if (!(br instanceof BufferedReader)) {
            br = new BufferedReader(br);
        }
        StringBuffer result = new StringBuffer(1024);
        char[] buffer = new char[4096];
        while (true) {
            int lengthChars = br.read(buffer);
            if (lengthChars < 0) {
                break;
            }
            result.append(buffer, 0, lengthChars);
        }
        br.close();
        return result.toString();
    }

    public static byte[] read(final InputStream in) throws IOException {
        byte[] data = new byte[1024];
        int count = -1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while ((count = in.read(data)) != -1) {
                out.write(data, 0, count);
            }
        } finally {
            out.close();
        }
        return out.toByteArray();
    }

    /** Usable only when the length is known in advance */
    public static byte[] read(final InputStream in, final int length) throws IOException {
        final byte[] buffer = new byte[length];
        int c = 0, t;
        while (c < length) {
            t = in.read(buffer, c, length - c);
            if (t < 0) {
                break;
            }
            c += t;
        }
        return buffer;
    }

    /** we'll ignore contents, so this is thread safe */
    private static final byte[] lengthBuf = new byte[8 * 1024];

    public static int length(final InputStream in) throws IOException {
        int len = 0;
        try {
            while (true) {
                int n = in.read(lengthBuf);
                if (n < 0) {
                    break;
                }
                len += n;
            }
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return len;
    }

    public static void writeStringToFile(File file, String string) {
        try {
            writeStringToWriter(string, new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void writeStringToWriter(String string, Writer writer) throws IOException {
        if (FileReadWriteUtil.mockFailureInWriteStringToFile) {
            throw new IOException("Mocked failure for tests");
        }
        Writer wr = new BufferedWriter(writer);
        try {
            wr.write(string);
        } finally {
            wr.close();
        }
    }

    public static synchronized void appendStringToFile(File file, String string) {
        while (FileReadWriteUtil.lockedFiles.contains(file.getName())) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        FileReadWriteUtil.lockedFiles.add(file.getName());
        try {
            RandomAccessFile rafile = new RandomAccessFile(file, "rw");
            rafile.seek(rafile.length());
            rafile.writeBytes(string);
            rafile.close();
        } catch (Throwable e) {
            FileReadWriteUtil.lockedFiles.remove(file.getName());
            throw new RuntimeException(e.toString());
        }
        FileReadWriteUtil.lockedFiles.remove(file.getName());
    }

    public static void writePropertiesToFile(File file, Properties fileProps) {
        String props = "";
        for (Enumeration q = fileProps.propertyNames(); q.hasMoreElements(); ) {
            String name = (String) q.nextElement();
            String value = fileProps.getProperty(name);
            props += name + ":" + value + "\n";
        }
        writeStringToFile(file, props);
    }
}
