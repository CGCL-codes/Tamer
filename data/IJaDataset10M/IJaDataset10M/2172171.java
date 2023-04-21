package org.elf.common.stream;

import java.io.*;

/**
 * Clase de utilidad para los Stream.
 * @author <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class StreamUtil {

    /**
     * No p�rmitimos que se cree la clase.
     */
    private StreamUtil() {
    }

    ;

    /**
     * Copia el contenido del Reader en el Writer
     * @param in Stream del que se leen los datos
     * @param out Stream al que se graban los datos
     */
    public static void copy(BufferedReader in, PrintWriter out) {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Copia el contenido del InputStream en el OutputStream
     * @param in Stream del que se leen los datos
     * @param out Stream al que se graban los datos
     */
    public static void copy(Reader in, Writer out) {
        try {
            int bufferSize = 2048;
            char[] buffer = new char[bufferSize];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Copia el contenido del InputStream en el OutputStream
     * @param in Stream del que se leen los datos
     * @param out Stream al que se graban los datos
     */
    public static void copy(InputStream in, OutputStream out) {
        try {
            int bufferSize = 2048;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
