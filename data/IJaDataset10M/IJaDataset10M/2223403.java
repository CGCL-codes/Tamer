package edu.ucdavis.genomics.metabolomics.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * stellt methoden zum koieren von daten bereit
 * 
 * @author wohlgemuth
 */
public class Copy {

    /**
	 * definierte buffer gr?sse von 1 gb
	 */
    public static final int GIGABYTE = 1024 * 1024 * 1024 * 1024;

    /**
	 * definierte buffer gr?sse von 1 mb
	 */
    public static final int MEGABYTE = 1024 * 1024 * 1024;

    /**
	 * definierte buffer gr?sse von 1 kb
	 */
    public static final int KILOBYTE = 1024 * 1024;

    /**
	 * definierte buffer gr?sse von 1 b
	 */
    public static final int BYTE = 1 * 8;

    /**
	 * die buffer gr?sse die verwendet werden soll zum kopieren default = 1MB
	 */
    public static int BUFFER = KILOBYTE;

    /**
	 * kopiert ein datei von a nach b
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
    public static void copy(String from, String to) throws IOException {
        copy(new File(from), new File(to));
    }

    /**
	 * kopiert ein datei von a nach b
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
    public static void copy(File from, File to) throws IOException {
        copy(new FileInputStream(from), new FileOutputStream(to));
    }

    /**
	 * copy the streams and close them
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
    public static void copy(InputStream from, OutputStream to) throws IOException {
        copy(from, to, true);
    }

    /**
	 * copy the streams without closing them
	 * @param from
	 * @param to
	 * @throws IOException
	 */
    public static void copy(InputStream from, OutputStream to, boolean close) throws IOException {
        int size = BUFFER;
        BufferedOutputStream out = new BufferedOutputStream(to);
        BufferedInputStream in = new BufferedInputStream(from);
        byte[] buffer = new byte[size];
        int length;
        while ((length = in.read(buffer, 0, size)) != -1) {
            out.write(buffer, 0, length);
        }
        if (close) {
            in.close();
            out.flush();
            out.flush();
            out.close();
        }
    }
}
