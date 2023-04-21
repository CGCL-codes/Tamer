package se.unlogic.standardutils.io;

import java.io.File;
import java.io.FileFilter;

public class EndsWithFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

    protected String suffix;

    protected boolean allowFolders;

    public EndsWithFileFilter(String suffix) {
        super();
        this.suffix = suffix;
    }

    public EndsWithFileFilter(String suffix, boolean allowFolders) {
        super();
        this.suffix = suffix;
        this.allowFolders = allowFolders;
    }

    @Override
    public boolean accept(File file) {
        if (file.getName().endsWith(suffix)) {
            return true;
        } else if (allowFolders && file.isDirectory()) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return suffix;
    }
}
