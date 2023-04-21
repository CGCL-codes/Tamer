package net.sf.jretina.ui.util;

public class FileUtil {

    public static String replaceExtension(String file, String newExt) {
        int p = file.lastIndexOf('.');
        return (p == -1 ? file : file.substring(0, p)) + newExt;
    }
}
