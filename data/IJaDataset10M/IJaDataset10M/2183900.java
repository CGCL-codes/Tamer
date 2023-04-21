package de.sambalmueslie.geocache_planer.common.gui.file_filter;

import java.io.File;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Sambalmueslie
 * 
 * @date 21.05.2009
 * 
 */
public abstract class AbstractFileFilter extends FileFilter {

    /**
	 * constructor.
	 * 
	 * @param extensions
	 *            the extensions
	 */
    public AbstractFileFilter(final String[] extensions) {
        for (final String ext : extensions) {
            validExtensions.add(ext);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }
        final String extension = getExtension(f);
        if (extension != null) {
            for (final String ext : validExtensions) {
                if (ext.equals(extension)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Get the extension of a file.
	 * 
	 * @param f
	 *            the file
	 * @return the extension
	 */
    private String getExtension(final File f) {
        String ext = null;
        final String s = f.getName();
        final int i = s.lastIndexOf('.');
        if ((i > 0) && (i < s.length() - 1)) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /** collection with the vaild extensions. */
    private final Vector<String> validExtensions = new Vector<String>();
}
