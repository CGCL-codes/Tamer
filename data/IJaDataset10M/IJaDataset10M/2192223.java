package com.xmxsuperstar.ant.task.model;

import java.io.File;

public class LibFileAnalyzier {

    private boolean isSourceLib = false;

    private String libName;

    private String libVersion;

    private String libExt;

    public LibFileAnalyzier(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String prefix = fileName.substring(0, dotIndex);
        libExt = fileName.substring(dotIndex + 1);
        if (prefix.contains("sources")) {
            isSourceLib = true;
            int i = prefix.lastIndexOf('-');
            prefix = prefix.substring(0, i);
        }
        int j = prefix.lastIndexOf('-');
        libName = prefix.substring(0, j);
        libVersion = prefix.substring(j + 1);
    }

    public String getLibName() {
        return libName;
    }

    public String getLibVersion() {
        return libVersion;
    }

    public String getLibExt() {
        return libExt;
    }

    public boolean isSourceLib() {
        return isSourceLib;
    }
}
