package unclej.filepath;

import java.io.File;

/**
 * Accepts only non-directory files.
 * @author scottv
 */
public class NonDirFilter extends FilelikeFilter {

    public boolean accept(Filelike f) {
        return !f.isDirectory();
    }

    public String toString() {
        return "non-directory";
    }

    public boolean shouldRecurse(File dir) {
        return true;
    }
}
