package com.gm.core.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import com.gm.core.lang.StringUtils;

/**
 * <p>
 * Some common IO operation
 * </p>
 */
public class IOUtils {

    private IOUtils() {
    }

    /**
	 * Unconditionally close an <code>Reader</code>.
	 * <p>
	 * Equivalent to {@link Reader#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input
	 *            the Reader to close, may be null or already closed
	 */
    public static void closeQuietly(Reader input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Unconditionally close a <code>Writer</code>.
	 * <p>
	 * Equivalent to {@link Writer#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output
	 *            the Writer to close, may be null or already closed
	 */
    public static void closeQuietly(Writer output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Unconditionally close an <code>InputStream</code>.
	 * <p>
	 * Equivalent to {@link InputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param input
	 *            the InputStream to close, may be null or already closed
	 */
    public static void closeQuietly(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Unconditionally close an <code>OutputStream</code>.
	 * <p>
	 * Equivalent to {@link OutputStream#close()}, except any exceptions will be
	 * ignored. This is typically used in finally blocks.
	 * 
	 * @param output
	 *            the OutputStream to close, may be null or already closed
	 */
    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Get String form appointing InputStream
	 * 
	 * @param in
	 *            InputStream
	 * @param encoding
	 * @return
	 */
    public static String toString(InputStream in, String encoding) {
        BufferedReader reader = null;
        StringBuilder sb = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, encoding));
            sb = new StringBuilder();
            int c;
            c = reader.read();
            while (c != -1) {
                sb.append((char) c);
                c = reader.read();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return sb.toString();
    }

    public static Writer NULL_WRITER = new NullWriter();

    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[8192];
        int n = 0;
        while ((n = reader.read(buf)) != -1) {
            writer.write(buf, 0, n);
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int n = 0;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    public static String readFile(File file) throws IOException {
        Reader in = new FileReader(file);
        StringWriter out = new StringWriter();
        copy(in, out);
        return out.toString();
    }

    public static String readFile(File file, String encoding) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        return toString(encoding, inputStream);
    }

    public static String toString(InputStream inputStream) throws UnsupportedEncodingException, IOException {
        Reader reader = new InputStreamReader(inputStream);
        StringWriter writer = new StringWriter();
        copy(reader, writer);
        return writer.toString();
    }

    public static String toString(String encoding, InputStream inputStream) throws UnsupportedEncodingException, IOException {
        Reader reader = new InputStreamReader(inputStream, encoding);
        StringWriter writer = new StringWriter();
        copy(reader, writer);
        return writer.toString();
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) throw new IOException("File '" + file + "' exists but is a directory");
            if (!file.canWrite()) throw new IOException("File '" + file + "' cannot be written to");
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) throw new IOException("File '" + file + "' could not be created");
        }
        return new FileOutputStream(file);
    }

    public static void writeByteArrayToFile(File file, byte data[]) throws IOException {
        OutputStream out = null;
        out = openOutputStream(file);
        out.write(data);
        IOUtils.closeQuietly(out);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    /**
	 * <p>使用文件输入流存储文件</p>
	 * @param filePath 文件路径
	 * @param in 文件输入流
	 */
    public static void saveFile(String filePath, InputStream in) {
        FileOutputStream out = null;
        try {
            if (in.available() > 0) {
                out = new FileOutputStream(filePath);
                byte[] data = new byte[in.available()];
                in.read(data);
                out.write(data);
                out.close();
            } else {
                byte[] data = IOUtils.toByteArray(in);
                writeByteArrayToFile(new File(filePath), data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(File file, String content) {
        saveFile(file, content, null, false);
    }

    public static void saveFile(File file, String content, boolean append) {
        saveFile(file, content, null, append);
    }

    public static void saveFile(File file, String content, String encoding) {
        saveFile(file, content, encoding, false);
    }

    public static void saveFile(File file, String content, String encoding, boolean append) {
        try {
            FileOutputStream output = new FileOutputStream(file, append);
            Writer writer = StringUtils.isBlank(encoding) ? new OutputStreamWriter(output) : new OutputStreamWriter(output, encoding);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class NullWriter extends Writer {

        public void close() throws IOException {
        }

        public void flush() throws IOException {
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
        }
    }

    public static void copyAndClose(InputStream in, OutputStream out) throws IOException {
        try {
            copy(in, out);
        } finally {
            close(in, out);
        }
    }

    public static void close(InputStream in, OutputStream out) {
        try {
            if (in != null) in.close();
        } catch (Exception e) {
        }
        ;
        try {
            if (out != null) out.close();
        } catch (Exception e) {
        }
        ;
    }
}
