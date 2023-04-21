package org.rubypeople.rdt.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.rubypeople.rdt.internal.core.util.CharOperation;

public class Util {

    public interface Displayable {

        public String displayString(Object o);
    }

    private static final int DEFAULT_READING_SIZE = 8192;

    public static final String UTF_8 = "UTF-8";

    public static String LINE_SEPARATOR = System.getProperty("line.separator");

    public static byte[] getFileByteContent(File file) throws IOException {
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return getInputStreamAsByteArray(stream, (int) file.length());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * Converts an array of Objects into String.
	 */
    public static String toString(Object[] objects, Displayable renderer) {
        if (objects == null) return "";
        StringBuffer buffer = new StringBuffer(10);
        for (int i = 0; i < objects.length; i++) {
            if (i > 0) buffer.append(", ");
            buffer.append(renderer.displayString(objects[i]));
        }
        return buffer.toString();
    }

    /**
	 * Returns the given input stream's contents as a byte array. If a length is
	 * specified (ie. if length != -1), only length bytes are returned.
	 * Otherwise all bytes in the stream are returned. Note this doesn't close
	 * the stream.
	 * 
	 * @throws IOException
	 *             if a problem occured reading the stream.
	 */
    public static byte[] getInputStreamAsByteArray(InputStream stream, int length) throws IOException {
        byte[] contents;
        if (length == -1) {
            contents = new byte[0];
            int contentsLength = 0;
            int amountRead = -1;
            do {
                int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);
                if (contentsLength + amountRequested > contents.length) {
                    System.arraycopy(contents, 0, contents = new byte[contentsLength + amountRequested], 0, contentsLength);
                }
                amountRead = stream.read(contents, contentsLength, amountRequested);
                if (amountRead > 0) {
                    contentsLength += amountRead;
                }
            } while (amountRead != -1);
            if (contentsLength < contents.length) {
                System.arraycopy(contents, 0, contents = new byte[contentsLength], 0, contentsLength);
            }
        } else {
            contents = new byte[length];
            int len = 0;
            int readSize = 0;
            while ((readSize != -1) && (len != length)) {
                len += readSize;
                readSize = stream.read(contents, len, length - len);
            }
        }
        return contents;
    }

    /**
	 * Converts an array of Objects into String.
	 */
    public static String toString(Object[] objects) {
        return toString(objects, new Displayable() {

            public String displayString(Object o) {
                if (o == null) return "null";
                return o.toString();
            }
        });
    }

    /**
	 * Returns the contents of the given file as a char array.
	 * When encoding is null, then the platform default one is used
	 * @throws IOException if a problem occured reading the file.
	 */
    public static char[] getFileCharContent(File file, String encoding) throws IOException {
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return getInputStreamAsCharArray(stream, (int) file.length(), encoding);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * Returns the given input stream's contents as a character array.
	 * If a length is specified (ie. if length != -1), this represents the number of bytes in the stream.
	 * Note this doesn't close the stream.
	 * @throws IOException if a problem occured reading the stream.
	 */
    public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
        InputStreamReader reader = null;
        try {
            reader = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);
        } catch (UnsupportedEncodingException e) {
            reader = new InputStreamReader(stream);
        }
        char[] contents;
        int totalRead = 0;
        if (length == -1) {
            contents = CharOperation.NO_CHAR;
        } else {
            contents = new char[length];
        }
        while (true) {
            int amountRequested;
            if (totalRead < length) {
                amountRequested = length - totalRead;
            } else {
                int current = reader.read();
                if (current < 0) break;
                amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);
                if (totalRead + 1 + amountRequested > contents.length) System.arraycopy(contents, 0, contents = new char[totalRead + 1 + amountRequested], 0, totalRead);
                contents[totalRead++] = (char) current;
            }
            int amountRead = reader.read(contents, totalRead, amountRequested);
            if (amountRead < 0) break;
            totalRead += amountRead;
        }
        int start = 0;
        if (totalRead > 0 && UTF_8.equals(encoding)) {
            if (contents[0] == 0xFEFF) {
                totalRead--;
                start = 1;
            }
        }
        if (totalRead < contents.length) System.arraycopy(contents, start, contents = new char[totalRead], 0, totalRead);
        return contents;
    }

    public static String camelCaseToUnderscores(String name) {
        if (name == null) return null;
        if (name.length() == 0) return "";
        StringBuffer newName = new StringBuffer();
        boolean lastWasUpper = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            newName.append(Character.toLowerCase(c));
            if (lastWasUpper && Character.isLowerCase(c)) {
                if (newName.length() > 2) newName.insert(newName.length() - 2, "_");
                lastWasUpper = false;
            }
            if (Character.isUpperCase(c)) {
                lastWasUpper = true;
            }
        }
        return newName.toString();
    }
}
