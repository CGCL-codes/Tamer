package org.lindenb.swing;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.HashSet;

/**
 * Basic FileFilter based on files extension
 * usage:
 *  FileExtensionFilter f=new FileExtensionFilter("Images","jpeg","jpg","png","gif")
 *
 */
public class FileExtensionFilter extends FileFilter {

    /** filter description */
    private String desc = null;

    /** set of extensions */
    private HashSet<String> suffixes = new HashSet<String>();

    /**
         * @arg filter description
         * @suffixes file suffixes (case insensitive, with or without dot)
         */
    public FileExtensionFilter(String desc, String... suffixes) {
        if (desc == null) desc = "Filter";
        this.desc = desc;
        for (String s : suffixes) {
            s = s.trim().toLowerCase();
            if (!s.startsWith(".")) s = "." + s;
            if (s.length() == 1) continue;
            this.suffixes.add(s);
        }
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String suff = f.getName().toLowerCase().trim();
        for (String s : this.suffixes) {
            if (suff.endsWith(s)) return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public String toString() {
        return getDescription() + ":" + this.suffixes.toString();
    }

    public static String getFileExtension(File f) {
        String s = f.getName();
        int n = s.lastIndexOf('.');
        if (n == -1) return null;
        return s.substring(n + 1);
    }

    public static FileExtensionFilter createImageFilter() {
        return new FileExtensionFilter("Images", ".jpeg", ".jpg", ".png", ".gif");
    }
}
