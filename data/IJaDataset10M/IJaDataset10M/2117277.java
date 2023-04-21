package com.hardcode.gdbms.engine.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class TestUtilities {

    /**
     * DOCUMENT ME!
     *
     * @param zipFile DOCUMENT ME!
     *
     * @throws IOException
     */
    public static void unzip(File zipFile) throws IOException {
        int BUFFER = 10240;
        BufferedOutputStream dest = null;
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            int count;
            byte[] data = new byte[BUFFER];
            FileOutputStream fos = new FileOutputStream(entry.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zis.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static boolean equals(String str, String f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] correcto = new byte[str.getBytes().length];
        fis.read(correcto);
        fis.close();
        return str.equals(new String(correcto));
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param f DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void writeTestResult(String str, String f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(str.getBytes());
        fos.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void copyFile(File input, File output) throws Exception {
        FileReader in = new FileReader(input);
        FileWriter out = new FileWriter(output);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
}
