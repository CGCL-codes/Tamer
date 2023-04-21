package org.sventon.appl;

import org.apache.commons.lang.Validate;
import java.io.File;
import java.io.FileFilter;

/**
 * File filter that accepts sventon configuration directories.
 */
class SventonConfigDirectoryFileFilter implements FileFilter {

    /**
   * The name of the sventon configuration file.
   */
    private final String configurationFilename;

    /**
   * Constructor.
   *
   * @param configurationFilename Name of the sventon configuration file.
   */
    public SventonConfigDirectoryFileFilter(final String configurationFilename) {
        Validate.notEmpty(configurationFilename, "Config filename cannot be empty");
        this.configurationFilename = configurationFilename;
    }

    /**
   * Checks to see if the file is a sventon configuration directory.
   *
   * @param path the Path to check
   * @return true if the file is a sventon configuration directory.
   */
    public boolean accept(final File path) {
        return path.isDirectory() && new File(path, configurationFilename).exists();
    }
}
