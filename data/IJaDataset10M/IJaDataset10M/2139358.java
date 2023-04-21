package org.lightframework.mvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TODO : document me
 *
 * @author User
 * @since 1.x.x
 */
public class FileUtils {

    public static boolean deleteDir(File dir) {
        if (!dir.isDirectory()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                try {
                    if (file.getCanonicalFile().getParentFile().equals(dir.getCanonicalFile())) {
                        deleteDir(file);
                        file.delete();
                    } else {
                        System.out.println("Warning: " + file + " may be a symlink.  Ignoring.");
                    }
                } catch (IOException e) {
                    System.out.println("Warning: Cannot determine canonical file for " + file + " - ignoring.");
                }
            } else {
                file.delete();
            }
        }
        dir.delete();
        return true;
    }

    public static File createDir(File dir) {
        if (dir != null && !dir.isDirectory()) {
            dir.mkdir();
        }
        return dir;
    }

    public static File createFile(File file) {
        if (file != null && !file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void main(String[] args) {
        File file = new File("C:/mvcTest");
        createDir(file);
        file = new File("C:/mvcTest/test.txt");
        createFile(file);
    }
}
