package jFileLib.common;

import java.io.File;

public class Path {

    public static boolean isValidePath(String path) {
        if (path == null || path.isEmpty() == true) return false; else return true;
    }

    public static String combine(String path, String file) {
        return Path.getCorrectPath(path) + file;
    }

    public static String getCorrectPath(String path) {
        if (Path.isValidePath(path) == false) return path;
        if (path.charAt(path.length() - 1) != File.separatorChar) path += File.separatorChar;
        return path;
    }

    public static boolean isFile(String path) {
        if (Path.isValidePath(path) == false) return false;
        java.io.File file = new java.io.File(path);
        return file.exists() && file.isFile();
    }

    public static boolean isDirectory(String path) {
        if (Path.isValidePath(path) == false) return false;
        java.io.File file = new java.io.File(path);
        return file.exists() && file.isDirectory();
    }

    public static String getAbsolutePath(String name) {
        java.io.File file = new java.io.File(name);
        return file.getAbsolutePath();
    }

    public static String getFileName(String path) {
        if (Path.isValidePath(path) == false) return null;
        java.io.File file = new java.io.File(path);
        return file.getName();
    }

    public static String getParent(String path) {
        if (Path.isValidePath(path) == false) return null;
        java.io.File file = new java.io.File(path);
        if (file.exists()) return file.getParent();
        return null;
    }

    public static boolean exists(String path) {
        if (Path.isValidePath(path) == false) return false;
        java.io.File file = new java.io.File(path);
        return file.exists();
    }
}
