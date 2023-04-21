package corpse;

import java.io.File;
import java.io.FileFilter;

/**
 * A filter that only accepts files whose names end with ".txt".
 * @author Georgi Boychev
 */
public class Filter implements FileFilter {

    /**
     * Accept a file if its name ends with ".txt".
     */
    public boolean accept(File file) {
        return file.getName().endsWith(".txt");
    }
}
