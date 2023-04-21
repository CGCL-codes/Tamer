package com.tetrasix.util;

import java.io.*;

public class FilePathUtility {

    public static String getDirectory(String path) {
        String dir = "";
        int pos = path.lastIndexOf(File.separator);
        if (pos != -1) {
            dir = path.substring(0, pos);
        }
        return dir;
    }

    public static String getFileName(String path) {
        String filename = path;
        int pos = path.lastIndexOf(File.separator);
        if (pos != -1) {
            filename = path.substring(pos + 1);
        }
        pos = filename.lastIndexOf(".");
        if (pos != -1) {
            filename = filename.substring(0, pos);
        }
        return filename;
    }

    public static String SubstituteExtension(String path, String newext) {
        int pos = path.lastIndexOf(".");
        if (pos > -1) {
            return path.substring(0, pos) + newext;
        } else {
            System.out.println("Bad file name format (extension expected) " + path);
            return null;
        }
    }
}
