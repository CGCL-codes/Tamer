package com.armatiek.infofuze.stream.filesystem.filefilter;

import java.io.Serializable;
import com.armatiek.infofuze.stream.filesystem.FileIf;

/**
 * This filter accepts <code>File</code>s that are hidden.
 * <p>
 * Example, showing how to print out a list of the
 * current directory's <i>hidden</i> files:
 *
 * Based on code from Apache Commons IO version 2.01.
 * The main difference is that this class filters objects that 
 * implement {@link com.armatiek.infofuze.stream.filesystem.FileIf}, 
 * not {@link java.io.File}.
 */
public class HiddenFileFilter implements IOFileFilter, Serializable {

    private static final long serialVersionUID = 1L;

    /** Singleton instance of <i>hidden</i> filter */
    public static final IOFileFilter HIDDEN = new HiddenFileFilter();

    /** Singleton instance of <i>visible</i> filter */
    public static final IOFileFilter VISIBLE = new NotFileFilter(HIDDEN);

    /**
     * Restrictive constructor.
     */
    protected HiddenFileFilter() {
    }

    /**
     * Checks to see if the file is hidden.
     * 
     * @param file  the File to check
     * @return <code>true</code> if the file is
     *  <i>hidden</i>, otherwise <code>false</code>.
     */
    @Override
    public boolean accept(FileIf file) {
        return file.isHidden();
    }
}
