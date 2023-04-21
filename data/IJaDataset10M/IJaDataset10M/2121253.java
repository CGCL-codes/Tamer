package de.dlr.davinspector.plugin;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class is used to filter only *.jar files.
 *
 * @version $LastChangedRevision$
 * @author Jochen Wuest
 */
public class JarFilenameFilter implements FilenameFilter {

    /**
     * {@inheritDoc}
     *
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept(File directory, String filename) {
        return filename.toLowerCase().endsWith(".jar");
    }
}
