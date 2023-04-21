package imtek.optsuite.psi.acquisition.io.opb;

import imtek.optsuite.acquisition.io.BasicFileFilter;
import java.io.File;

/**
 * OPBFileFilter
 * 
 * @author Alexander Bieber
 */
public class OPBFileFilter extends BasicFileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.toLowerCase().equals("opb")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getDescription() {
        return "*.opb (OptSuite Phasedata Binary)";
    }
}
