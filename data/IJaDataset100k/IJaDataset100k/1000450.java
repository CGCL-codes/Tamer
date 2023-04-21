package org.gerhardb.lib.io;

import java.io.File;
import java.io.FileFilter;

/**
 * Filters out directories.
 * Based on having a period in name at moment.
 */
public class NoFiles implements FileFilter {

    public static final NoFiles NO_FILES = new NoFiles();

    /**
    *
    * @param file The path to the file without the actual file.
    * @return true this is a file otherwise false
    */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return false;
    }
}
