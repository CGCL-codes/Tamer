package com.rhythm.commons.io.filters;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author mlee
 */
public class HiddenDirectoryFileFilter implements FileFilter {

    private static final HiddenDirectoryFileFilter INSTANCE = new HiddenDirectoryFileFilter();

    /**
     * Returns a singleton instance of the {@code HiddenFileFilter}
     * @return
     */
    public static HiddenDirectoryFileFilter getInstance() {
        return INSTANCE;
    }

    private HiddenDirectoryFileFilter() {
    }

    public boolean accept(File pathname) {
        return ((pathname.isHidden()) ? false : pathname.isDirectory());
    }
}
