package de.huxhorn.lilith.swing.filefilters;

import java.io.File;
import java.io.FileFilter;

public class RrdFileFilter implements FileFilter {

    public boolean accept(File file) {
        if (!file.isFile()) {
            return false;
        }
        String absPath = file.getAbsolutePath();
        return absPath.toLowerCase().endsWith(".rrd");
    }
}
