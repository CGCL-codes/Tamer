package net.sourceforge.importscrubber;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Encapsulates a file filter which accepts directories and (Java source file names only when there is a class file in the same directory).
 */
public class JavaFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        File file = new File(dir + ImportScrubber.FILE_SEPARATOR + name);
        if (file.isDirectory() || name.endsWith(".java")) {
            return true;
        }
        return false;
    }
}
