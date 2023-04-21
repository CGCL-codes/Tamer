package vademecum.io.reader;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ClsFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory() || f.getName().toLowerCase().endsWith("cls")) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return ".cls files";
    }
}
