package org.dbe.studio.core.smcore.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipFile;

/**
 * @author gdorigo
 *
 */
public final class IOUtil {

    /**
     * Constructor
     */
    private IOUtil() {
        super();
    }

    /**
     * @param file ##
     * @return ##
     * @throws Exception ##
     */
    public static String loadFile(final File file) throws Exception {
        return loadBynaryFile(file).toString();
    }

    /**
     * @param file ##
     * @return ##
     * @throws Exception ##
     */
    public static ByteArrayOutputStream loadBynaryFile(final File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream((int) file.length());
        byte[] b = new byte[4096];
        int nb;
        while ((nb = fis.read(b)) != -1) {
            baos.write(b, 0, nb);
        }
        fis.close();
        return baos;
    }

    /**
     * @param url ##
     * @return ##
     * @throws Exception ##
     */
    public static String loadFile(final URL url) throws Exception {
        InputStream fis = (InputStream) url.getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        int nb;
        while ((nb = fis.read(b)) != -1) {
            baos.write(b, 0, nb);
        }
        fis.close();
        return baos.toString();
    }

    /**
     * @param zipFile ##
     * @return ##
     */
    public static int testZip(final File zipFile) {
        try {
            ZipFile zf = new ZipFile(zipFile);
            int count = 0;
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                count++;
            }
            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param file ##
     * @param strContent ##
     * @throws Exception ##
     */
    public static void saveFile(final File file, final String strContent) throws Exception {
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(strContent);
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * @param file ##
     * @param contents ##
     * @throws Exception ##
     */
    public static void saveFile(final File file, final byte[] contents) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(contents);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * @param file ##
     * @throws Exception ##
     */
    public static void dumpFile(final File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] b = new byte[10000];
        int nb;
        while ((nb = fis.read(b)) != -1) {
            System.out.print(new String(b, 0, nb));
        }
    }

    /**
     * @param from ##
     * @param to ##
     * @throws Exception ##
     */
    public static void copyFile(final File from, final File to) throws Exception {
        InputStream fin = null;
        OutputStream fout = null;
        try {
            fin = new FileInputStream(from);
            fout = new FileOutputStream(to);
            byte[] b = new byte[4096];
            int nb;
            while ((nb = fin.read(b)) != -1) {
                fout.write(b, 0, nb);
            }
            fout.close();
        } finally {
            closeInputStream(fin);
            closeOutputStream(fout);
        }
    }

    /**
     * @param in ##
     */
    public static void closeInputStream(final InputStream in) {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (Exception e) {
        }
    }

    /**
     * @param out ##
     */
    public static void closeOutputStream(final OutputStream out) {
        if (out == null) {
            return;
        }
        try {
            out.close();
        } catch (Exception e) {
        }
    }
}
