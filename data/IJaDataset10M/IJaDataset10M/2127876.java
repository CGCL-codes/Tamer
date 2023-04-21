package com.worldware.misc;

import java.io.FilenameFilter;
import java.io.File;

public class dirFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        File temp = new File(dir, name);
        return temp.isDirectory();
    }
}
