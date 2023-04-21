package org.openremote.controller.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author handy.wang 2010-06-29
 *
 */
public class FileUtilOnlyForTest {

    public static void copyFile(String src, String dest) {
        File inputFile = new File(src);
        File outputFile = new File(dest);
        FileReader in;
        try {
            in = new FileReader(inputFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);
        }
        if (!f.canWrite()) {
            throw new IllegalArgumentException("Delete: write protected: " + fileName);
        }
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0) throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
        }
        boolean success = f.delete();
        if (!success) {
            throw new IllegalArgumentException("Delete: deletion failed");
        }
    }
}
