package rs.realestate.util;

import java.io.File;

public class FileUtil {

    public static void makeFolderRoot(String path) {
        String root = getFolderRoot(path);
        File f = new File(root);
        f.mkdir();
    }

    public static String getFolderRoot(String path) {
        File file = new File(path);
        if (file.isFile()) {
            path = file.getAbsolutePath();
            int lastIndex = path.lastIndexOf("\\");
            return path.substring(0, lastIndex);
        } else {
            return file.getPath();
        }
    }

    public static String addPrefixToFileName(String path, String prefix) {
        File file = new File(path);
        if (!file.exists()) return null;
        if (file.isFile()) {
            path = file.getAbsolutePath();
            int lastIndex = path.lastIndexOf("\\");
            String root = path.substring(0, lastIndex);
            String relPath = path.substring(root.length() + 1);
            return path.substring(0, lastIndex) + "\\" + prefix + relPath;
        } else {
            throw new RuntimeException();
        }
    }
}
