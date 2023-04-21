package org.sapient_platypus.utils.mac;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * A filter for Mac OS files.
 * @author Nicholas Daley
 */
public final class MacFileFilter extends FileFilter implements IOFileFilter, FilenameFilter {

    /**
     * The type to filter.
     */
    final MacType type;

    /**
     * Constructor.
     * @param type The type to filter.
     */
    public MacFileFilter(final MacType type) {
        this.type = type;
    }

    /**
     * Constructor.
     * @param type The type to filter as an <code>int</code>.
     */
    public MacFileFilter(final int type) {
        this.type = new MacType(type);
    }

    /**
     * Constructor.
     * @param type The type to filter as 4 bytes.
     */
    public MacFileFilter(final byte[] type) {
        this.type = new MacType(type);
    }

    /**
     * Constructor.
     * @param type The type to filter as a String.
     */
    public MacFileFilter(final String type) {
        this.type = new MacType(type);
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(final File f) {
        try {
            return this.type.equals(new MacType(f));
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(final File dir, final String name) {
        return accept(new File(dir, name));
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "'" + type + "' files";
    }
}
