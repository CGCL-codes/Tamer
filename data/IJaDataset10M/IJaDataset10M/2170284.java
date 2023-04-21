package playground.tnicolai.matsim4opus.utils.io.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * @author thomas
 *
 */
public class TabFilter implements FileFilter {

    private final String tab = "tab";

    public boolean accept(File file) {
        if (file.getName().toLowerCase().endsWith(tab)) return true;
        return false;
    }
}
