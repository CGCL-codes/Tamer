package org.sf.cocosc.twodimages.dialogs;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.sf.cocosc.twodimages.util.FileUtil;

public class ExtensionsFileFilter extends FileFilter {

    private String[] extensions = null;

    public ExtensionsFileFilter(String[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean accept(File file) {
        String extension = FileUtil.getExtension(file);
        return containsString(extension);
    }

    @Override
    public String getDescription() {
        return "Image files";
    }

    private boolean containsString(String target) {
        boolean found = false;
        if (target != null) {
            for (String value : extensions) {
                if (target.equalsIgnoreCase(value)) {
                    found = true;
                }
            }
        }
        return found;
    }
}
