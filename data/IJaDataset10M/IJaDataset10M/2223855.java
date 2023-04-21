package dva.acuitytest;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author J-Chris
 */
public class AcuityTestFileFilter extends FileFilter {

    String pattern = "_acuitytest-data.xml";

    static final String description = "Acuity test data (*.xml)";

    public AcuityTestFileFilter() {
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String s = f.getName();
        return s.endsWith(pattern);
    }

    public String getDescription() {
        return description;
    }
}
