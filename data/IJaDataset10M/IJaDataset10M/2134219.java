package csheets.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

/**
 * A filename filter for filtering on filename extensions.
 * @author Einar Pehrson
 */
public class FilenameExtensionFilter implements FilenameFilter {

    /** The extensions of allowed files. */
    private List<String> extensions;

    /**
	 * Creates a new filter for files with the given extensions.
	 * @param extensions the extension of allowed files
	 */
    public FilenameExtensionFilter(String... extensions) {
        this(Arrays.asList(extensions));
    }

    /**
	 * Creates a new filter for files with the given extensions.
	 * @param extensions the extension of allowed files
	 */
    public FilenameExtensionFilter(List<String> extensions) {
        if (extensions.size() == 0) throw new IllegalArgumentException();
        this.extensions = extensions;
    }

    public boolean accept(File file, String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase().trim();
        return extensions.contains(extension);
    }

    /**
	 * Returns the file extensions that the filter allows.
	 * @return the file extensions that the filter allows
	 */
    public String[] getExtensions() {
        return extensions.toArray(new String[extensions.size()]);
    }
}
