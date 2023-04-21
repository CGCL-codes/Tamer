package com.stromberglabs.util.file;

import java.io.File;

public class FileFilter {

    private String mFilterRegex;

    public FileFilter(String regex) {
        mFilterRegex = regex;
    }

    public boolean passes(File file) {
        assert (file != null);
        return file.getName().toLowerCase().matches(mFilterRegex);
    }
}
