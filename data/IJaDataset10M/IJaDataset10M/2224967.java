package cz.muni.pdfjbim;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Radim Hatlapatka (208155@mail.muni.cz)
 */
public class Jbig2FilenameFilter implements FilenameFilter {

    private String basename = "output";

    public Jbig2FilenameFilter() {
    }

    public Jbig2FilenameFilter(String basename) {
        this.basename = basename;
    }

    public boolean accept(File dir, String name) {
        if (name == null) {
            return false;
        }
        return name.startsWith(basename);
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }
}
