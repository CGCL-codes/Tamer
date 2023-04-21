package com.darkhonor.rage.libs;

import java.io.*;

/**
 * The JarFileFilter class implements a {@link FileFilter} that looks for JAR
 * files
 * 
 * @author Alexander Ackerman
 * 
 * @version 1.0.0
 * 
 * @see java.io.FileFilter
 */
public class JarFileFilter implements FileFilter {

    /**
     * Compares the given file to see if it is a JAR file
     * 
     * @param pathname The file being filtered
     * @return <code>true</code> if the File is a JAR file and can be read
     *         by the user, <code>false</code> otherwise.
     */
    @Override
    public boolean accept(File pathname) {
        if (pathname.canRead() && !pathname.isDirectory() && pathname.getName().toLowerCase().endsWith(".jar")) {
            return true;
        }
        return false;
    }
}
