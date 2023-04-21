package com.waxayaz.TomcatMI.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Waxayaz
 *
 */
public class ZipUtils {

    private static final int BUFFER = 2048;

    public static void unzip(String file, String outputDir) {
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(file);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                if (entry.isDirectory()) {
                    File dir = new File(outputDir + File.separator + entry.getName());
                    System.out.println("Creating dir " + dir.getAbsolutePath());
                    dir.mkdir();
                } else {
                    File f = new File(outputDir + File.separator + entry.getName());
                    FileOutputStream fos = new FileOutputStream(f);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
