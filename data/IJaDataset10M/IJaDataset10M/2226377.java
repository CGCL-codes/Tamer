package org.apache.nutch.parse.mspowerpoint;

import java.io.File;
import java.io.FilenameFilter;

class FileExtensionFilter implements FilenameFilter {

    private String ext = "*";

    /**
   * @param ext
   */
    public FileExtensionFilter(String ext) {
        this.ext = ext;
    }

    public boolean accept(File dir, String name) {
        if (name.endsWith(this.ext)) return true;
        return false;
    }

    public String toString() {
        return this.ext;
    }
}
