package desmoj.extensions.experimentation.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class XMLFilter extends FileFilter {

    /**
	 * Accepts all directories and xml files.
	 * 
	 * @param file
	 *            file: file to be filtered
	 */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext != null) {
            if (ext.equals("xml")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /** @return The description of this filter */
    public String getDescription() {
        return "*.xml";
    }
}
