package imtek.optsuite.oct.io;

import java.io.File;
import imtek.optsuite.acquisition.io.BasicFileFilter;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public class PGMFileFilter extends BasicFileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.toLowerCase().equals("pgm")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getDescription() {
        return "*pgm (Document me!!)";
    }
}
