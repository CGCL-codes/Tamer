package de.cologneintelligence.fitgoodies.runners;

import fit.Counts;

/**
 * Saves the result count of a specific file.
 *
 * @author jwierum
 * @version $Id: FileCount.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public class FileCount {

    private final Counts result;

    private final String filename;

    /**
	 * Initializes a new FileCount object.
	 *
	 * @param file the processed file
	 * @param counts the results
	 */
    public FileCount(final String file, final Counts counts) {
        filename = file;
        result = counts;
    }

    /**
	 * Returns the results of the file.
	 * @return fit results
	 */
    public final Counts getCounts() {
        return result;
    }

    /**
	 * Returns the filename.
	 * @return filename of the file
	 */
    public final String getFile() {
        return filename;
    }

    /**
	 * Compares two <code>FileCount</code> objects by filename.
	 *
	 * @param other other <code>FileCount</code> object.
	 * @return true if both objects describe the same file, false otherwise
	 */
    @Override
    public final boolean equals(final Object other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        return this.getFile().equals(((FileCount) other).getFile());
    }

    /**
	 * Returns the hash code of a file.
	 * @return the hash code
	 */
    @Override
    public final int hashCode() {
        return filename.hashCode();
    }
}
