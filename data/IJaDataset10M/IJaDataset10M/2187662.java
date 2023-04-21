package de.alindow.vcrinfo.view;

import java.io.File;
import java.io.FilenameFilter;

public class FilenamePatternFilterImpl implements FilenameFilter {

    /**
	 * Pattern to filter for.
	 */
    private String pattern;

    /**
	 * Creates a filter for the given Pattern.
	 * 
	 * @param iPattern
	 *            Pattern to filter for with this filter
	 */
    public FilenamePatternFilterImpl(final String iPattern) {
        if (iPattern == null) {
            throw new IllegalArgumentException("Mit null aufgerufen");
        }
        this.pattern = iPattern;
    }

    /**
	 * Accepts files with the right filename.
	 * 
	 * @param dir
	 *            Directory
	 * @param fname
	 *            Filename
	 * @return Return
	 */
    public final boolean accept(final File dir, final String fname) {
        return fname.matches(pattern);
    }
}
