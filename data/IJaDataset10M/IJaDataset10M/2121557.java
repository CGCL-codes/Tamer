package com.sun.opengl.util;

import java.io.*;

/** Utilities for dealing with files. */
public class FileUtil {

    private FileUtil() {
    }

    /**
   * Returns the lowercase suffix of the given file name (the text
   * after the last '.' in the file name). Returns null if the file
   * name has no suffix. Only operates on the given file name;
   * performs no I/O operations.
   *
   * @param file name of the file
   * @return lowercase suffix of the file name
   * @throws NullPointerException if file is null
   */
    public static String getFileSuffix(File file) {
        return getFileSuffix(file.getName());
    }

    /**
   * Returns the lowercase suffix of the given file name (the text
   * after the last '.' in the file name). Returns null if the file
   * name has no suffix. Only operates on the given file name;
   * performs no I/O operations.
   *
   * @param filename name of the file
   * @return lowercase suffix of the file name
   * @throws NullPointerException if filename is null
   */
    public static String getFileSuffix(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0) {
            return null;
        }
        return toLowerCase(filename.substring(lastDot + 1));
    }

    private static String toLowerCase(String arg) {
        if (arg == null) {
            return null;
        }
        return arg.toLowerCase();
    }
}
