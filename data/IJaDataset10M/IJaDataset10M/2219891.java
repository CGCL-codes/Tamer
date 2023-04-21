package ch.epfl.arni.jtossim.gui.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter {

    private String desc;

    private String ext;

    public SimpleFileFilter(String ext, String desc) {
        this.desc = desc;
        this.ext = ext;
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null && extension.equals(ext)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
