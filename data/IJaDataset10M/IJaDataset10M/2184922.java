package edu.kds.gui.dialogs;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class XMLFileFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null) {
            return extension.equalsIgnoreCase("xml");
        }
        return false;
    }

    private static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public String getDescription() {
        return "*.xml";
    }
}
