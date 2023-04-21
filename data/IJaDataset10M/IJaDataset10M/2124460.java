package com.groovytagger.ui.frames.model.filter;

import java.io.File;
import java.io.FilenameFilter;

public class DirectoryFilter implements FilenameFilter {

    public boolean accept(File file, String name) {
        return file.isDirectory();
    }
}
