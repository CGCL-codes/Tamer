package org.ctext.ite.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * The document filter for the file chooser.
 * @author W. Fourie
 */
public class DocumentFilter extends FileFilter {

    private static final String doc = "doc";

    private static final String odt = "odt";

    private static final String txt = "txt";

    private static final String html = "html";

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(doc) || extension.equals(odt) || extension.equals(txt) || extension.equals(html)) return true; else return false;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Translatable Documents";
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(".");
        if (i > 0 && i < s.length() - 1) ext = s.substring(i + 1).toLowerCase();
        return ext;
    }
}
