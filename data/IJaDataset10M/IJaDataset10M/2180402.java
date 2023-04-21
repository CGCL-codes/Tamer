package com.melloware.jukes.file.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filters for subdirs in JFileChooser.
 * <p>
 * Copyright (c) 2010 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 * AZ 2010
 */
public final class dirFilter extends FileFilter {

    /**
     * Default Constuctor
     */
    public dirFilter() {
        super();
    }

    public String getDescription() {
        return "Sub directories";
    }

    /**
     * Accept all subdirs
     */
    public boolean accept(File dir, String name) {
        return dir.isDirectory();
    }

    /**
     * Accept all subdirs
     */
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
