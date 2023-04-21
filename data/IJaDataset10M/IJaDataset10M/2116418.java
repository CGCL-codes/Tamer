package org.nist.worldgen.ui;

import javax.swing.filechooser.FileFilter;
import java.io.*;

/**
 * Filters files by their extension.
 *
 * @author Stephen Carlson (NIST)
 * @version 4.0
 */
public class ExtensionFileFilter extends FileFilter implements FilenameFilter {

    private final String extension;

    private final String description;

    /**
	 * Creates a new ExtensionFileFilter. Would use FileNameExtensionFilter, but that's since
	 * 1.6, and this program must still support J5.
	 *
	 * @param extension the extension allowed
	 * @param description the description of this extension
	 */
    public ExtensionFileFilter(final String extension, final String description) {
        this.extension = "." + extension.toLowerCase();
        this.description = description + " (*" + this.extension + ")";
    }

    public boolean accept(File dir, String name) {
        return accept(new File(dir, name));
    }

    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(extension);
    }

    public String getDescription() {
        return description;
    }
}
