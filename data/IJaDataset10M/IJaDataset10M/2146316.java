package wsi.ra.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expression filter for files.
 */
public class BasicRegExpFilenameFilter implements FilenameFilter, RegExpFilenameFilter {

    private Pattern pattern;

    private List skipIfExtension;

    public BasicRegExpFilenameFilter(String regExp) {
        pattern = Pattern.compile(regExp);
    }

    public boolean accept(File dir, String name) {
        Matcher m;
        m = pattern.matcher(name);
        if (m.matches()) {
            if (skipIfExtension != null) {
                int size = skipIfExtension.size();
                for (int i = 0; i < size; i++) {
                    if (name.endsWith((String) skipIfExtension.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void addSkipExtension(String extension) {
        if (skipIfExtension == null) {
            skipIfExtension = new Vector();
        }
        skipIfExtension.add(extension);
    }
}
