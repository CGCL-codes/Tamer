package edu.ucsd.osdt.db.rbnb;

import java.io.*;

/**
 * Some simple file IO primitives reimplemented in Java.
 * All methods are static since there is no state.
  */
public class FileIO {

    /** Copy a file from one filename to another */
    public static void copyFile(String inName, String outName) throws FileNotFoundException, IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(inName));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outName));
        copyFile(is, os, true);
    }

    /** Copy a file from an opened InputStream to an opened OutputStream */
    public static void copyFile(InputStream is, OutputStream os, boolean close) throws IOException {
        int b;
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        is.close();
        if (close) os.close();
    }

    /** Copy a file from an opened Reader to an opened Writer */
    public static void copyFile(Reader is, Writer os, boolean close) throws IOException {
        int b;
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        is.close();
        if (close) os.close();
    }

    /** Copy a file from a filename to a PrintWriter. */
    public static void copyFile(String inName, PrintWriter pw, boolean close) throws FileNotFoundException, IOException {
        BufferedReader ir = new BufferedReader(new FileReader(inName));
        copyFile(ir, pw, close);
    }

    /** Open a file and read the first line from it. */
    public static String readLine(String inName) throws FileNotFoundException, IOException {
        BufferedReader is = new BufferedReader(new FileReader(inName));
        String line = null;
        line = is.readLine();
        is.close();
        return line;
    }

    /** The size of blocking to use */
    protected static final int BLKSIZ = 8192;

    /** Copy a data file from one filename to another, alternate method.
     * As the name suggests, use my own buffer instead of letting
     * the BufferedReader allocate and use the buffer.
     */
    public void copyFileBuffered(String inName, String outName) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(inName);
        OutputStream os = new FileOutputStream(outName);
        int count = 0;
        byte[] b = new byte[BLKSIZ];
        while ((count = is.read(b)) != -1) {
            os.write(b, 0, count);
        }
        is.close();
        os.close();
    }

    /** Read the entire content of a Reader into a String */
    public static String readerToString(Reader is) throws IOException {
        StringBuffer sb = new StringBuffer();
        char[] b = new char[BLKSIZ];
        int n;
        while ((n = is.read(b)) > 0) {
            sb.append(b, 0, n);
        }
        return sb.toString();
    }

    /** Read the content of a Stream into a String */
    public static String inputStreamToString(InputStream is) throws IOException {
        return readerToString(new InputStreamReader(is));
    }
}
