package org.ximtec.igesture.tool.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Comment
 * @version 1.0 26.10.2008
 * @author Ueli Kurmann
 */
public class ExtensionFileFilter extends FileFilter {

    private static final String DOT = ".";

    private String extension;

    private String description;

    public ExtensionFileFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if (f != null) {
            return f.isDirectory() || f.getName().endsWith(DOT + extension);
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }
}
