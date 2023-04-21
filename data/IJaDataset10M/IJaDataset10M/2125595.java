package mr3.util;

import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * @author takeshi morita
 */
public class MR3FileFilter extends FileFilter {

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("mr3")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getDescription() {
        return "MR^3 Project File (*.mr3)";
    }
}
