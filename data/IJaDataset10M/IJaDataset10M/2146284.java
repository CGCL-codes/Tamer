package org.exmaralda.teide.ui;

import java.io.File;
import java.io.FileFilter;

/**
 * @author woerner
 *
 */
public class FF implements FileFilter {

    public boolean accept(File pathname) {
        return false;
    }
}
