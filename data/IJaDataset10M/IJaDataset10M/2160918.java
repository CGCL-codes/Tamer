package com.pegaa.uploader.common;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 *
 * @author tayfun
 */
public class CustomFileFilter implements FileFilter {

    private ArrayList<String> extensions = null;

    private int extSize = 0;

    public CustomFileFilter() {
        this.extensions = new ArrayList<String>(4);
    }

    /**
     *  Add file extension to the filter list.
     * Before adding extension string lowercased
     * 
     * @param extension
     */
    public void addExtension(String extension) {
        this.extensions.add(extension.toLowerCase());
        this.extSize++;
    }

    /**
     *  Checks for given extensions and returns acceptance of extension.
     * Accepts all files of no extension given.
     * 
     * Does not show hidden files.
     * @param pathname
     * @return
     */
    public boolean accept(File f) {
        if (f.isHidden()) {
            return false;
        }
        if (extSize == 0) {
            if (f.isDirectory() != true) return true; else return false;
        }
        for (int i = 0; i < this.extSize; i++) {
            if (f.getAbsolutePath().toLowerCase().endsWith(this.extensions.get(i))) {
                return true;
            }
        }
        return false;
    }
}
