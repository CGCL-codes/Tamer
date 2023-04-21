package dk.i2m.converge.core.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * {@link FilenameFilter} for filtering files based on their file extension. The
 * file extension being the name that comes after the last dot (.) in a
 * filename.
 *
 * @author Allan Lykke Christensen
 */
public class FileExtensionFilter implements FilenameFilter {

    private String extension;

    /**
     * Creates a new instance of {@link FileExtensionFilter}.
     *
     * @param extension Name of the file extension (e.g. xml)
     */
    public FileExtensionFilter(String extension) {
        this.extension = extension;
    }

    /**
     * Method executed for each file in the directory to determine if it should
     * be included in the file selection.
     *
     * @param directory Directory of the file
     * @param filename Name of the file
     * @return {@code true} if the file should be included, otherwise
     *         {@code false}
     */
    @Override
    public boolean accept(File directory, String filename) {
        boolean fileOK = true;
        if (extension != null) {
            fileOK &= filename.toLowerCase().endsWith('.' + extension);
        }
        return fileOK;
    }
}
