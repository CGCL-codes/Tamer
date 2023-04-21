package org.outerj.pollo.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter {

    protected String extension;

    protected String description;

    public ExtensionFileFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        if (f.getName().endsWith(extension)) return true; else return false;
    }

    public String getDescription() {
        return description;
    }
}
