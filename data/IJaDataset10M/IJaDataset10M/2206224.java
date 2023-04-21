package au.gov.nla.aons.repository.crawl.file.util;

import java.io.File;

public class FileUtil {

    public static boolean isFileSubDir(File basePath, File subDir) {
        File currentDir = subDir;
        while (subDir != null) {
            if (basePath.equals(currentDir)) {
                return true;
            }
            currentDir = currentDir.getParentFile();
        }
        return false;
    }
}
