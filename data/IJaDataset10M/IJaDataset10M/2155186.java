package com.nexirius.framework.gadgets.filechooser;

import java.io.File;
import java.io.FilenameFilter;

class DirectoryOnly implements FilenameFilter {

    public boolean accept(File dir, String name) {
        File f = new File(dir, name);
        return f.isDirectory();
    }
}
