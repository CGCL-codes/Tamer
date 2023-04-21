package com.armatiek.infofuze.stream.filesystem.filefilter;

import java.io.Serializable;
import com.armatiek.infofuze.stream.filesystem.FileIf;

/**
 * This filter accepts <code>File</code>s that are files (not directories).
 * 
 * Based on code from Apache Commons IO version 2.01.
 * The main difference is that this class filters objects that 
 * implement {@link com.armatiek.infofuze.stream.filesystem.FileIf}, 
 * not {@link java.io.File}.
 */
public class FileFileFilter implements IOFileFilter, Serializable {

    private static final long serialVersionUID = 1L;

    /** Singleton instance of file filter */
    public static final IOFileFilter FILE = new FileFileFilter();

    /**
     * Restrictive consructor.
     */
    protected FileFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     *
     * @param file  the File to check
     * @return true if the file is a file
     */
    @Override
    public boolean accept(FileIf file) {
        return file.isFile();
    }
}
