package com.tetrasix.majix.uis;

import java.io.FilenameFilter;
import java.io.File;

public class ExtensionFilenameFilter implements FilenameFilter {

    String _ext[];

    public ExtensionFilenameFilter(String extension) {
        _ext = new String[1];
        _ext[0] = extension.toUpperCase();
    }

    public ExtensionFilenameFilter(String extension1, String extension2) {
        _ext = new String[2];
        _ext[0] = extension1.toUpperCase();
        _ext[1] = extension2.toUpperCase();
    }

    public boolean accept(File dir, String name) {
        for (int ii = 0; ii < _ext.length; ii++) {
            if (name.toUpperCase().endsWith(_ext[ii])) {
                return true;
            }
        }
        return false;
    }
}
